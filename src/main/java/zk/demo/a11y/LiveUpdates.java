package zk.demo.a11y;

import org.zkoss.zk.ui.Executions;
import zk.demo.a11y.domain.Order;
import zk.demo.a11y.domain.OrderStatus;
import zk.demo.a11y.repository.OrderRepo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.util.Collections.emptySet;

public class LiveUpdates {

	private final Random rnd = new Random();

	private final OrderRepo orderRepo = (OrderRepo) Executions.getCurrent().getDesktop().getAttribute("orderRepo");

	// in a real live scenario this would an external trigger
	private final AtomicBoolean liveUpdatesEnabled = new AtomicBoolean(false);
	private ScheduledExecutorService liveUpdatesScheduler;
	private final Consumer<Order> onOrderUpdated;

	public LiveUpdates(Consumer<Order> onOrderUpdated) {
		this.onOrderUpdated = onOrderUpdated;
	}

	public void toggle(boolean enabled) {
		liveUpdatesEnabled.set(enabled);
		if(enabled) {
			liveUpdatesScheduler = Executors.newSingleThreadScheduledExecutor();
			scheduleNextUpdate();
		} else {
			liveUpdatesScheduler.shutdownNow();
		}
	}

	private void scheduleNextUpdate() {
		liveUpdatesScheduler.schedule(() -> {
			List<Order> orders = orderRepo.findOrders("", 0, 999999, emptySet(), emptySet());
			OrderStatus[] orderStatuses = OrderStatus.values();
			OrderStatus randomStatus = orderStatuses[rnd.nextInt(orderStatuses.length)];
			Order randomOrder = orders.get(rnd.nextInt(orders.size()));
			randomOrder.setStatus(randomStatus);
			onOrderUpdated.accept(randomOrder);
			if(liveUpdatesEnabled.get()) {
				scheduleNextUpdate();
			}
		}, (long) (rnd.nextInt(5000) + 5000), TimeUnit.MILLISECONDS);
	}
}
