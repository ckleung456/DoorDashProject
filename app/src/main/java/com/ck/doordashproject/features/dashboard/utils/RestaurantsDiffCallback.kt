package com.ck.doordashproject.features.dashboard.utils

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper

class RestaurantsDiffCallback(val oldList: List<RestaurantDataModelWrapper>, val newList: List<RestaurantDataModelWrapper>): DiffUtil.Callback(){
    companion object {
        const val EXTRA_DIFF_DATA = "com.ck.doordashproject.features.dashboard.utils.RestaurantsDiffCallback.EXTRA_DIFF_DATA"
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Bundle? {
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_DIFF_DATA, newList[newItemPosition])
        return bundle
    }
}