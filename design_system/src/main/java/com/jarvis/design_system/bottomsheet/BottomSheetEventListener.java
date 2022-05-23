package com.jarvis.design_system.bottomsheet;

import com.jarvis.design_system.item.JxListItem;

public abstract class BottomSheetEventListener {
    public void onItemClick(int position) {}

    public void onItemClick(int position, JxListItem itemView) {}

    public void onButtonPositiveClick() {}

    public void onButtonPositiveClick(int selectedItem) {}

    public void onButtonNegativeClick() {}

    public void onCloseClick() {}

    public void onCancel() {}

    public void onDismiss() {}
}
