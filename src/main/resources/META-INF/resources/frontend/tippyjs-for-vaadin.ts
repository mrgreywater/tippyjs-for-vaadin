import {html, LitElement, PropertyValueMap} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import tippy from 'tippy.js';
import {TippyJsForVaadinLifecyle} from "./tippyjs-for-vaadin-lifecycle";

/** Custom elemnt displaying current time clock.
 *
 * Element impleme
 */
@customElement('tippyjs-for-vaadin')
export class TippyjsForVaadin extends LitElement {
    @property({type: Object}) tippy = null;

    @property({type: String}) placement = "top";

    @property() arrow = true;

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
            this.tippy = tippy(parentElement, {
                theme: 'lumo',
                allowHTML: true,
                content: this,
                placement: this.placement,
                arrow: true,
            });
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

    shouldUpdate(changedProperties: PropertyValueMap<any>) {
        if (changedProperties.has("placement")) {
            this.tippy.setProps({
                placement: this.placement
            })
        }
        if (changedProperties.has("arrow")) {
            this.tippy.setProps({
                arrow: this.arrow
            })
        }
        return super.shouldUpdate(changedProperties);
    }
}
