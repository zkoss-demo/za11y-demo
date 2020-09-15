package zk.demo.a11y.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static zk.demo.a11y.domain.OrderStatus.OPEN;

public class Order {
    private final long number;
    private OrderStatus status = OPEN;
    private final Customer customer;
    private List<OrderItem> items = new ArrayList<>();

    private static final AtomicInteger orderNumberSequence = new AtomicInteger(1000);
    private boolean canceled;

    public Order(Customer customer) {
        this.number = orderNumberSequence.getAndIncrement();
        this.customer = customer;
    }

    public long getNumber() {
        return number;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getNumItems() {
        return items.size();
    }

    public String searchString() {
        return number + "|" + customer.searchString() + "|" + items.stream().map(OrderItem::getName).collect(Collectors.joining("|"));
    }
}
