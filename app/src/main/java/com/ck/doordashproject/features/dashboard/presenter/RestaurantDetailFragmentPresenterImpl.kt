package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.data.RestaurantDetailViewDataModel
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.disposables.CompositeDisposable

class RestaurantDetailFragmentPresenterImpl : RestaurantDetailFragmentPresenter, Target {
    companion object {
        const val RATIO = 100.0
    }

    private val viewModel: RestaurantViewModel
    private val actionEventModel: RestaurantActionEventModel
    private val compositeDisposable: CompositeDisposable
    private val mImageUtils: ImageUtils

    @VisibleForTesting
    var mData: RestaurantDetailDataModel? = null

    constructor(viewModel: RestaurantViewModel) : this(
        viewModel,
        RestaurantActionEventModel.INSTANCE,
        CompositeDisposable(),
        ImageUtils.INSTANCE
    )

    @VisibleForTesting
    constructor(
        viewModel: RestaurantViewModel,
        actionEventModel: RestaurantActionEventModel,
        compositeDisposable: CompositeDisposable,
        imageUtils: ImageUtils
    ) {
        this.viewModel = viewModel
        this.actionEventModel = actionEventModel
        this.compositeDisposable = compositeDisposable
        mImageUtils = imageUtils
    }

    override fun onStart(owner: LifecycleOwner) {
        compositeDisposable.add(actionEventModel
            .observeRestaurantDetail()
            .subscribe {
                Log.w("HA?", "HERE?")
                setDetail(it)
            })
    }

    override fun onStop(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        if (placeHolderDrawable != null && mData != null) {
            setViewData(placeHolderDrawable, null, mData!!)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        if (errorDrawable != null && mData != null) {
            setViewData(errorDrawable, null, mData!!)
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (bitmap != null && mData != null) {
            setViewData(null, bitmap, mData!!)
        }
    }

    @VisibleForTesting
    fun setDetail(detailDataModel: RestaurantDetailDataModel) {
        mData = detailDataModel
        mImageUtils.loadLogo(mData!!.cover_img_url, this)
    }

    private fun setViewData(drawable: Drawable?, logoBitmap: Bitmap?, detailDataModel: RestaurantDetailDataModel) {
        val data = RestaurantDetailViewDataModel(drawable, logoBitmap, detailDataModel.name,
            detailDataModel.status, detailDataModel.phone_number, detailDataModel.description,
            convertCenToDollar(detailDataModel.delivery_fee), detailDataModel.yelp_rating, detailDataModel.average_rating)
        viewModel.setRestaurantDetail(data)
    }

    private fun convertCenToDollar(fee: Long): Double {
        return fee / RATIO
    }
}