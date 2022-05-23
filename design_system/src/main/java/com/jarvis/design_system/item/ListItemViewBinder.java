package com.jarvis.design_system.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvis.design_system.button.JxButton;
import com.jarvis.design_system.button.JxCheckBoxButton;
import com.jarvis.design_system.button.JxToggleButton;
import com.jarvis.design_system.databinding.ListItem2linecontentEndTextIconBinding;
import com.jarvis.design_system.databinding.ListItem2linecontentStyle1Binding;
import com.jarvis.design_system.databinding.ListItem2linecontentStyle2Binding;
import com.jarvis.design_system.databinding.ListItemBottomsheetDropdownDialogBinding;
import com.jarvis.design_system.databinding.ListItemButtonctaBinding;
import com.jarvis.design_system.databinding.ListItemIconValueBinding;
import com.jarvis.design_system.databinding.ListItemIconValueStyle3Binding;
import com.jarvis.design_system.databinding.ListItemProgress2linecontentStyleBinding;
import com.jarvis.design_system.databinding.ListItemToggleBinding;
import com.jarvis.design_system.item.config.ListItemEndButtonStyle;
import com.jarvis.design_system.item.config.ListItemEndElement;
import com.jarvis.design_system.item.config.ListItemEndToggelStyle;
import com.jarvis.design_system.item.config.ListItemStartElement;

public class ListItemViewBinder implements IListItem {
    private final Context context;
    private final ViewGroup parentView;
    private final LayoutInflater inflater;
    private final ListItemViewWrapper itemViewWrapper;
    private ListItemIconValueBinding bindingIconValue;
    private ListItemButtonctaBinding bindingButtonCta;
    private ListItem2linecontentStyle1Binding binding2LineContentStyle1;
    private ListItem2linecontentStyle2Binding binding2LineContentStyle2;
    private ListItemProgress2linecontentStyleBinding bindingProgress2LineContentStyle;
    private ListItem2linecontentEndTextIconBinding binding2LineEndWithTextAndIcon;
    private ListItemIconValueStyle3Binding listItemIconValueStyle3Binding;
    private ListItemBottomsheetDropdownDialogBinding listItemBottomsheetDropdownDialogBinding;

    public ListItemViewBinder(Context context, ViewGroup parentView) {
        this.context = context;
        this.parentView = parentView;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemViewWrapper = new ListItemViewWrapper();
    }

