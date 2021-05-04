package com.ck.doordashproject.features.dashboard.ui.viewholders

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.ck.doordashproject.R
import com.ck.doordashproject.base.utils.setOnThrottleClickListener
import com.ck.doordashproject.databinding.AdapterRestaurantBinding
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantViewHolderPresenterImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import kotlinx.android.synthetic.main.adapter_restaurant.view.*

class RestaurantViewHolder(
    private val binding: AdapterRestaurantBinding,
    val showRestaurantDetail: (Long) -> Unit,
    val setRestaurantLikeStatus: (RestaurantDataModelWrapper) -> Unit
): RecyclerView.ViewHolder(binding.root), RestaurantViewHolderView {
    private val presenter: RestaurantViewHolderPresenter =
        RestaurantViewHolderPresenterImpl(
            view = this,
            showRestaurantDetail = showRestaurantDetail,
            setRestaurantLikeStatus = setRestaurantLikeStatus
        )

    init {
        itemView.img_option.setOnThrottleClickListener(throttleInterval = 200) {
            presenter.performLikeOption()
        }
        itemView.setOnThrottleClickListener {
            presenter.showRestaurantDetail()
        }
    }

    override fun setRestaurantName(name: String) {
        binding.txtRestaurantName.text = name
    }

    override fun setRestaurantSubTitle(subTitle: String) {
        binding.txtRestaurantDescription.text = subTitle
    }

    override fun setRestaurantLogo(bitmap: Bitmap) {
        binding.imgRestaurantLogo.setImageBitmap(bitmap)
    }

    override fun setRestaurantLogo(drawable: Drawable) {
        binding.imgRestaurantLogo.setImageDrawable(drawable)
    }

    override fun setRestaurantStatus(status: String) {
        binding.txtRestaurantStatus.text = status
    }

    override fun setRestaurantLikedStatus(status: Int) {
        val statusString = itemView.context.getString(status)
        binding.imgOption.text = itemView.context.getString(R.string.like_option, statusString)
    }

    fun onBind(restaurantDataModel: RestaurantDataModelWrapper) {
        presenter.onBind(restaurantDataModel)
    }
}