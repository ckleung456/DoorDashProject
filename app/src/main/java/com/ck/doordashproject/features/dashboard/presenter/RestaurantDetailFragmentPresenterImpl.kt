package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.view.RestaurantDetailFragmentView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RestaurantDetailFragmentPresenterImpl: RestaurantDetailFragmentPresenter, Target {
    companion object {
        const val RATIO = 100.0
    }
    private val mView: RestaurantDetailFragmentView
    private val mImageUtils: ImageUtils

    @VisibleForTesting
    var mData: RestaurantDetailDataModel? = null

    constructor(view: RestaurantDetailFragmentView): this(
            view,
            ImageUtils.INSTANCE
    )

    @VisibleForTesting
    constructor(view: RestaurantDetailFragmentView, imageUtils: ImageUtils) {
        mView = view
        mImageUtils = imageUtils
    }

    override fun onStart(owner: LifecycleOwner) {
        if (mData != null) {
            mImageUtils.loadLogo(mData!!.cover_img_url, this)
        }
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        if (placeHolderDrawable != null) {
            mView.setRestaurantLogo(placeHolderDrawable)
        }
        setViewData()
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        if (errorDrawable != null) {
            mView.setRestaurantLogo(errorDrawable)
        }
        setViewData()
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (bitmap != null) {
            mView.setRestaurantLogo(bitmap)
        }
        setViewData()
    }

    override fun setDetail(detailDataModel: RestaurantDetailDataModel) {
        mData = detailDataModel
    }

    private fun setViewData() {
        mView.setRestaurantName(mData!!.name)
        mView.setRestaurantStatus(mData!!.status)
        mView.setRestaurantPhoneNumber(mData!!.phone_number)
        mView.setRestaurantDescription(mData!!.description)
        mView.setRestaurantDeliveryFee(convertCenToDollar(mData!!.delivery_fee))
        mView.setRestaurantYelpRating(mData!!.yelp_rating)
        mView.setRestaurantAverageRating(mData!!.average_rating)
    }

    private fun convertCenToDollar(fee: Long): Double {
        return fee/RATIO
    }
}