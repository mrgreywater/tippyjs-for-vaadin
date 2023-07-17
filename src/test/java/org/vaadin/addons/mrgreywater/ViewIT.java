package org.vaadin.addons.mrgreywater;

import com.vaadin.testbench.TestBenchElement;
import org.junit.Assert;
import org.junit.Test;

public class ViewIT extends AbstractViewTest {

    @Test
    public void componentWorks() {
        final TestBenchElement tippyjs = $("tippyjs-for-vaadin").waitForFirst();
        // Check that axa-text contains at least one other element, which means that
        // is has been upgraded to a custom element and not just rendered as an empty
        // tag
        Assert.assertTrue(
                tippyjs.$(TestBenchElement.class).all().size() > 0);
    }
}
