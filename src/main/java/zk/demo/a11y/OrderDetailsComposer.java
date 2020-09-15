package zk.demo.a11y;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.DefaultStepModel;
import org.zkoss.zkmax.zul.Stepbar;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import zk.demo.a11y.domain.Order;
import zk.demo.a11y.domain.OrderStatus;

public abstract class OrderDetailsComposer extends SelectorComposer {

    private Order order;

    @Wire
    private Selectbox orderStatus;
    @Wire
    private Stepbar orderSteps;
    private DefaultStepModel<OrderStatus> orderStepsModel;

    public OrderDetailsComposer(Order order) {
        this.order = order;
    }

    @Override
    public void doBeforeComposeChildren(Component comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        //make order variable available in zul file via ${order}
        comp.setAttribute("order", order);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        ListModelList<OrderStatus> orderStatusModel = new ListModelList<>(OrderStatus.values());
        orderStatus.setModel(orderStatusModel);
        orderStatusModel.addToSelection(order.getStatus());

        orderStepsModel = new DefaultStepModel<>();
        orderSteps.setModel(orderStepsModel);
        orderStatusModel.forEach(orderStepsModel::add);
        orderStepsModel.setActiveStep(order.getStatus());
    }

    @Listen("onSelect=#orderStatus")
    public void onSelectStatus(SelectEvent event) {
        OrderStatus orderStatus = (OrderStatus) event.getSelectedObjects().iterator().next();
        order.setStatus(orderStatus);
        orderStepsModel.setActiveStep(orderStatus);
        onOrderUpdated(order);
    }

    @Listen("onClick=#cancelOrder")
    public void onCancelOrder() {
        onCancelOrder(order);
    }

    protected abstract void onOrderUpdated(Order order);

    protected abstract void onCancelOrder(Order order);

    public Order getOrder() {
        return order;
    }
}
