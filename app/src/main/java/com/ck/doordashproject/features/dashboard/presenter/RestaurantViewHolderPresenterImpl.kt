package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import com.ck.doordashproject.R
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RestaurantViewHolderPresenterImpl : RestaurantViewHolderPresenter, Target {
    private val mView: RestaurantViewHolderView
    private val mActionModel: RestaurantActionEventModel
    private val mImageUtils: ImageUtils

    @VisibleForTesting
    var mDataModel: RestaurantDataModelWrapper? = null

    var likeStatus: LikedStatus = LikedStatus.NO_PREF

    constructor(view: RestaurantViewHolderView) : this(
            view,
            RestaurantActionEventModel.INSTANCE,
            ImageUtils.INSTANCE)

    @VisibleForTesting
    constructor(view: RestaurantViewHolderView, actionModel: RestaurantActionEventModel, imageUtils: ImageUtils) {
        mView = view
        mActionModel = actionModel
        mImageUtils = imageUtils
    }

    override fun onBind(restaurantDataModel: RestaurantDataModelWrapper) {
        mDataModel = restaurantDataModel
        val data = mDataModel!!.restaurantData
        mView.setRestaurantName(data.name)
        mView.setRestaurantSubTitle(data.description)
        mView.setRestaurantStatus(data.status_type)
        mImageUtils.loadLogo(data.cover_img_url, this)
        when {
            mDataModel!!.likeStatus == LikedStatus.LIKED -> mView.setRestaurantLikedStatus(R.string.like_status_liked)
            mDataModel!!.likeStatus == LikedStatus.UN_LIKED -> mView.setRestaurantLikedStatus(R.string.like_status_unliked)
            mDataModel!!.likeStatus == LikedStatus.NO_PREF -> mView.setRestaurantLikedStatus(R.string.like_status_no_pref)
        }
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        if (placeHolderDrawable != null) {
            mView.setRestaurantLogo(placeHolderDrawable)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        if (errorDrawable != null) {
            mView.setRestaurantLogo(errorDrawable)
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (bitmap != null) {
            mView.setRestaurantLogo(bitmap)
        }
    }

    override fun showRestaurantDetail() {
        if (mDataModel != null) {
            mActionModel.showRestaurantDetailById(mDataModel!!.restaurantData.id)
        }
    }

    override fun performLikeOption() {
        if (likeStatus == LikedStatus.NO_PREF || likeStatus == LikedStatus.UN_LIKED) {
            likeStatus = LikedStatus.LIKED
        } else {
            likeStatus = LikedStatus.UN_LIKED
        }
        when {
            mDataModel!!.likeStatus == LikedStatus.LIKED -> mView.setRestaurantLikedStatus(R.string.like_status_liked)
            mDataModel!!.likeStatus == LikedStatus.UN_LIKED -> mView.setRestaurantLikedStatus(R.string.like_status_unliked)
            mDataModel!!.likeStatus == LikedStatus.NO_PREF -> mView.setRestaurantLikedStatus(R.string.like_status_no_pref)
        }
        mDataModel!!.likeStatus = likeStatus
        mActionModel.likeOption(mDataModel!!)
    }
}