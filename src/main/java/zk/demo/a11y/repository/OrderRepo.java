package zk.demo.a11y.repository;

import zk.demo.a11y.domain.Order;
import zk.demo.a11y.domain.OrderItem;
import zk.demo.a11y.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static zk.demo.a11y.domain.OrderStatus.*;

public class OrderRepo {

    private List<Order> orders = new ArrayList<>();

    public OrderRepo(int numOrders, int maxNumItems) {
        for(int i = 0; i < numOrders; i++) {
            Order order = new Order(randomFrom(MockData.customers));
            int numItems = rnd.nextInt(maxNumItems) + 1;
            for(int j = 0; j < numItems; j++) {
                order.getItems().add(randomFrom(MockData.items));
            }
            orders.add(order);
        }
        orders.get(1).setStatus(PREPARING);
        orders.get(2).setStatus(DELIVERING);
        orders.get(3).setStatus(COMPLETE);
    }

    public List<String> findIngredients() {
        return MockData.ingredients;
    }

    private Random rnd = new Random();
    public <T> T randomFrom(List<T> list) {
        return list.get(rnd.nextInt(list.size()));
    }

    // don't do this in java code, use a DB or full text index instead
    public List<Order> findOrders(String search, int minPrice, int maxPrice, Set<String> ingredients, Set<OrderStatus> status) {
        return orders.stream()
                .filter(order -> status.isEmpty() || status.contains(null) || status.contains(order.getStatus()))
                .filter(totalBetween(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                .filter(order -> search.isEmpty() || order.searchString().toLowerCase().contains(search.toLowerCase()))
                .filter(ingredientsMatch(ingredients))
                .collect(Collectors.toList());
    }

    private Predicate<? super Order> ingredientsMatch(Set<String> ingredients) {
        return order -> ingredients.isEmpty() || ingredients.stream()
                .map(String::toLowerCase)
                .anyMatch(ingredient -> order.getItems().stream()
                        .map(OrderItem::getDescription)
                        .anyMatch(description -> description.toLowerCase().contains(ingredient)));
    }

    public Predicate<Order> totalBetween(BigDecimal min, BigDecimal max) {
        return order -> {
            BigDecimal value = order.getTotal();
            return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        };
    }
}
