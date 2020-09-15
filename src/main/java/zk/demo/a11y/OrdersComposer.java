package zk.demo.a11y;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import zk.demo.a11y.domain.Order;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.zkoss.zul.Messagebox.Button.CANCEL;
import static org.zkoss.zul.Messagebox.Button.OK;
import static org.zkoss.zul.Messagebox.QUESTION;
import static zk.demo.a11y.domain.OrderStatus.COMPLETE;

public class OrdersComposer extends SelectorComposer<Component> {

    private static final Messagebox.Button[] OK_CANCEL = {OK, CANCEL};
    private static final String[] CANCEL_ORDER_LABELS = {"Cancel Order", "Keep Order"};

    @Wire
    private Tabbox ordersNav;
    private ListModelList<NavInfo> navModel = new ListModelList<>();
    private OrderListComposer orderListComposer;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        orderListComposer = new OrderListComposer() {
            @Override
            protected void onShowDetails(Order order) {
                NavInfo navInfo = new NavInfo("orderDetails", "Order #" + order.getNumber(),
                        newOrderDetailsComposer(order), true);
                navModel.add(navInfo);
                navModel.addToSelection(navInfo);
            }

            @Override
            protected void onCompleteOrder(Order order) {
                completeOrder(order);
            }

            @Override
            protected void onCancelOrder(Order order) {
                cancelOrder(order);
            }
        };
        Map<String, Object> navData = Collections.singletonMap("composer", orderListComposer);
        navModel.add(new NavInfo("orderList", "Order List", orderListComposer, false));
        navModel.addToSelection(navModel.get(0));
        ordersNav.setModel(navModel);
    }

    private OrderDetailsComposer newOrderDetailsComposer(Order order) {
        return new OrderDetailsComposer(order) {
            @Override
            protected void onOrderUpdated(Order order) {
                orderListComposer.refreshRow(order);
            }

            @Override
            protected void onCancelOrder(Order order) {
                cancelOrder(order);
            }
        };
    }

    @Listen("onCloseTab=#ordersNav")
    public void onCloseTab(Event event) {
        NavInfo navInfo = (NavInfo) event.getData();
        navModel.remove(navInfo);
    }

    private void completeOrder(Order order) {
        order.setStatus(COMPLETE);
        navInfosByOrder(order).forEach(navModel::notifyChange);
        orderListComposer.refreshRow(order);
    }

    private void cancelOrder(Order order) {
        Messagebox.show("Are you sure to cancel order?", "Order #" + order.getNumber(), OK_CANCEL, CANCEL_ORDER_LABELS, QUESTION, OK, event -> {
            if (event.getButton() == OK) {
                order.setCanceled(true);
                navModel.removeAll(navInfosByOrder(order));
                orderListComposer.refreshRow(order);
            }
        });
    }

    private Collection<NavInfo> navInfosByOrder(Order order) {
        return navModel.stream().filter(navInfo -> {
            Object composer = navInfo.getComposer();
            if (composer instanceof OrderDetailsComposer) {
                return ((OrderDetailsComposer) composer).getOrder().equals(order);
            }
            return false;
        }).collect(Collectors.toList());
    }

    public static class NavInfo<T> {
        private String navId;
        private String title;
        private T composer;
        private boolean closable;

        public NavInfo(String navId, String title, T composer, boolean closable) {
            this.navId = navId;
            this.title = title;
            this.composer = composer;
            this.closable = closable;
        }

        public String getNavId() {
            return navId;
        }

        public String getTitle() {
            return title;
        }

        public T getComposer() {
            return composer;
        }

        public boolean isClosable() {
            return closable;
        }
    }
}
