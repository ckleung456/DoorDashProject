package com.ck.doordashproject.features.dashboard.ui.viewholders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ck.doordashproject.R
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenterImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import kotlinx.android.synthetic.main.adapter_restaurant.view.*

class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), RestaurantViewHolderView {
    val mPresenter: RestaurantViewHolderPresenter
    lateinit var context: Context

    init {
        mPresenter = RestaurantViewHolderPresenterImpl(this)
        context = itemView.context
//        itemView.setOnClickListener(this)
        itemView.img_option.setOnClickListener {
            mPresenter.performLikeOption()
        }
        itemView.setOnClickListener {
            mPresenter.showRestaurantDetail()
        }
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

    override fun setRestaurantLikedStatus(status: Int) {
        val statusString = context.getString(status)
        itemView.img_option.text = context.getString(R.string.like_option, statusString)
    }

    fun onBind(restaurantDataModel: RestaurantDataModelWrapper) {
        mPresenter.onBind(restaurantDataModel)
    }
}