package com.jarvis.design_system.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jarvis.design_system.R;

/**
 * Created by Jarvis Nguyen on 20/01/2022.
 */

public class JxToolbar extends RelativeLayout {
    //<editor-fold desc="Properties">
    private final int STYLE_LOGO = 0;
    private final int STYLE_TITLE = 1;
    private final int STYLE_BACK_TITLE = 2;
    private final int STYLE_BACK_TITLE_BUTTON = 3;
    private final int STYLE_BACK_TITLE_ICON = 4;
    private final int STYLE_BACK_TITLE_2ICON = 5;
    private final int STYLE_TITLE_2ICON = 6;

    private OnToolbarActionListener listener;
    private Drawable iconAction1, iconAction2, logo;
    private boolean hideDidiver = false;
    private String textCta;
    private int toolbarStyle;
    private Toolbar toolbarView;
    private View dividerView;
    private Drawable backIcon;
    private MenuItem itemAction1, itemAction2;
    private boolean disableCta;
    //</editor-fold>

    public JxToolbar(Context context) {
        super(context);
        initView(context, null);
    }

    public JxToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public JxToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View viewRoot = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this);
        this.toolbarView = viewRoot.findViewById(R.id.toolbar_view);
        this.dividerView = viewRoot.findViewById(R.id.divider_toolbar_view);
        this.initToolbar(context, attrs);
    }

    private void initToolbar(Context context, AttributeSet attrs) {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.white_5));
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.JxToolbar);
            this.toolbarStyle = attributeArray.getInt(R.styleable.JxToolbar_toolbarViewStyle, STYLE_BACK_TITLE);
            String title = attributeArray.getString(R.styleable.JxToolbar_toolbarTitle);
            this.textCta = attributeArray.getString(R.styleable.JxToolbar_toolbarTextCta);
            this.iconAction1 = attributeArray.getDrawable(R.styleable.JxToolbar_toolbarAction1);
            this.iconAction2 = attributeArray.getDrawable(R.styleable.JxToolbar_toolbarAction2);
            this.logo = attributeArray.getDrawable(R.styleable.JxToolbar_toolbarLogo);
            this.hideDidiver = attributeArray.getBoolean(R.styleable.JxToolbar_toolbarHideDivider, false);
            this.disableCta = attributeArray.getBoolean(R.styleable.JxToolbar_toolbarDisableCta, false);
            this.backIcon = attributeArray.getDrawable(R.styleable.JxToolbar_toolbarBackIcon);

            if (toolbarStyle == STYLE_TITLE || toolbarStyle == STYLE_TITLE_2ICON) {
                this.toolbarView.setTitleTextAppearance(context, R.style.ToolbarTitle_Large);
            } else {
                this.toolbarView.setTitleTextAppearance(context, R.style.ToolbarTitle_Normal);
            }
            if (toolbarStyle != STYLE_LOGO) {
                this.setTitle(title);
            } else {
                this.toolbarView.setLogo(logo);
            }
            this.dividerView.setVisibility(this.hideDidiver ? GONE : VISIBLE);
        }
    }

    public Toolbar getToolbar() {
        return this.toolbarView;
    }

    public void setOnToolbarActiontListener(OnToolbarActionListener listener) {
        this.listener = listener;
    }

    public boolean onCreateOptionsMenu(AppCompatActivity context, Menu menu) {
        ActionBar supportActionBar = context.getSupportActionBar();
        if (hasBackButton() && supportActionBar != null) {
            if (backIcon == null) {
                backIcon = ContextCompat.getDrawable(context, R.drawable.ic_back);
            }
            supportActionBar.setHomeAsUpIndicator(backIcon);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        context.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        itemAction1 = menu.findItem(R.id.action_1);
        itemAction2 = menu.findItem(R.id.action_2);
        if (this.toolbarStyle == STYLE_BACK_TITLE_2ICON || this.toolbarStyle == STYLE_TITLE_2ICON
                || this.toolbarStyle == STYLE_LOGO) {
            if (this.iconAction1 != null) {
                itemAction1.setIcon(this.iconAction1);
            } else {
                itemAction1.setVisible(false);
            }
            if (this.iconAction2 != null) {
                itemAction2.setIcon(this.iconAction2);
            } else {
                itemAction2.setVisible(false);
            }
        } else if (this.toolbarStyle == STYLE_BACK_TITLE_ICON && this.iconAction2 != null) {
            itemAction1.setVisible(false);
            itemAction2.setIcon(this.iconAction2);
        } else if (this.toolbarStyle == STYLE_BACK_TITLE_BUTTON && !TextUtils.isEmpty(textCta)) {
            itemAction1.setVisible(false);
            itemAction2.setTitle(textCta);
            if (disableCta) {
                updateTitleCta(false);
            }
        } else {
            itemAction1.setVisible(false);
            itemAction2.setVisible(false);
        }
        return true;
    }

    public void updateTitleCta(boolean isEnable) {
        if (itemAction2 == null) {
            return;
        }
        SpannableString spanString = new SpannableString(itemAction2.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(isEnable ? getResources().getColor(R.color.pri_4) : getResources().getColor(R.color.ink_2)), 0, spanString.length(), 0); //fix the color to white
        itemAction2.setTitle(spanString);
        itemAction2.setEnabled(isEnable);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_1) {
            if (listener != null) {
                listener.onToolbarAction1Click();
            }
            return true;
        } else if (itemId == R.id.action_2) {
            if (listener != null) {
                if (toolbarStyle == STYLE_BACK_TITLE_BUTTON) {
                    listener.onToolbarTextCtaClick();
                } else {
                    listener.onToolbarAction2Click();
                }
            }
            return true;
        }
        return false;
    }

    public boolean hasBackButton() {
        return this.toolbarStyle > STYLE_TITLE && this.toolbarStyle < STYLE_TITLE_2ICON;
    }

    public void setVisibleAction2(boolean isVisible) {
        int size = this.toolbarView.getMenu().size();
        if (size >= 2) {
            MenuItem item = this.toolbarView.getMenu().getItem(1);
            if (item != null) {
                item.setVisible(isVisible);
            }
        }

    }

    public void setVisibleAction1(boolean isVisible) {
        int size = this.toolbarView.getMenu().size();
        if (size >= 1) {
            MenuItem item = this.toolbarView.getMenu().getItem(0);
            if (item != null) {
                item.setVisible(isVisible);
            }
        }
    }

    public void setVisibleDivider(boolean isVisible) {
        this.dividerView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public CharSequence getTitle() {
        return this.toolbarView.getTitle();
    }

    public void setTitle(String title) {
        this.toolbarView.setTitle(title);
    }

    public void setTitle(int title) {
        this.toolbarView.setTitle(title);
    }

    public void setIconAction1(Drawable iconAction1) {
        this.iconAction1 = iconAction1;
    }

    public void setIconAction2(Drawable iconAction2) {
        this.iconAction2 = iconAction2;
    }

    public MenuItem getItemAction1() {
        return itemAction1;
    }

    public MenuItem getItemAction2() {
        return itemAction2;
    }
}
