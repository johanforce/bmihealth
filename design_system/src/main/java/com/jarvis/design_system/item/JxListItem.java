package com.jarvis.design_system.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jarvis.design_system.R;
import com.jarvis.design_system.item.config.ListIteamEndTextColor;
import com.jarvis.design_system.item.config.ListItemContentElement;
import com.jarvis.design_system.item.config.ListItemEndButtonStyle;
import com.jarvis.design_system.item.config.ListItemEndElement;
import com.jarvis.design_system.item.config.ListItemEndToggelStyle;
import com.jarvis.design_system.item.config.ListItemStartElement;

public class JxListItem extends RelativeLayout {
    protected TypedArray attributeArray;
    protected int itemStartElement, itemContentElement, itemEndElement, itemEndButtonStyle, itemEndToggleStyle;
    protected Drawable itemStartIcon, itemEndIcon;
    protected String itemTitle, itemDescription, itemEndText, itemEndButtonText;
    protected int itemEndTextColor;
    private ListItemViewBinder listItemViewBinder;

    public JxListItem(Context context) {
        super(context);
        initView(context, null);
    }

    public JxListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public JxListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        try {
            this.attributeArray = context.obtainStyledAttributes(attrs, R.styleable.JxListItem);
            this.itemStartElement = this.attributeArray.getInt(R.styleable.JxListItem_itemStartElement, ListItemStartElement.HIDE.value);
            this.itemEndElement = this.attributeArray.getInt(R.styleable.JxListItem_itemEndElement, ListItemEndElement.HIDE.value);
            this.itemContentElement = this.attributeArray.getInt(R.styleable.JxListItem_itemContentElement, ListItemContentElement.ONLY_TITLE.value);
            this.itemEndButtonStyle = this.attributeArray.getInt(R.styleable.JxListItem_itemEndButtonStyle, ListItemEndButtonStyle.PRIMARY.value);
            this.itemEndTextColor = this.attributeArray.getColor(R.styleable.JxListItem_itemEndTextColor, getResources().getColor(ListIteamEndTextColor.BASE_INK_500.value));
            this.itemEndToggleStyle = this.attributeArray.getColor(R.styleable.JxListItem_iteamEndToggleStyle, ListItemEndToggelStyle.TOGGEL.value);

            this.itemTitle = this.attributeArray.getString(R.styleable.JxListItem_itemTitle);
            this.itemDescription = this.attributeArray.getString(R.styleable.JxListItem_itemDescription);
            this.itemEndText = this.attributeArray.getString(R.styleable.JxListItem_itemEndText);
            this.itemEndButtonText = this.attributeArray.getString(R.styleable.JxListItem_itemEndButtonText);
            this.itemStartIcon = this.attributeArray.getDrawable(R.styleable.JxListItem_itemStartIcon);
            this.itemEndIcon = this.attributeArray.getDrawable(R.styleable.JxListItem_itemEndIcon);

            this.listItemViewBinder = new ListItemViewBinder(context, this);
            this.inflateViewData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inflateViewData() {
        if (this.itemEndElement == ListItemEndElement.TOGGLE.value) {
            this.listItemViewBinder.bindViewToggle(itemTitle, itemStartElement, itemStartIcon, itemEndToggleStyle);
        } else if (this.itemEndElement == ListItemEndElement.BUTTON.value) {
            this.listItemViewBinder.bindViewButtonCTA(itemTitle, itemStartElement, itemStartIcon, itemEndButtonStyle, itemEndButtonText);
        } else if (this.itemContentElement == ListItemContentElement.TITLE_AND_DESCRIPTION_STYLE1.value) {
            this.listItemViewBinder.bindView2LineContentStyle1(itemTitle, itemDescription, itemStartElement, itemStartIcon, itemEndElement, itemEndText, itemEndIcon);
        } else if (this.itemContentElement == ListItemContentElement.TITLE_AND_DESCRIPTION_STYLE2.value) {
            this.listItemViewBinder.bindView2LineContentStyle2(itemTitle, itemDescription, itemStartElement, itemStartIcon, itemEndElement, itemEndText, itemEndIcon);
        } else if (this.itemContentElement == ListItemContentElement.TITLE_AND_DESCRIPTION_STYLE3.value) {
            this.listItemViewBinder.bindViewListItemIconValueStyle3(itemTitle, itemDescription, itemStartElement, itemStartIcon, itemEndElement, itemEndIcon);
        } else if (this.itemContentElement == ListItemContentElement.PROGRESS_AND_TITLE_AND_DESCRIPTION.value) {
            this.listItemViewBinder.bindViewProgress2LineContentStyle(itemTitle, itemDescription, itemEndElement, itemEndIcon, itemEndText);
        } else if (this.itemContentElement == ListItemContentElement.TITLE_AND_DESCRIPTION_END_WITH_TEXT_AND_ICON.value) {
            this.listItemViewBinder.bindView2LineEndWithTextAndIcon(itemTitle, itemDescription, itemEndElement, itemEndIcon, itemEndText);
        } else if (this.itemContentElement == ListItemContentElement.BOTTOMSHEET_DROPDOWN_DIALOG.value) {
            this.listItemViewBinder.bindViewBottomSheetDropdownDialog(itemTitle, itemStartElement, itemStartIcon, itemEndElement, itemEndIcon);
        } else {
            this.listItemViewBinder.bindViewIconValue(itemTitle, itemStartElement, itemStartIcon, itemEndElement, itemEndIcon, itemEndText, itemEndTextColor);
        }
    }

    public ListItemViewBinder getViewBinder() {
        return this.listItemViewBinder;
    }

    public Drawable getItemStartIcon() {
        return this.itemStartIcon;
    }
}
