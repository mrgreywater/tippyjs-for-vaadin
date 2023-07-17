import {html, LitElement, PropertyValueMap} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import tippy, {animateFill, roundArrow} from 'tippy.js';
import {TippyJsForVaadinLifecyle} from "./tippyjs-for-vaadin-lifecycle";

/** Custom elemnt displaying current time clock.
 *
 * Element impleme
 */
@customElement('tippyjs-for-vaadin')
export class TippyjsForVaadin extends LitElement {
    @property({type: Object}) tippy = null;

    @property({type: String}) placement = "top";

    @property({type: String}) animation = "fade";

    @property({type: String}) theme = "lumo";

    @property() arrow: SVGElement | String | boolean = true;

    @property({type: Boolean}) enableAnimateFill = false;
    @property({type: Boolean}) inertia = false;

    @property({type: Object}) lifecycle: TippyJsForVaadinLifecyle = null;

    connectedCallback(): void {
        super.connectedCallback();

        if (this.lifecycle) return;

        this.lifecycle = new TippyJsForVaadinLifecyle(this.parentElement);
        console.log("vtip: created", this.lifecycle.parentElement);

        requestAnimationFrame(() => {
            this._createTippy(this.lifecycle.parentElement);
            this.lifecycle.onAttach = () => {
                this._createTippy(this.lifecycle.parentElement);
            }

            this.lifecycle.onDetach = () => {
                console.log("vtip: detached lifecycle", this.lifecycle.parentElement);
                this._destroyTippy();
            }
        });
    }

    _destroyTippy() {
        if (this.tippy) {
            this.tippy.destroy();
            this.tippy = null;
        }
    }

    destroy() {
        console.log("vtip: destroy", this);
        this._destroyTippy();
        if (this.lifecycle && this.lifecycle.parentElement)
            this.lifecycle.parentElement.removeChild(this.lifecycle);
    }

    _createTippy(parentElement: HTMLElement) {
        if (!this.tippy) {
            let props = {
                plugins: [],
                content: this,
                theme: this.theme,
                allowHTML: true,
                animation: this.animation,
                placement: this.placement,
                inertia: this.inertia,
                arrow: this.arrow,
            };
            if (this.enableAnimateFill) {
                props.plugins.push(animateFill);
                props["animateFill"] = true;
            }
            this.tippy = tippy(parentElement, props);
        } else {
            this.tippy.setContent(null);
            this.tippy.setContent(this);
        }
        if (this.lifecycle.isHovering()) {
            this.tippy.show();
        }
    }

    render() {
        return html`<slot></slot>`;
    }

    getRoundArrow() {
        return roundArrow;
    }

    setArrow(arrow: SVGElement | String | boolean) {
        console.log("vtip: setting arrow", arrow);
        this.arrow = arrow;
        if (this.tippy) {
            this.tippy.setProps({
                arrow: this.arrow,
            })
        }
    }

    shouldUpdate(changedProperties: PropertyValueMap<any>) {
        if (this.tippy) {
            if (changedProperties.has("placement")) {
                this.tippy.setProps({
                    placement: this.placement
                })
            }
            if (changedProperties.has("animation")) {
                this.tippy.setProps({
                    animation: this.animation
                })
            }
            if (changedProperties.has("arrow")) {
                this.tippy.setProps({
                    arrow: this.arrow
                })
            }
            if (changedProperties.has("enableAnimateFill")) {
                this.tippy.setProps({
                    plugins: [animateFill],
                    animateFill: this.enableAnimateFill,
                })
            }
            if (changedProperties.has("theme")) {
                this.tippy.setProps({
                    theme: this.theme,
                })
            }
            if (changedProperties.has("inertia")) {
                this.tippy.setProps({
                    inertia: this.inertia,
                })
            }
        }
        return super.shouldUpdate(changedProperties);
    }
}
