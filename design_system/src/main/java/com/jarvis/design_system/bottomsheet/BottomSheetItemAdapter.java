package com.jarvis.design_system.bottomsheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jarvis.design_system.databinding.BottomSheetItemGridAdapterBinding;
import com.jarvis.design_system.databinding.BottomSheetItemLinearAdapterBinding;

public class BottomSheetItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private JxBottomSheetConfiguration configuration;
    private BottomSheetEventListener listener;
    private Context context;

    public BottomSheetItemAdapter(Context context, JxBottomSheetConfiguration configuration) {
        this.context = context;
        this.configuration = configuration;
    }

    public void setOnItemClickListener(BottomSheetEventListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (configuration.getListItemStyle() == JxBottomSheetConfiguration.LIST_ITEM_STYLE_GRID) {
            BottomSheetItemGridAdapterBinding viewBinding = BottomSheetItemGridAdapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolderListItemGrid(viewBinding);
        }
        BottomSheetItemLinearAdapterBinding viewBinding = BottomSheetItemLinearAdapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolderListItemLinear(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JxBottomSheetItemData itemData = configuration.getListData().get(position);
        if (holder instanceof ViewHolderListItemLinear) {
            ((ViewHolderListItemLinear) holder).bindView(position, itemData);
        }
        if (holder instanceof ViewHolderListItemGrid) {
            ((ViewHolderListItemGrid) holder).bindView(position, itemData);
        }
    }

    @Override
    public int getItemCount() {
        if (configuration == null || configuration.getListData() == null) {
            return 0;
        }
        return configuration.getListData().size();
    }

    public class ViewHolderListItemGrid extends RecyclerView.ViewHolder {
        private BottomSheetItemGridAdapterBinding viewBinding;

        public ViewHolderListItemGrid(@NonNull BottomSheetItemGridAdapterBinding itemView) {
            super(itemView.getRoot());
            this.viewBinding = itemView;
        }

        void bindView(int position, JxBottomSheetItemData itemData) {
            if (itemData.getIconDrawable() != null) {
                this.viewBinding.ivIcon.setImageDrawable(itemData.getIconDrawable());
            } else if (itemData.getIconRes() > 0) {
                this.viewBinding.ivIcon.setImageResource(itemData.getIconRes());
            } else {
                this.viewBinding.ivIcon.setVisibility(View.GONE);
            }
            this.viewBinding.tvContent.setText(itemData.getItemTitle());
            this.viewBinding.viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public class ViewHolderListItemLinear extends RecyclerView.ViewHolder {
        private BottomSheetItemLinearAdapterBinding viewBinding;

        public ViewHolderListItemLinear(@NonNull BottomSheetItemLinearAdapterBinding itemView) {
            super(itemView.getRoot());
            this.viewBinding = itemView;
        }

        void bindView(int position, JxBottomSheetItemData itemData) {
            if (itemData.getIconDrawable() != null) {
                this.viewBinding.viewItem.getViewBinder().setStartIcon(itemData.getIconDrawable());
            } else if (itemData.getIconRes() > 0) {
                this.viewBinding.viewItem.getViewBinder().setStartIcon(ContextCompat.getDrawable(context, itemData.getIconRes()));
            }
            this.viewBinding.viewItem.getViewBinder().setTitle(itemData.getItemTitle());
            this.viewBinding.viewItem.getViewBinder().getImageViewStartIcon().setSelected(itemData.isSelected());
            this.viewBinding.viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position, viewBinding.viewItem);
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
