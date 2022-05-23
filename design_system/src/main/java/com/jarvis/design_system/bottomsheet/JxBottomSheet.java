package com.jarvis.design_system.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jarvis.design_system.databinding.BottomSheetStyleOptionBinding;
import com.jarvis.design_system.item.JxListItem;

public class JxBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetStyleOptionBinding viewBinding;
    private JxBottomSheetConfiguration bottomSheetConfig;
    private BottomSheetItemAdapter itemAdapter;
    private int selectedItemPosition = -1;
    private BottomSheetEventListener listener;
    private boolean hasButtonPositive = false;
    private boolean hasButtonNegative = false;

    public JxBottomSheet() {
    }

    public JxBottomSheet(JxBottomSheetConfiguration bottomSheetConfig) {
        this.bottomSheetConfig = bottomSheetConfig;
    }

    public void setEventListener(BottomSheetEventListener listener) {
        this.listener = listener;
    }

    private void setViewTitleVisible(boolean isVisible) {
        if (this.viewBinding != null) {
            this.viewBinding.viewHeader.setViewTitleVisible(isVisible);
        }
    }

    private void setButtonPositiveVisible(boolean isVisible) {
        if (this.viewBinding != null) {
            this.hasButtonPositive = isVisible;
            this.viewBinding.btnPositive.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void setButtonNegativeVisible(boolean isVisible) {
        if (this.viewBinding != null) {
            this.hasButtonNegative = isVisible;
            this.viewBinding.btnNegative.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void setCloseable(boolean isCloseable) {
        this.bottomSheetConfig.setCloseable(isCloseable);
        if (this.viewBinding != null && !isCloseable) {
            this.viewBinding.viewHeader.setCloseButtonVisible(true);
            this.viewBinding.viewHeader.setGrabberVisible(false);
        }
        this.setCancelable(isCloseable);
    }

    public void setCloseButtonVisible(boolean isVisible) {
        this.bottomSheetConfig.setCloseButtonVisible(isVisible);
        if (this.viewBinding != null) {
            this.viewBinding.viewHeader.setCloseButtonVisible(isVisible);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewBinding = BottomSheetStyleOptionBinding.inflate(inflater, container, true);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.bottomSheetConfig == null){
            this.bottomSheetConfig = new JxBottomSheetConfiguration("");
        }
        if (TextUtils.isEmpty(this.bottomSheetConfig.getTitle())) {
            this.setViewTitleVisible(false);
        } else {
            this.setViewTitleVisible(true);
            this.viewBinding.viewHeader.setTitle(this.bottomSheetConfig.getTitle());
        }

        if (this.bottomSheetConfig == null || TextUtils.isEmpty(this.bottomSheetConfig.getButtonPositiveText())) {
            this.setButtonPositiveVisible(false);
        } else {
            this.setButtonPositiveVisible(true);
            this.viewBinding.btnPositive.setText(this.bottomSheetConfig.getButtonPositiveText());
            this.viewBinding.btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (bottomSheetConfig.getBottomSheetStyle() == JxBottomSheetConfiguration.STYLE_LIST_ITEM) {
                            listener.onButtonPositiveClick(getSelectedItemPosition());
                        } else {
                            listener.onButtonPositiveClick();
                        }
                    }
                    dismiss();
                }
            });
        }
        if (this.bottomSheetConfig == null || TextUtils.isEmpty(this.bottomSheetConfig.getButtonNegativeText())) {
            this.setButtonNegativeVisible(false);
        } else {
            this.setButtonNegativeVisible(true);
            this.viewBinding.btnNegative.setText(this.bottomSheetConfig.getButtonNegativeText());
            this.viewBinding.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onButtonNegativeClick();
                    }
                    dismiss();
                }
            });
        }

        this.setCloseable(bottomSheetConfig.isCloseable());
        if (bottomSheetConfig.getCloseIcon() != null) {
            this.viewBinding.viewHeader.setCloseButtonIcon(bottomSheetConfig.getCloseIcon());
        }
        this.setCloseButtonVisible(bottomSheetConfig.isCloseButtonVisible());
        this.viewBinding.viewHeader.setCloseOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCloseClick();
                }
            }
        });

        if (bottomSheetConfig.getBottomSheetStyle() == JxBottomSheetConfiguration.STYLE_LIST_ITEM) {
            this.viewBinding.tvContent.setVisibility(View.GONE);
            this.viewBinding.rvListItem.setVisibility(View.VISIBLE);

            this.itemAdapter = new BottomSheetItemAdapter(getContext(), this.bottomSheetConfig);
            this.itemAdapter.setOnItemClickListener(new BottomSheetEventListener() {
                @Override
                public void onItemClick(int position, JxListItem itemView) {
                    super.onItemClick(position, itemView);
                    selectedItemPosition = position;
                    if (listener != null) {
                        listener.onItemClick(position, itemView);
                    }
                    notifyDataSetChanged();
                    if (isCancelable() && !hasButtonPositive) {
                        dismiss();
                    }
                }

                @Override
                public void onItemClick(int position) {
                    super.onItemClick(position);
                    selectedItemPosition = position;
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                    if (isCancelable() && !hasButtonPositive) {
                        dismiss();
                    }
                }
            });
            if (bottomSheetConfig.getListItemStyle() == JxBottomSheetConfiguration.LIST_ITEM_STYLE_GRID) {
                int totalItem = bottomSheetConfig.getListData().size();
                this.viewBinding.rvListItem.setLayoutManager(new GridLayoutManager(getContext(), Math.min(totalItem == 0 ? 1 : totalItem, 3)));
            } else {
                this.viewBinding.rvListItem.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            this.viewBinding.rvListItem.setAdapter(this.itemAdapter);
        } else if (bottomSheetConfig.getBottomSheetStyle() == JxBottomSheetConfiguration.STYLE_TEXT_CONTENT) {
            this.viewBinding.rvListItem.setVisibility(View.GONE);
            this.viewBinding.tvContent.setVisibility(View.VISIBLE);
            this.viewBinding.tvContent.setText(Html.fromHtml(bottomSheetConfig.getContent()));
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (listener != null) {
            listener.onDismiss();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        if (listener != null) {
            listener.onCancel();
        }
        super.onCancel(dialog);
    }

    public void notifyDataSetChanged() {
        if (this.itemAdapter != null) {
            this.itemAdapter.notifyDataSetChanged();
        }
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }
}