    public ListItemIconValueBinding bindViewIconValue(String title, int startIconElement, Drawable startIcon,
                                                      int endItemElement, Drawable endIcon, String endText, int endTextColor) {
        this.bindingIconValue = ListItemIconValueBinding.inflate(LayoutInflater.from(context), parentView, true);
        this.bindingIconValue.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.bindingIconValue.tvTitle);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.bindingIconValue.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.bindingIconValue.ivStartIcon);
        }

        if (endItemElement == ListItemEndElement.TEXT_VALUE_STYLE1.value) {
            this.bindingIconValue.tvValue.setText(endText);
            this.bindingIconValue.tvValue.setTextColor(endTextColor);
            this.itemViewWrapper.wrapTvValueStyle1(this.bindingIconValue.tvValue);
        } else if (endItemElement == ListItemEndElement.TEXT_AND_ICON.value) {
            this.bindingIconValue.tvValue.setText(endText);
            this.itemViewWrapper.wrapTvValueStyle1(this.bindingIconValue.tvValue);
            this.itemViewWrapper.wrapIvValueIcon(this.bindingIconValue.ivValueIcon);
        } else if (endItemElement == ListItemEndElement.ICON.value) {
            this.bindingIconValue.ivEndIcon.setImageDrawable(endIcon);
            this.itemViewWrapper.wrapIvEndIcon(this.bindingIconValue.ivEndIcon);
        }

        return this.bindingIconValue;
    }

    public ListItemButtonctaBinding bindViewButtonCTA(String title, int startIconElement, Drawable startIcon, int buttonStyle, String buttonText) {
        this.bindingButtonCta = ListItemButtonctaBinding.inflate(inflater, parentView, true);
        this.bindingButtonCta.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.bindingButtonCta.tvTitle);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.bindingButtonCta.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.bindingButtonCta.ivStartIcon);
        }

        if (buttonStyle == ListItemEndButtonStyle.PRIMARY.value) {
            this.bindingButtonCta.btnPrimary.setText(buttonText);
            this.itemViewWrapper.wrapButtonCta(this.bindingButtonCta.btnPrimary);
        } else if (buttonStyle == ListItemEndButtonStyle.SECONDARY.value) {
            this.bindingButtonCta.btnSecondary.setText(buttonText);
            this.itemViewWrapper.wrapButtonCta(this.bindingButtonCta.btnSecondary);
        } else {
            this.bindingButtonCta.btnPlain.setText(buttonText);
            this.itemViewWrapper.wrapButtonCta(this.bindingButtonCta.btnPlain);
        }
        this.itemViewWrapper.wrapIvEndIcon(this.bindingButtonCta.ivEndIcon);
        return this.bindingButtonCta;
    }

    public ListItemToggleBinding bindViewToggle(String title, int startIconElement, Drawable startIcon, int itemEndToggleStyle) {
        ListItemToggleBinding bindingToggle = ListItemToggleBinding.inflate(inflater, parentView, true);
        bindingToggle.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(bindingToggle.tvTitle);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            bindingToggle.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(bindingToggle.ivStartIcon);
        }

        if (itemEndToggleStyle == ListItemEndToggelStyle.TOGGEL.value) {
            this.itemViewWrapper.wrapButtonToggle(bindingToggle.btnToggle);
        } else {
            this.itemViewWrapper.wrapButtonCheckbox(bindingToggle.btnCheckbox);
        }
        return bindingToggle;
    }

    public ListItem2linecontentStyle1Binding bindView2LineContentStyle1(String title, String description, int startIconElement, Drawable startIcon, int endElement, String endItemText, Drawable endIcon) {
        this.binding2LineContentStyle1 = ListItem2linecontentStyle1Binding.inflate(inflater, parentView, true);
        this.binding2LineContentStyle1.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.binding2LineContentStyle1.tvTitle);

        this.binding2LineContentStyle1.tvDescription.setText(description);
        this.itemViewWrapper.wrapTvDescription(this.binding2LineContentStyle1.tvDescription);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.binding2LineContentStyle1.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.binding2LineContentStyle1.ivStartIcon);
        }

        if (endElement == ListItemEndElement.TEXT_VALUE_STYLE1.value) {
            this.binding2LineContentStyle1.tvValue.setText(endItemText);
            this.itemViewWrapper.wrapTvValueStyle1(this.binding2LineContentStyle1.tvValue);
        } else if (endElement == ListItemEndElement.ICON.value) {
            this.binding2LineContentStyle1.ivEndIcon.setImageDrawable(endIcon);
            this.itemViewWrapper.wrapIvEndIcon(this.binding2LineContentStyle1.ivEndIcon);
        }

        return this.binding2LineContentStyle1;
    }

    public void bindView2LineContentStyle2(
            String title, String description, int startIconElement, Drawable startIcon, int endElement,
            String endItemText, Drawable endIconDraw) {
        this.binding2LineContentStyle2 = ListItem2linecontentStyle2Binding.inflate(inflater, parentView, true);
        this.binding2LineContentStyle2.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.binding2LineContentStyle2.tvTitle);

        this.binding2LineContentStyle2.tvDescription.setText(description);
        this.itemViewWrapper.wrapTvDescription(this.binding2LineContentStyle2.tvDescription);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.binding2LineContentStyle2.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.binding2LineContentStyle2.ivStartIcon);
        }

        if (endElement == ListItemEndElement.TEXT_VALUE_STYLE2.value) {
            this.binding2LineContentStyle2.tvValue.setText(endItemText);
            this.itemViewWrapper.wrapTvValueStyle2(this.binding2LineContentStyle2.tvValue);
        } else if (endElement == ListItemEndElement.ICON.value) {
            this.itemViewWrapper.wrapIvEndIcon(this.binding2LineContentStyle2.ivEndIcon);
            this.binding2LineContentStyle2.ivEndIcon.setImageDrawable(endIconDraw);
        }
    }

    public ListItemProgress2linecontentStyleBinding bindViewProgress2LineContentStyle(String title, String description, int itemEndElement, Drawable itemEndIcon, String itemEndText) {
        this.bindingProgress2LineContentStyle = ListItemProgress2linecontentStyleBinding.inflate(inflater, parentView, true);
        this.bindingProgress2LineContentStyle.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.bindingProgress2LineContentStyle.tvTitle);

        this.bindingProgress2LineContentStyle.tvDescription.setText(description);
        this.itemViewWrapper.wrapTvDescription(this.bindingProgress2LineContentStyle.tvDescription);
        this.itemViewWrapper.wrapProgressText(this.bindingProgress2LineContentStyle.tvProgressValue);
        this.itemViewWrapper.wrapProgress(this.bindingProgress2LineContentStyle.progressCircular);
        if (itemEndElement == ListItemEndElement.HIDE.value) {
            this.bindingProgress2LineContentStyle.ivEndIcon.setVisibility(View.GONE);
        } else {
            this.itemViewWrapper.wrapIvEndIcon(this.bindingProgress2LineContentStyle.ivEndIcon);
        }
        return this.bindingProgress2LineContentStyle;
    }

    public ListItem2linecontentEndTextIconBinding bindView2LineEndWithTextAndIcon(String title, String description, int itemEndElement, Drawable itemEndIcon, String itemEndText) {
        this.binding2LineEndWithTextAndIcon = ListItem2linecontentEndTextIconBinding.inflate(inflater, parentView, true);
        this.binding2LineEndWithTextAndIcon.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.binding2LineEndWithTextAndIcon.tvTitle);

        if (description == null) {
            this.binding2LineEndWithTextAndIcon.tvDescription.setVisibility(View.GONE);
        } else {
            this.binding2LineEndWithTextAndIcon.tvDescription.setText(description);
            this.itemViewWrapper.wrapTvDescription(this.binding2LineEndWithTextAndIcon.tvDescription);
        }

        this.binding2LineEndWithTextAndIcon.tvValue.setText(itemEndText);
        this.itemViewWrapper.wrapTvValueStyle1(this.binding2LineEndWithTextAndIcon.tvValue);
        if (itemEndElement == ListItemEndElement.INVISIBLE.value) {
            this.binding2LineEndWithTextAndIcon.ivEndIcon.setVisibility(View.INVISIBLE);
        } else {
            this.itemViewWrapper.wrapIvEndIcon(this.binding2LineEndWithTextAndIcon.ivEndIcon);
        }
        return this.binding2LineEndWithTextAndIcon;
    }

    public ListItemIconValueStyle3Binding bindViewListItemIconValueStyle3(String title, String description, int startIconElement, Drawable startIcon, int endItemElement, Drawable endIcon) {
        this.listItemIconValueStyle3Binding = ListItemIconValueStyle3Binding.inflate(inflater, parentView, true);
        this.listItemIconValueStyle3Binding.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.listItemIconValueStyle3Binding.tvTitle);

        this.listItemIconValueStyle3Binding.tvDescription.setText(description);
        this.itemViewWrapper.wrapTvDescription(this.listItemIconValueStyle3Binding.tvDescription);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.listItemIconValueStyle3Binding.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.listItemIconValueStyle3Binding.ivStartIcon);
        }
        if (endItemElement == ListItemEndElement.HIDE.value) {
            this.listItemIconValueStyle3Binding.ivEndIcon.setVisibility(View.GONE);
        } else {
            this.itemViewWrapper.wrapIvEndIcon(this.listItemIconValueStyle3Binding.ivEndIcon);
            this.listItemIconValueStyle3Binding.ivEndIcon.setImageDrawable(endIcon);
        }
        return this.listItemIconValueStyle3Binding;
    }

    public ListItemIconValueBinding bindViewBottomSheetDropdownDialog(String title, int startIconElement, Drawable startIcon, int endItemElement, Drawable endIcon) {
        this.listItemBottomsheetDropdownDialogBinding = ListItemBottomsheetDropdownDialogBinding.inflate(LayoutInflater.from(context), parentView, true);
        this.listItemBottomsheetDropdownDialogBinding.tvTitle.setText(title);
        this.itemViewWrapper.wrapTvTitle(this.listItemBottomsheetDropdownDialogBinding.tvTitle);

        if (startIconElement == ListItemStartElement.DISPLAY.value) {
            this.listItemBottomsheetDropdownDialogBinding.ivStartIcon.setImageDrawable(startIcon);
            this.itemViewWrapper.wrapStartIcon(this.listItemBottomsheetDropdownDialogBinding.ivStartIcon);
        }

        if (endItemElement == ListItemEndElement.ICON.value) {
            this.listItemBottomsheetDropdownDialogBinding.ivEndIcon.setImageDrawable(endIcon);
            this.itemViewWrapper.wrapIvEndIcon(this.listItemBottomsheetDropdownDialogBinding.ivEndIcon);
        }

        return this.bindingIconValue;
    }

    public void setTitle(String title) {
        if (this.itemViewWrapper.tvTitle != null) {
            this.itemViewWrapper.tvTitle.setText(title);
        }
    }

    public void setTitleVisible(boolean isVisible) {
        if (this.itemViewWrapper.tvTitle != null) {
            this.itemViewWrapper.tvTitle.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setDescription(String description) {
        if (this.itemViewWrapper.tvDescription != null) {
            this.itemViewWrapper.tvDescription.setText(Html.fromHtml(description));
        }
    }

    public void setDescriptionVisible(boolean isVisible) {
        if (this.itemViewWrapper.tvDescription != null) {
            this.itemViewWrapper.tvDescription.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setToggleChecked(boolean isChecked) {
        if (this.itemViewWrapper.btnToggle != null) {
            this.itemViewWrapper.btnToggle.setChecked(isChecked);
        }
    }

    public void setCheckBoxChecked(boolean isChecked) {
        if (this.itemViewWrapper.btnCheckBox != null) {
            this.itemViewWrapper.btnCheckBox.setChecked(isChecked);
        }
    }

    public void setToggleVisible(boolean isVisible) {
        if (this.itemViewWrapper.btnToggle != null) {
            this.itemViewWrapper.btnToggle.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setTextValueStyle1(String value) {
        if (this.itemViewWrapper.tvValueStyle1 != null) {
            this.itemViewWrapper.tvValueStyle1.setText(value);
        }
    }

    public void setTextValueStyle1Visible(boolean isVisible) {
        if (this.itemViewWrapper.tvValueStyle1 != null) {
            this.itemViewWrapper.tvValueStyle1.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setTextValueStyle2(String value) {
        if (this.itemViewWrapper.tvValueStyle2 != null) {
            this.itemViewWrapper.tvValueStyle2.setText(value);
        }
    }

    public void setTextValueStyle2Visible(boolean isVisible) {
        if (this.itemViewWrapper.tvValueStyle2 != null) {
            this.itemViewWrapper.tvValueStyle2.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setStartIcon(Drawable startIcon) {
        if (this.itemViewWrapper.ivStartIcon != null) {
            this.itemViewWrapper.ivStartIcon.setImageDrawable(startIcon);
        }
    }

    public void setStartIconVisible(boolean isVisible) {
        if (this.itemViewWrapper.ivStartIcon != null) {
            this.itemViewWrapper.ivStartIcon.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setProgressValue(int currentValue, int maxValue) {
        if (this.itemViewWrapper.progressBar != null && this.itemViewWrapper.tvProgressValue != null
                && maxValue != 0) {
            this.itemViewWrapper.progressBar.setMax(maxValue);
            this.itemViewWrapper.progressBar.setProgress(currentValue);
            int percent = currentValue * 100 / maxValue;
            this.itemViewWrapper.tvProgressValue.setText(percent + "%");
        }
    }

    @Override
    public void setEndIcon(Drawable endIcon) {
        if (this.itemViewWrapper.ivEndIcon != null) {
            this.itemViewWrapper.ivEndIcon.setImageDrawable(endIcon);
        }
    }

    @Override
    public void setEndIconVisible(boolean isVisible) {
        if (this.itemViewWrapper.ivEndIcon != null) {
            this.itemViewWrapper.ivEndIcon.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setButtonCTAText(String text) {
        if (this.itemViewWrapper.btnButtonCta != null) {
            this.itemViewWrapper.btnButtonCta.setText(text);
        }
    }

    @Override
    public void setButtonCTAVisible(boolean isVisible) {
        if (this.itemViewWrapper.btnButtonCta != null) {
            this.itemViewWrapper.btnButtonCta.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setButtonClickListener(View.OnClickListener listener) {
        if (this.itemViewWrapper.btnButtonCta != null) {
            this.itemViewWrapper.btnButtonCta.setOnClickListener(listener);
        }
    }

    @Override
    public TextView getTextViewTitle() {
        return this.itemViewWrapper.tvTitle;
    }

    @Override
    public TextView getTextViewDescription() {
        return this.itemViewWrapper.tvDescription;
    }

    @Override
    public JxToggleButton getButtonToggle() {
        return this.itemViewWrapper.btnToggle;
    }

    @Override
    public JxCheckBoxButton getCheckBoxButton() {
        return this.itemViewWrapper.btnCheckBox;
    }

    @Override
    public TextView getTextViewValueStyle1() {
        return this.itemViewWrapper.tvValueStyle1;
    }

    @Override
    public TextView getTextViewValueStyle2() {
        return this.itemViewWrapper.tvValueStyle2;
    }

    @Override
    public ImageView getImageViewStartIcon() {
        return this.itemViewWrapper.ivStartIcon;
    }

    @Override
    public ImageView getImageViewEndIcon() {
        return this.itemViewWrapper.ivEndIcon;
    }

    @Override
    public JxButton getButtonCTA() {
        return this.itemViewWrapper.btnButtonCta;
    }

    @Override
    public ProgressBar getProgressBar() {
        return this.itemViewWrapper.progressBar;
    }

    @Override
    public TextView getTextViewProgressBar() {
        return this.itemViewWrapper.tvProgressValue;
    }
}
