package org.vaadin.addons.mrgreywater;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;

import java.util.HashSet;
import java.util.Set;

public class TippyDynamicCssLoader {
    Set<String> loadedUrls = new HashSet<>();
    private TippyDynamicCssLoader() { }
    private static TippyDynamicCssLoader getInstance() {
        var instance = ComponentUtil.getData(UI.getCurrent(), TippyDynamicCssLoader.class);
        if (instance == null) {
            instance = new TippyDynamicCssLoader();
            ComponentUtil.setData(UI.getCurrent(), TippyDynamicCssLoader.class, instance);
        }
        return instance;
    }
    public synchronized static void loadCss(String cssUrl) {
        var instance = getInstance();
        if (instance.loadedUrls.contains(cssUrl)) {
            return;
        }
        UI.getCurrent().getPage().addStyleSheet(cssUrl);
        instance.loadedUrls.add(cssUrl);
    }
}
