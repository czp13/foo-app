package com.example.application.views;

import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.eventlist.EventMenuSelectionListView;
import com.example.application.views.events.CreateEventView;
import com.example.application.views.events.EventsView;
import com.example.application.views.helloworld2.HelloWorld2View;
import com.example.application.views.list.ListView;
import com.example.application.views.menu.MealView;
import com.example.application.views.menu.MenuView;
import com.example.application.views.summary.OrderSummaryView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.example.application.views.participant.ParticipantView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final HorizontalLayout headerItemLayout = new HorizontalLayout();
    protected H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        headerItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        headerItemLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerItemLayout.setWidth(100, Unit.PERCENTAGE);

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        headerItemLayout.add(viewTitle);
        addToNavbar(true, toggle, headerItemLayout);
    }

    private void addDrawerContent() {
        H1 appName = new H1("foo-app");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(ListView.class)) {
            nav.addItem(new AppNavItem("List", ListView.class, LineAwesomeIcon.TH_SOLID.create()));

        }
        if (accessChecker.hasAccess(CreateEventView.class)) {
            nav.addItem(new AppNavItem("Events", EventsView.class, LineAwesomeIcon.GLOBE_SOLID.create()));

        }
        if (accessChecker.hasAccess(HelloWorld2View.class)) {
            nav.addItem(new AppNavItem("Hello World2", HelloWorld2View.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        }
        if (accessChecker.hasAccess(MenuView.class)) {
            nav.addItem(new AppNavItem("Menu", MenuView.class, LineAwesomeIcon.LIST_SOLID.create()));
        }
        if (accessChecker.hasAccess(MealView.class)) {
            nav.addItem(new AppNavItem("Meal", MealView.class, LineAwesomeIcon.LIST_SOLID.create()));
        }
        if (accessChecker.hasAccess(ParticipantView.class)) {
            nav.addItem(new AppNavItem("Food choice", ParticipantView.class, LineAwesomeIcon.PIZZA_SLICE_SOLID.create()));
        }
        if (accessChecker.hasAccess(EventMenuSelectionListView.class)) {
            nav.addItem(new AppNavItem("Menu Selection", EventMenuSelectionListView.class, LineAwesomeIcon.PIZZA_SLICE_SOLID.create()));
        }
        if (accessChecker.hasAccess(OrderSummaryView.class)){
            nav.addItem(new AppNavItem("Order Summary", OrderSummaryView.class, LineAwesomeIcon.CHART_BAR.create()));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    protected void addItemToRightSlotOfNavbar(Component component){
        headerItemLayout.add(component);
    }
}
