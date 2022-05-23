package com.jarvis.design_system.item;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvis.design_system.button.JxButton;
import com.jarvis.design_system.button.JxCheckBoxButton;
import com.jarvis.design_system.button.JxToggleButton;

public interface IListItem {

    void setTitle(String title);

    TextView getTextViewTitle();

    void setTitleVisible(boolean isVisible);

    void setDescription(String description);

    TextView getTextViewDescription();

    void setDescriptionVisible(boolean isVisible);

    void setToggleChecked(boolean isChecked);

    JxToggleButton getButtonToggle();

    void setToggleVisible(boolean isVisible);

    void setTextValueStyle1(String value);

    void setTextValueStyle1Visible(boolean isVisible);

    TextView getTextViewValueStyle1();

    void setTextValueStyle2(String value);

    void setTextValueStyle2Visible(boolean isVisible);

    TextView getTextViewValueStyle2();

    void setStartIcon(Drawable startIcon);

    void setStartIconVisible(boolean isVisible);

    ImageView getImageViewStartIcon();

    void setEndIcon(Drawable endIcon);

    void setEndIconVisible(boolean isVisible);

    ImageView getImageViewEndIcon();

    void setButtonCTAText(String text);

    void setButtonCTAVisible(boolean isVisible);

    JxButton getButtonCTA();

    JxCheckBoxButton getCheckBoxButton();

    void setButtonClickListener(View.OnClickListener listener);

    ProgressBar getProgressBar();

    TextView getTextViewProgressBar();
}
