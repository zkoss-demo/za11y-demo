package zk.demo.a11y;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.theme.Themes;
import zk.demo.a11y.repository.OrderRepo;

import java.util.Optional;

public class MainComposer extends SelectorComposer {
    private static final String NAV_ID = "NAV_ID";

    @Wire
    private Selectbox themeSelection;
    @Wire
    private Navbar sideNav;
    @Wire
    private Center content;

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        // usually created by an IOC container
        Session session = Sessions.getCurrent();
        if(!session.hasAttribute("orderRepo")) {
            session.setAttribute("orderRepo", new OrderRepo(100, 5));
        }
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initThemeSelection(themeSelection);
        initSideNav(sideNav);
//        navigateTo("overview");
        navigateTo("orders");
    }

    @Listen("onSelect=#sideNav")
    public void onNavigate(Event navEvent) {
        Navitem navitem = ((Navbar) navEvent.getTarget()).getSelectedItem();
        navigateTo((String) navitem.getAttribute(NAV_ID));
    }

    private void navigateTo(String navId) {
        content.getChildren().clear();
        findNavitemById(sideNav, navId).ifPresent(item -> item.setSelected(true));
        Executions.createComponents("/WEB-INF/pages/" + navId + ".zul", content, null);
    }

    private void initSideNav(Navbar sideNav) {
        sideNav.appendChild(createNavitem("overview", "Overview", "z-icon-eye"));
        sideNav.appendChild(createNavitem("orders", "Orders", "z-icon-list-alt"));
        sideNav.appendChild(createNavitem("feedback", "Feedback", "z-icon-thumbs-up"));
        sideNav.appendChild(createNavitem("statistics", "Statistics", "z-icon-bar-chart"));
    }

    private Navitem createNavitem(String navId, String label, String iconSclass) {
        Navitem navitem = new Navitem();
        navitem.setAttribute(NAV_ID, navId);
        navitem.setLabel(label);
        navitem.setIconSclass(iconSclass);
        return navitem;
    }

    private Optional<Navitem> findNavitemById(Navbar sideNav, String navId) {
        return sideNav.getChildren().stream()
                .filter(Navitem.class::isInstance)
                .map(Navitem.class::cast)
                .filter(item -> navId.equals(item.getAttribute(NAV_ID)))
                .findFirst();
    }

    private void initThemeSelection(Selectbox themeSelection) {
        ListModelList<String> themesModel = new ListModelList<>(Themes.getThemes());
        themesModel.sort(String::compareToIgnoreCase);
        themesModel.addToSelection(Themes.getCurrentTheme());
        themeSelection.setModel(themesModel);
        themeSelection.setItemRenderer((owner, data, index) ->
                Themes.getDisplayName((String) data));
        themeSelection.addEventListener(Events.ON_SELECT, e -> {
            Selectable<String> model = (Selectable<String>) themeSelection.getModel();
            Themes.setTheme(Executions.getCurrent(), model.getSelection().iterator().next());
            Executions.sendRedirect("");
        });
    }
}
