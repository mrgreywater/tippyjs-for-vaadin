package org.vaadin.addons.mrgreywater;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;


@Tag("tippyjs-for-vaadin")
@JsModule("./tippyjs-for-vaadin.ts")
@JsModule("./tippyjs-for-vaadin-lifecycle.ts")
@CssImport(value = "./tippyjs-for-vaadin.css")
@NpmPackage(value = "tippy.js", version = "6.3.7")
@CssImport("tippy.js/dist/tippy.css")
@CssImport("tippy.js/dist/svg-arrow.css")
public class Tippy extends Component implements HasSize, HasComponents {
    private static final String CDN_URL = "https://cdnjs.cloudflare.com/ajax/libs/tippy.js/6.3.7";

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

    enum Animation {
        SHIFT_AWAY, SHIFT_AWAY_SUBTLE, SHIFT_AWAY_EXTREME, SHIFT_TOWARD, SHIFT_TOWARD_SUBTLE, SHIFT_TOWARD_EXTREME, SCALE, SCALE_SUBTLE, SCALE_EXTREME, PERSPECTIVE, PERSPECTIVE_SUBTLE, PERSPECTIVE_EXTREME;
        private final String prop;

        Animation() {
            prop = this.name().toLowerCase().replace('_', '-');
        }

        public static Animation ofProp(Object prop) {
            if (prop == null) {
                return null;
            }
            return Arrays.stream(Animation.values()).filter(placement -> placement.getProp().equals(prop)).findFirst().orElse(null);
        }

        public String getProp() {
            return prop;
        }
    }

    enum Theme {
        LUMO, LIGHT, LIGHT_BORDER, MATERIAL, TRANSLUCENT;
        private final String prop;

        Theme() {
            prop = this.name().toLowerCase().replace('_', '-');
        }

        public static Theme ofProp(Object prop) {
            if (prop == null) {
                return null;
            }
            return Arrays.stream(Theme.values()).filter(placement -> placement.getProp().equals(prop)).findFirst().orElse(null);
        }

        public String getProp() {
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

    public void loadCss(String cssUrl) {
        TippyDynamicCssLoader.loadCss(CDN_URL + cssUrl);
    }

    public static void setDefaultConfig(TippyConfig config) {
        new Tippy().getElement().executeJs("this.setDefaultConfig($0);", config.toJson());
    }

    public void setConfig(TippyConfig config) {
        getElement().executeJs("this.setConfig($0);", config.toJson());
    }

    public void setPlacement(Placement placement) {
        getElement().setProperty("placement", placement.getProp());
    }

    public Placement getPlacement() {
        return Placement.ofProp(getElement().getProperty("placement", "top"));
    }

    public void setRoundArrow(boolean roundArrow) {
        if (roundArrow) {
            getElement().executeJs("this.setArrow(this.getRoundArrow());");
        } else {
            setArrow(true);
        }
    }

    public void setArrow(Svg svg) {
        getElement().executeJs("this.setArrow($0);", svg.getElement());
    }

    public void setArrow(String arrow) {
        getElement().executeJs("this.setArrow($0);", arrow);
    }

    public void setArrow(boolean value) {
        getElement().executeJs("this.setArrow($0);", value);
    }

    public void setAnimation(Animation animation) {
        loadCss("/animations/" + animation.getProp() + ".css");
        getElement().setProperty("animation", animation.getProp());
    }

    public void setAnimation(String animation) {
        getElement().setProperty("animation", animation);
    }

    public String getAnimationString() {
        return getElement().getProperty("animation", "fade");
    }

    public Animation getAnimation() {
        return Animation.ofProp(getAnimationString());
    }

    public void setAnimateFill(boolean animateFill) {
        if (animateFill) {
            loadCss("/backdrop.css");
            loadCss("/animations/shift-away.css");
        }
        getElement().setProperty("enableAnimateFill", animateFill);
    }

    public void getAnimateFill() {
        getElement().getProperty("enableAnimateFill", false);
    }

    public void setInertia(boolean inertia) {
        getElement().setProperty("inertia", inertia);
    }

    public boolean getInertia() {
        return getElement().getProperty("inertia", false);
    }

    public void setTheme(Theme theme) {
        setTheme(theme.getProp());
    }

    public void setTheme(String theme) {
        if (!Objects.equals(theme, "lumo") && Arrays.stream(Theme.values()).map(Theme::getProp).anyMatch(themeName -> themeName.equals(theme))) {
            loadCss("/themes/" + theme + ".css");
        }
        getElement().setProperty("theme", theme);
    }

    public Theme getTheme() {
        return Theme.ofProp(getThemeString());
    }

    public String getThemeString() {
        return getElement().getProperty("theme", "lumo");
    }

}
