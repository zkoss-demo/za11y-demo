package zk.demo.a11y;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.theme.Themes;
import zk.demo.a11y.repository.OrderRepo;

import java.util.Optional;

public class MainComposer extends SelectorComposer {
    private static final String NAV_ID = "NAV_ID";

    @Wire
    private Selectbox themeSelection;
    @Wire
    private A menuToggle;
    @Wire
    private North bannerNorth;
    @Wire
    private West sideNavWest;
    @Wire
    private Div bannerNavContainer;
    @Wire
    private Navbar mainNav;
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
        initMainNav(mainNav);
        navigateTo("overview");
        menuToggle.setClientAttribute("aria-controls", bannerNavContainer.getUuid());
    }

    @Listen("onClientInfo=#main")
    public void handleClientInfo(ClientInfoEvent clientInfoEvent) {
        boolean largeView = clientInfoEvent.getDesktopWidth() >= 720;
        mainNav.setParent(largeView ? sideNavWest : bannerNavContainer);
        if(largeView) {
            sideNavWest.setVisible(true);
            menuToggle.setVisible(false);
            toggleBannerNavContainer(false);
        } else {
            sideNavWest.setVisible(false);
            if(!menuToggle.isVisible()) {
                menuToggle.setVisible(true);
                toggleBannerNavContainer(false);
            }
        }
    }

    @Listen("onClick=#bannerNorth #menuToggle")
    public void toggleMenu() {
        toggleBannerNavContainer(!bannerNavContainer.isVisible());
    }

    private void toggleBannerNavContainer(boolean open) {
        bannerNavContainer.setVisible(open);
        menuToggle.setClientAttribute("aria-expanded", String.valueOf(open));
        menuToggle.invalidate();
        mainNav.focus(); //no effect due to issue https://tracker.zkoss.org/browse/ZK-4753
    }

    @Listen("onSelect=#mainNav")
    public void onSelectNavItem(Event navEvent) {
        Navitem navitem = ((Navbar) navEvent.getTarget()).getSelectedItem();
        navigateTo((String) navitem.getAttribute(NAV_ID));
        toggleBannerNavContainer(false);
        if(menuToggle.isVisible()) {
            menuToggle.focus();
        }
    }

    @Listen("onNavigate=#main")
    public void onNavigate(Event navEvent) {
        navigateTo((String) (String) navEvent.getData());
    }

    private void navigateTo(String navId) {
        content.getChildren().clear();
        findNavitemById(mainNav, navId).ifPresent(item -> item.setSelected(true));
        Executions.createComponents("/WEB-INF/pages/" + navId + ".zul", content, null);
    }

    private void initMainNav(Navbar mainNav) {
        mainNav.appendChild(createNavitem("overview", "Overview", "z-icon-eye"));
        mainNav.appendChild(createNavitem("orders", "Orders", "z-icon-list-alt"));
        mainNav.appendChild(createNavitem("feedback", "Feedback", "z-icon-thumbs-up"));
        mainNav.appendChild(createNavitem("statistics", "Statistics", "z-icon-bar-chart"));
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
