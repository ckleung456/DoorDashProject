package com.ck.doordashproject.features.dashboard.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ck.doordashproject.R
import com.ck.doordashproject.base.modules.data.RestaurantDataModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantAdatperPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantAdapterPresenterImpl
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder
import com.ck.doordashproject.features.dashboard.view.RestaurantAdapterView

class RestaurantAdapter: RecyclerView.Adapter<RestaurantViewHolder>(), RestaurantAdapterView {
    private var mPresenter: RestaurantAdatperPresenter = RestaurantAdapterPresenterImpl(this)
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        mContext = parent.context
        return RestaurantViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_restaurant, parent, false))
    }

    override fun getItemCount(): Int {
        return mPresenter.getItemCount()
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        mPresenter.bindDataToHolder(holder, position, ArrayList())
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int, payloads: MutableList<Any>) {
        mPresenter.bindDataToHolder(holder, position, payloads)
    }

    override fun updateContents(diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(this)
    }

    fun setRestaurants(restaurantsList: ArrayList<RestaurantDataModel>) {
        mPresenter.setRestaurants(restaurantsList)
    }
}