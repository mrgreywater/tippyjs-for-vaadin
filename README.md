Tippy.js wrapper for Vaadin 24+

It allows to create Tooltips with Java using the Component or LitRenderer API.

In the future the tooltips are supposed to allow dynamic tooltips that are client side only (javascript) or client/server (java).

Java:
```java
var tooltip = Tippy.createTooltipFor(component);
tooltip.add(new HorizontalLayout(new Text("Image"), new Image(...)))
```

For Grid:
```java
grid.addColumn(LitRenderer
        .of("<label><tippyjs-for-vaadin>${item.tooltip}</tippyjs-for-vaadin>${item.tooltip}</label>")
        .withProperty("tooltip", o -> "Tooltip")
);
```

