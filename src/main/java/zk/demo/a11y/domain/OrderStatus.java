package zk.demo.a11y.domain;

public enum OrderStatus {
    OPEN("Open"),
    PREPARING("Preparing"),
    DELIVERING("Delivering"),
    COMPLETE("Complete");

    private final String label;

    public String getLabel() {
        return label;
    }

    OrderStatus(String label) {
        this.label = label;
    }
}
