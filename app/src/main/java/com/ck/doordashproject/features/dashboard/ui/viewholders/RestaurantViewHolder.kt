package com.ck.doordashproject.features.dashboard.ui.viewholders

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ck.doordashproject.R
import com.ck.doordashproject.base.modules.data.RestaurantDataModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenterImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import kotlinx.android.synthetic.main.adapter_restaurant.view.*

class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), RestaurantViewHolderView, View.OnClickListener {
    val mPresenter: RestaurantViewHolderPresenter

    init {
        mPresenter = RestaurantViewHolderPresenterImpl(this)
        itemView.setOnClickListener(this)
    }

    override fun setRestaurantName(name: String) {
        itemView.txt_restaurant_name.text = name
    }

    override fun setRestaurantSubTitle(subTitle: String) {
        itemView.txt_restaurant_description.text = subTitle
    }

    override fun setRestaurantLogo(bitmap: Bitmap) {
        itemView.img_restaurant_logo.setImageBitmap(bitmap)
    }

    override fun setRestaurantLogo(drawable: Drawable) {
        itemView.img_restaurant_logo.setImageDrawable(drawable)
    }

    override fun setRestaurantStatus(status: String) {
        itemView.txt_restaurant_status.text = status
    }

    override fun getResize(): Int {
        return itemView.context.resources.getDimension(R.dimen.image_resize).toInt()
    }

    override fun onClick(v: View?) {
        mPresenter.showRestaurantDetail()
    }

    fun onBind(restaurantDataModel: RestaurantDataModel) {
        mPresenter.onBind(restaurantDataModel)
    }
}