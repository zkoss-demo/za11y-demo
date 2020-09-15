package zk.demo.a11y.domain;

import java.math.BigDecimal;

public class OrderItem {
    private String name;
    private String description;
    private BigDecimal price;

    public OrderItem(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderItem: " + name + '(' + description + ')';
    }
}
