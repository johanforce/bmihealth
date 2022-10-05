@file:Suppress("UNUSED_PARAMETER")

package com.jarvis.bmihealth.presentation.register.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.bmihealth.databinding.ItemGoalOnboardingBinding
import com.jarvis.bmihealth.presentation.register.model.GoalModel

class GoalAdapter : RecyclerView.Adapter<GoalAdapter.ViewHolderView>() {
    private var mDataset: MutableList<GoalModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val binding: ItemGoalOnboardingBinding = ItemGoalOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolderView(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(dataSet: MutableList<GoalModel>) {
        mDataset.clear()
        mDataset.addAll(dataSet)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolderView(itemView: ItemGoalOnboardingBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding: ItemGoalOnboardingBinding?
        fun bindView(goalModel: GoalModel, position: Int) {
            binding?.tvContent?.text = goalModel.title
            binding?.tvDes?.visibility =
                if (TextUtils.isEmpty(goalModel.des)) View.GONE else View.VISIBLE
            binding?.tvDes?.text = goalModel.des
        }

        init {
            binding = itemView
        }
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.bindView(mDataset[position], position)
    }
}
