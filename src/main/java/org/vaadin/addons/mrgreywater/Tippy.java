package org.vaadin.addons.mrgreywater;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

import java.lang.ref.WeakReference;
import java.util.Arrays;


@Tag("tippyjs-for-vaadin")
@JsModule("./tippyjs-for-vaadin.ts")
@JsModule("./tippyjs-for-vaadin-lifecycle.ts")
@CssImport(value = "./tippyjs-for-vaadin.css")
@NpmPackage(value = "tippy.js", version = "6.3.7")
@CssImport("tippy.js/themes/light.css")
@CssImport("tippy.js/themes/light-border.css")
@CssImport("tippy.js/themes/material.css")
@CssImport("tippy.js/themes/translucent.css")
@CssImport("tippy.js/dist/tippy.css")
@CssImport("tippy.js/dist/backdrop.css")
@CssImport("tippy.js/animations/shift-away.css")
@CssImport("tippy.js/dist/svg-arrow.css")
public class Tippy extends Component implements HasSize, HasComponents {
    enum Placement {
        TOP, TOP_START, TOP_END, RIGHT, RIGHT_START, RIGHT_END, BOTTOM, BOTTOM_START, BOTTOM_END, LEFT, LEFT_START, LEFT_END;
        private final String prop;

        Placement() {
            prop = this.name().toLowerCase().replace('_', '-');
        }

        public static Placement ofProp(Object prop) {
            if (prop == null) {
                return null;
            }
            return Arrays.stream(Placement.values()).filter(placement -> placement.getProp().equals(prop)).findFirst().orElse(null);
        }

        public String getProp() {
            return prop;
        }
    }

    enum Arrow {
        DEFAULT(true), ROUND("Round");
        private final Object prop;

        Arrow(Object prop) {
            this.prop = prop;
        }

        public static Arrow ofProp(Object prop) {
            if (prop == null) {
                return null;
            }
            return Arrays.stream(Arrow.values()).filter(placement -> placement.getProp().equals(prop)).findFirst().orElse(null);
        }

        public Object getProp() {
            return prop;
        }
    }

    private WeakReference<Component> extended;
    private Registration attachRegistration = null;

    public Tippy() {
        super();
    }

    protected void attach(Component component) {
        if (component.isAttached()) {
            attachTo(component);
        } else {
            attachRegistration = component.addAttachListener(event -> {
                attachTo(component);
            });
        }
    }

    private void attachTo(Component component) {
        if (component.getElement().isTextNode()) {
            throw new IllegalArgumentException("Attaching to Text node not supported because it doesn't represent an HTML Element. Use a Div or NativeLabel instead.");
        }
        extended = new WeakReference<>(component);
        component.getElement().appendChild(getElement());
    }

    public void destroy() {
        getElement().executeJs("this.destroy();");
        if (attachRegistration != null) {
            attachRegistration.remove();
            attachRegistration = null;
        }
        if (extended != null) {
            getElement().removeFromParent();
            extended.clear();
        }
    }

    public static Tippy createTooltipFor(Component component) {
        var tooltip = new Tippy();
        tooltip.attach(component);
        return tooltip;
    }

    public void setText(String text) {
        setContent(text);
    }

    public void setContent(String content) {
        removeAll();
        add(new Text(content));
    }

    public void setContent(Component content) {
        removeAll();
        add(content);
    }

    public void setPlacement(Placement placement) {
        getElement().setProperty("placement", placement.getProp());
    }

    public Placement getPlacement() {
        return Placement.ofProp(getElement().getProperty("placement", "top"));
    }

}
