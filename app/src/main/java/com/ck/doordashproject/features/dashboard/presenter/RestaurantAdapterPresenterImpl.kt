package com.ck.doordashproject.features.dashboard.presenter

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder
import com.ck.doordashproject.features.dashboard.utils.RestaurantsDiffCallback
import com.ck.doordashproject.features.dashboard.view.RestaurantAdapterView

class RestaurantAdapterPresenterImpl(val view: RestaurantAdapterView): RestaurantAdatperPresenter {

    @VisibleForTesting
    var restaurantsList = ArrayList<RestaurantDataModelWrapper>()

    override fun getItemCount(): Int {
        return restaurantsList.size
    }

    override fun setRestaurants(newList: ArrayList<RestaurantDataModelWrapper>) {
        val diffCallback = RestaurantsDiffCallback(restaurantsList, newList)
        view.updateContents(DiffUtil.calculateDiff(diffCallback))
        restaurantsList.clear()
        restaurantsList.addAll(newList)
    }

    override fun bindDataToHolder(holder: RestaurantViewHolder, position: Int, payloads: MutableList<Any>) {
        val data: RestaurantDataModelWrapper
        if (payloads.isEmpty()) {
            data = restaurantsList[position]
        } else {
            val diffBundle = payloads[payloads.size - 1] as Bundle
            data = diffBundle.getParcelable(RestaurantsDiffCallback.EXTRA_DIFF_DATA) as RestaurantDataModelWrapper
        }
        holder.onBind(data)
    }
}