package com.jarvis.design_system.dialog;

import android.annotation.SuppressLint;
import android.view.View;

import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;
import com.jarvis.design_system.item.JxListItem;

import java.util.LinkedList;
import java.util.List;

public class DialogGenderPicker extends BaseFragmentDialog {

    private JxBottomSheetHeader viewHeader;
    private JxListItem itemGenderMale, itemGenderFemale, itemGenderOthers;
    private String dialogTitle;
    private int selectedGender = 0;
    private List<String> listGenderText;
    private OnSelectItemListener listener;

    @SuppressLint("SimpleDateFormat")
    public DialogGenderPicker(Builder builder) {
        this.dialogTitle = builder.dialogTitle;
        this.listGenderText = builder.listGenderText;
        this.selectedGender = builder.selectGender;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_gender_picker;
    }

    @Override
    protected void initView(View contentView) {
        this.viewHeader = contentView.findViewById(R.id.viewHeader);
        this.itemGenderMale = contentView.findViewById(R.id.itemGenderMale);
        this.itemGenderFemale = contentView.findViewById(R.id.itemGenderFemale);
        this.itemGenderOthers = contentView.findViewById(R.id.itemGenderOthers);

        this.itemGenderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender(0);
                if (listener != null) {
                    listener.onSelected(selectedGender);
                }
                dismiss();
            }
        });
        this.itemGenderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender(1);
                if (listener != null) {
                    listener.onSelected(selectedGender);
                }
                dismiss();
            }
        });
        this.itemGenderOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender(2);
                if (listener != null) {
                    listener.onSelected(selectedGender);
                }
                dismiss();
            }
        });
        this.viewHeader.setTitle(this.dialogTitle);

        if (listGenderText != null) {
            int size = listGenderText.size();
            if (size >= 1) {
                itemGenderMale.getViewBinder().setTitle(listGenderText.get(0));
            }
            if (size >= 2) {
                itemGenderFemale.getViewBinder().setTitle(listGenderText.get(1));
            }
            if (size >= 3) {
                itemGenderOthers.getViewBinder().setTitle(listGenderText.get(2));
            }
        }
        this.selectGender(selectedGender);
    }

    public void setListener(OnSelectItemListener listener) {
        this.listener = listener;
    }

    private void selectGender(int genderIndex) {
        if (genderIndex < 0 || genderIndex > 2) {
            genderIndex = 0;
        }
        this.selectedGender = genderIndex;
        switch (genderIndex) {
            case 0:
                this.itemGenderMale.getViewBinder().getImageViewStartIcon().setSelected(true);
                this.itemGenderFemale.getViewBinder().getImageViewStartIcon().setSelected(false);
                this.itemGenderOthers.getViewBinder().getImageViewStartIcon().setSelected(false);
                break;
            case 1:
                this.itemGenderMale.getViewBinder().getImageViewStartIcon().setSelected(false);
                this.itemGenderFemale.getViewBinder().getImageViewStartIcon().setSelected(true);
                this.itemGenderOthers.getViewBinder().getImageViewStartIcon().setSelected(false);
                break;
            case 2:
                this.itemGenderMale.getViewBinder().getImageViewStartIcon().setSelected(false);
                this.itemGenderFemale.getViewBinder().getImageViewStartIcon().setSelected(false);
                this.itemGenderOthers.getViewBinder().getImageViewStartIcon().setSelected(true);
                break;
        }
    }

    public interface OnSelectItemListener {
        void onSelected(int gender);
    }

    public static class Builder {
        private int selectGender = 0;
        private List<String> listGenderText = new LinkedList<>();
        private String dialogTitle;

        public Builder() {
        }

        public Builder setDialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
            return this;
        }

        public Builder setListGenderText(List<String> listGenderText) {
            this.listGenderText = listGenderText;
            return this;
        }

        public Builder setSelectedGender(int selectGender) {
            this.selectGender = selectGender;
            return this;
        }


        public DialogGenderPicker build() {
            return new DialogGenderPicker(this);
        }
    }
}
