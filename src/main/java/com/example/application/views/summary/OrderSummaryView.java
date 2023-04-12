package com.example.application.views.summary;

import com.example.application.data.dto.EventDto;
import com.example.application.data.entity.DietAttributes;
import com.example.application.data.service.EventService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;


@PageTitle("Order summary")
@Route(value = "summary", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class OrderSummaryView extends VerticalLayout {

    private final EventService eventService;

    public OrderSummaryView(EventService eventService) {
        this.eventService = eventService;

        List<EventDto> eventDtos = eventService.getUserEvents();

        eventDtos.forEach(event -> {
            Details eventView = createEventView(event);
            add(eventView);
        });
    }

    private Details createEventView(EventDto event) {
        Details details = new Details();
        details.setSummary(createSummary(event));
        details.addContent(createCharts());
        return details;
    }

    private VerticalLayout createSummary(EventDto event) {
        VerticalLayout detailSummaryLayout = new VerticalLayout();
        detailSummaryLayout.setPadding(false);

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setAlignItems(FlexComponent.Alignment.END);
        nameLayout.add(new H4(event.getName()));
        nameLayout.add(new Span(event.getDeadLine().toString()));

        detailSummaryLayout.add(nameLayout);
        detailSummaryLayout.add(new Span(event.getDescription()));

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.addClassName("event-content");
        contentLayout.setPadding(true);

        return detailSummaryLayout;
    }

    private HorizontalLayout createCharts() {

        Chart chartColumn = new Chart();
        chartColumn.setWidth(50, Unit.PERCENTAGE);
        chartColumn.setHeight(250, Unit.PIXELS);
        chartColumn.getConfiguration().setTitle("Menu choices");
        chartColumn.getConfiguration().getChart().setType(ChartType.COLUMN);
        chartColumn.getConfiguration().getxAxis().setType(AxisType.CATEGORY);
        chartColumn.getConfiguration().getyAxis().setTitle("Number of orders");
        chartColumn.getConfiguration().getLegend().setEnabled(false);
        chartColumn.getConfiguration().getTooltip().setPointFormat("{point.y} orders");

        ListSeries series = new ListSeries("Choices");
        series.setData(5, 3);
        chartColumn.getConfiguration().setSeries(series);
        chartColumn.getConfiguration().getxAxis().setCategories("Burger", "Pizza");

        Chart chartPie = new Chart();
        chartPie.setWidth(50, Unit.PERCENTAGE);
        chartPie.setHeight(250, Unit.PIXELS);
        chartPie.getConfiguration().setTitle("Allergies");
        chartPie.getConfiguration().getChart().setType(ChartType.PIE);
        chartPie.getConfiguration().getxAxis().setType(AxisType.CATEGORY);
        chartPie.getConfiguration().getyAxis().setTitle("Number of orders");
        chartPie.getConfiguration().getLegend().setEnabled(false);
        chartPie.getConfiguration().getTooltip().setPointFormat("{point.y} orders");

        chartPie.getConfiguration().getxAxis().setType(AxisType.CATEGORY);
        chartPie.getConfiguration().getyAxis().setTitle("Number of orders");
        chartPie.getConfiguration().getLegend().setEnabled(false);
        chartPie.getConfiguration().getTooltip().setPointFormat("{point.y} orders");

        DataSeries seriesPie = new DataSeries();
        seriesPie.add(new DataSeriesItem(DietAttributes.LACTOSE_FREE.getName(), 5));
        seriesPie.add(new DataSeriesItem(DietAttributes.GLUTEN_FREE.getName(), 3));
        chartPie.getConfiguration().setSeries(seriesPie);
//        chartPie.getConfiguration().getxAxis().setCategories(Arrays.stream(DietAttributes.values()).map(DietAttributes::toString).toArray(String[]::new));

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeightFull();
        layout.add(chartColumn, chartPie);

        return layout;
    }

}
