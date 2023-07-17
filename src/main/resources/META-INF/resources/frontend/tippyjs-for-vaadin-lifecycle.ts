// @ts-ignore
import {customElement} from 'lit/decorators.js';

/*
Keeps track of the lifecycle of a parent component.
 */
@customElement('tippyjs-for-vaadin-lifecycle')
export class TippyJsForVaadinLifecyle extends HTMLElement {
    public onAttach: any;
    public onDetach: any;
    public attachedTo: HTMLElement;
    private hovering: boolean = false;

    constructor(parentElement: HTMLElement) {
        super();
        this.attachedTo = parentElement;
        parentElement.appendChild(this);
        parentElement.addEventListener('mouseenter', () => {
            this.hovering = true;
        });
        parentElement.addEventListener('mouseleave', () => {
            this.hovering = false;
        });
    }

    connectedCallback(): void {
        if (this.onAttach) this.onAttach();
    }

    disconnectedCallback(): void {
        if (this.attachedTo.isConnected) {
            setTimeout(() => {
                if (this.parentElement != this.attachedTo) {
                    console.log("vtip: reconnecting")
                    this.attachedTo.appendChild(this);
                }
            }, 0)
        } else {
            if (this.onDetach) {
                this.onDetach();
            }
        }

    }

    isHovering() {
        return this.hovering;
    }
}
