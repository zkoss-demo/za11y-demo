package zk.demo.a11y;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Rangeslider;
import org.zkoss.zkmax.zul.Searchbox;
import org.zkoss.zul.*;
import zk.demo.a11y.domain.Order;
import zk.demo.a11y.domain.OrderStatus;
import zk.demo.a11y.repository.OrderRepo;

import java.util.List;

public abstract class OrderListComposer extends SelectorComposer<Component> {

    // usually injected by an IOC container
    private final OrderRepo orderRepo = (OrderRepo) Executions.getCurrent().getDesktop().getAttribute("orderRepo");

    @Wire
    private Listbox orderList;
    private ListModelList<Order> orderListModel;

    @Wire
    private Textbox searchFilter;
    @Wire
    private Rangeslider priceFilter;
    @Wire
    private Searchbox<String> ingredientsFilter;
    @Wire
    private Selectbox statusFilter;
    private ListModelList<OrderStatus> orderStatusModel;
    @Wire
    private Checkbox liveUpdatesToggle;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        orderListModel = new ListModelList<>();
        orderList.setModel(orderListModel);

        ListModelList<String> ingredientsModel = new ListModelList<>(orderRepo.findIngredients());
        ingredientsModel.setMultiple(true);
        ingredientsFilter.setModel(ingredientsModel);

        this.orderStatusModel = new ListModelList<>(OrderStatus.values());
        this.orderStatusModel.add(0, null);
        statusFilter.setModel(this.orderStatusModel);

        refreshOrders();
    }

    private void refreshOrders() {
        List<Order> results = orderRepo.findOrders(searchFilter.getValue(),
                priceFilter.getStartValue(),
                priceFilter.getEndValue(),
                ingredientsFilter.getSelectedItems(),
                orderStatusModel.getSelection());
        if(!results.equals(orderListModel)) {
            orderListModel.clear();
            orderListModel.addAll(results);
        }
    }

    @Listen("onClick=#refresh")
    public void refresh() {
        refreshOrders();
    }

    @Listen("onCheck=#liveUpdatesToggle")
    public void toggleLiveUpdates() {
        onToggleLiveUpdates(liveUpdatesToggle.isChecked());
    }

    @Listen("onComplete=#orderList")
    public void onComplete(Event event) {
        onCompleteOrder((Order) event.getData());
    }

    @Listen("onShowDetails=#orderList")
    public void onShowDetails(Event event) {
        onShowDetails((Order) event.getData());
    }

    @Listen("onCancel=#orderList")
    public void onCancel(Event event) {
        onCancelOrder((Order) event.getData());
    }

    protected abstract void onCompleteOrder(Order order);
    protected abstract void onCancelOrder(Order order);
    protected abstract void onShowDetails(Order order);
    protected abstract void onToggleLiveUpdates(boolean checked);

    public void refreshRow(Order order) {
        if(order.isCanceled()) {
            orderListModel.remove(order);
        } else {
            orderListModel.notifyChange(order);
        }
    }

}
