package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RestaurantViewHolderPresenterImpl : RestaurantViewHolderPresenter, Target {
    private val mView: RestaurantViewHolderView
    private val mActionModel: RestaurantActionEventModel
    private val mImageUtils: ImageUtils

    @VisibleForTesting
    var mDataModel: RestaurantDataModel? = null

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

    override fun onBind(restaurantDataModel: RestaurantDataModel) {
        mDataModel = restaurantDataModel
        mView.setRestaurantName(mDataModel!!.name)
        mView.setRestaurantSubTitle(mDataModel!!.description)
        mView.setRestaurantStatus(mDataModel!!.status_type)
        mImageUtils.loadLogo(mDataModel!!.cover_img_url, this)
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
            mActionModel.showRestaurantDetailById(mDataModel!!.id)
        }
    }
}