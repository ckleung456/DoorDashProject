package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import com.ck.doordashproject.R
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RestaurantViewHolderPresenterImpl constructor(
    private val view: RestaurantViewHolderView,
    private val imageUtils: ImageUtils = ImageUtils(),
    private val showRestaurantDetail: (Long) -> Unit,
    private val setRestaurantLikeStatus: (RestaurantDataModelWrapper) -> Unit
) : RestaurantViewHolderPresenter, Target {

    @VisibleForTesting
    var dataModel: RestaurantDataModelWrapper? = null

    var likeStatus: LikedStatus = LikedStatus.NO_PREF

    override fun onBind(restaurantDataModel: RestaurantDataModelWrapper) {
        dataModel = restaurantDataModel
        dataModel?.restaurantData?.let { data ->
            data.name?.let {
                view.setRestaurantName(it)
            }
            data.description?.let {
                view.setRestaurantSubTitle(it)
            }
            data.status_type?.let {
                view.setRestaurantStatus(it)
            }
            data.cover_img_url?.let {
                imageUtils.loadLogo(it, this)
            }
        }
        when (dataModel?.likeStatus) {
            LikedStatus.LIKED -> view.setRestaurantLikedStatus(R.string.like_status_liked)
            LikedStatus.UN_LIKED -> view.setRestaurantLikedStatus(R.string.like_status_unliked)
            LikedStatus.NO_PREF -> view.setRestaurantLikedStatus(R.string.like_status_no_pref)
        }
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        placeHolderDrawable?.let {
            view.setRestaurantLogo(it)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        errorDrawable?.let {
            view.setRestaurantLogo(it)
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmap?.let {
            view.setRestaurantLogo(it)
        }
    }

    override fun showRestaurantDetail() {
        dataModel?.restaurantData?.id?.let {
            showRestaurantDetail.invoke(it)
        }
    }

    override fun performLikeOption() {
        likeStatus = if (likeStatus == LikedStatus.NO_PREF || likeStatus == LikedStatus.UN_LIKED) {
            LikedStatus.LIKED
        } else {
            LikedStatus.UN_LIKED
        }
        when (likeStatus) {
            LikedStatus.LIKED -> view.setRestaurantLikedStatus(R.string.like_status_liked)
            LikedStatus.UN_LIKED -> view.setRestaurantLikedStatus(R.string.like_status_unliked)
            LikedStatus.NO_PREF -> view.setRestaurantLikedStatus(R.string.like_status_no_pref)
        }
        dataModel?.let {
            it.likeStatus = likeStatus
            setRestaurantLikeStatus.invoke(it)
        }
    }
}