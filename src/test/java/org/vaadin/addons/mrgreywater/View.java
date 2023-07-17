package org.vaadin.addons.mrgreywater;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;
import java.util.Objects;

@Route("")
public class View extends VerticalLayout {
    private static record Person(int i) {
    }

    private boolean dark = false;

    public View() {
        {
            // Placement
            var vLayout = new VerticalLayout();
            vLayout.setWidthFull();
            var placements = Tippy.Placement.values();
            for (int j = 0; j < placements.length / 3; j++) {
                var hLayout = new HorizontalLayout();
                hLayout.setWidthFull();
                vLayout.add(hLayout);
                for (int i = 0; i < 3; i++) {
                    var placement = placements[j * 3 + i];
                    var button = new Button(placement.getProp());
                    button.setWidth("200px");
                    var tippy = Tippy.createTooltipFor(button);
                    tippy.setText("Tooltip");
                    tippy.setPlacement(placement);
                    hLayout.add(button);
                }
            }
            add(vLayout);
        }
        {
            // Arrows
            var hlayout = new HorizontalLayout();
            hlayout.setWidthFull();
            var arrows = Tippy.Arrow.values();
            for (int i = 0; i < arrows.length; i++) {
                var arrow = arrows[i];
                var button = new Button(arrow.name().substring(0, 1) + arrow.name().substring(1).toLowerCase());
                button.setWidth("200px");
                var tippy = Tippy.createTooltipFor(button);
                tippy.setText("I'm a Tippy tooltip!");
                hlayout.add(button);
            }
            add(hlayout);
        }
        {
            var switchThemeVariantButton = new Button("Switch to dark theme");
            var tooltip = Tippy.createTooltipFor(switchThemeVariantButton);
            tooltip.setText("Switch to " + (dark ? "light" : "dark") + " theme.");
            switchThemeVariantButton.addClickListener(event -> {
                dark = !dark;
                var js = "document.documentElement.setAttribute('theme', $0)";
                getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
                switchThemeVariantButton.setText("Switch to " + (dark ? "light" : "dark") + " theme.");
                tooltip.setText("Switch to " + (dark ? "light" : "dark") + " theme.");
            });
            add(switchThemeVariantButton);
        }
        {
            var removeSelfButton = new Button("Removes this button");
            removeSelfButton.addClickListener(event -> {
                removeSelfButton.removeFromParent();
            });
            var tippy = Tippy.createTooltipFor(removeSelfButton);
            tippy.add(new Text("I am removing myself"));
            tippy.addDetachListener(event -> {
                Notification.show("Detached Button Tooltip");
            });
            add(removeSelfButton);
        }
        var v1 = new VerticalLayout();
        var v2 = new VerticalLayout();

        var button2 = new Button("Switch Layouts");
        button2.addClickListener(event -> {
            if (v1.getChildren().anyMatch(component -> Objects.equals(button2, component))) {
                v1.remove(button2);
                v2.add(button2);
            } else {
                v2.remove(button2);
                v1.add(button2);
            }
        });
        var tippy2 = Tippy.createTooltipFor(button2);
        tippy2.add(new Text("Text"));
        tippy2.addDetachListener(event -> {
            System.out.println("DETACH Button2");
        });
        v1.add(button2);
        add(new HorizontalLayout(v1, v2));

        Grid<Person> grid = new Grid<>();

        var person = new ArrayList<Person>();
        for (int i = 0; i < 1000; i++) {
            person.add(new Person(i));
        }

        grid.setItems(person);
        grid.addColumn(LitRenderer
                .<Person>of("<label><tippyjs-for-vaadin><b>${item.name}</b></tippyjs-for-vaadin><b>${item.name}</b></label>")
                .withProperty("name", person1 -> String.valueOf(person1.i))
        ).setHeader("LitRenderer");

        grid.addColumn(new ComponentRenderer<>(item -> {
            var o = new NativeLabel(String.valueOf(item.i));
            var tooltip = Tippy.createTooltipFor(o);
            tooltip.setContent(String.valueOf(item.i));
            return o;
        })).setHeader("ComponentRenderer");

        add(grid);
    }
}
