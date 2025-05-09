package ru.imagebook.client.app.view.common;

import com.google.gwt.user.client.ui.Anchor;


public class PopoverAnchor extends Anchor {
    private String content;
    private String placement = "right";
    private String trigger = "focus";

    public PopoverAnchor(String text) {
        super(text);
        this.setTabIndex(0);
        this.getElement().setAttribute("role", "button");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Popover.popover(this.getElement(), placement, getTitle(), content, trigger);
    }
}
