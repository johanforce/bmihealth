package com.jarvis.design_system.item.config;

public enum ListItemEndElement {
    HIDE(0),
    ICON(1),
    TEXT_VALUE_STYLE1(2),
    TEXT_VALUE_STYLE2(3),
    TEXT_AND_ICON(4),
    TOGGLE(5),
    BUTTON(6),
    INVISIBLE(7);

    public int value;

    ListItemEndElement(int value) {
        this.value = value;
    }
}
