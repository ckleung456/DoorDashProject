package com.ck.doordashproject.features.dashboard.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ck.doordashproject.R
import com.ck.doordashproject.databinding.AdapterRestaurantBinding
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder
import com.ck.doordashproject.features.dashboard.utils.RestaurantsDiffCallback

class RestaurantAdapter(
    private val showRestaurantDetail: (Long) -> Unit,
    private val setRestaurantLikeStatus: (RestaurantDataModelWrapper) -> Unit
) : RecyclerView.Adapter<RestaurantViewHolder>() {

    var items = listOf<RestaurantDataModelWrapper>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(RestaurantsDiffCallback(field, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder =
        RestaurantViewHolder(
            binding = AdapterRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            showRestaurantDetail = showRestaurantDetail,
            setRestaurantLikeStatus = setRestaurantLikeStatus
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: RestaurantViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            items[position]
        } else {
            (payloads[payloads.size - 1] as Bundle).getParcelable(RestaurantsDiffCallback.EXTRA_DIFF_DATA)
        }?.let {
            holder.onBind(it)
        }
    }
}