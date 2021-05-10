package com.ck.doordashproject.features.dashboard.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.repository.network.RetrofitException
import com.ck.doordashproject.base.utils.Event
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.base.utils.fireEvent
import com.ck.doordashproject.features.dashboard.models.data.RestaurantDetailViewDataModel
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity.Companion.EXTRA_RESTAURANT_DETAIL
import com.ck.doordashproject.features.dashboard.usecase.GetRestaurantDetailUseCase
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val imageUtils: ImageUtils,
    private val compositeDisposable: CompositeDisposable
): ViewModel() {
    private val detailRestaurantId = savedStateHandle.getLiveData<Long>(EXTRA_RESTAURANT_DETAIL)

    lateinit var onError: (Int?, String?) -> Unit

    val restaurantDetail: LiveData<Event<RestaurantDetailViewDataModel>> = MediatorLiveData<Event<RestaurantDetailViewDataModel>>().apply {
        fun update() {
            detailRestaurantId.value?.let {
                getRestaurantDetailUseCase
                    .getSingle(input = GetRestaurantDetailUseCase.Input(restaurantId = it))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = { e ->
                            if (e is RetrofitException) {
                                if (e.getKind() == RetrofitException.Kind.NETWORK) {
                                    onError.invoke(R.string.network_error, null)
                                } else if (e.getKind() == RetrofitException.Kind.HTTP) {
                                    onError.invoke(null, e.message)
                                }
                            }
                        },
                        onSuccess = { dataModel ->
                            dataModel.cover_img_url?.let { cover_img_url ->
                                imageUtils.loadLogo(imageUrl = cover_img_url, object: Target {
                                    override fun onBitmapLoaded(
                                        bitmap: Bitmap?,
                                        from: Picasso.LoadedFrom?
                                    ) {
                                        sent(logoBitmap = bitmap)
                                    }

                                    override fun onBitmapFailed(
                                        e: Exception?,
                                        errorDrawable: Drawable?
                                    ) {
                                        sent(logoDrawable = errorDrawable)
                                    }

                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                        sent(logoDrawable = placeHolderDrawable)
                                    }

                                    private fun sent(logoBitmap: Bitmap? = null, logoDrawable: Drawable? = null) {
                                        fireEvent(RestaurantDetailViewDataModel(
                                            logoDrawable = logoDrawable,
                                            logoBitmap = logoBitmap,
                                            name = dataModel.name ?: "",
                                            status = dataModel.status ?: "",
                                            phoneNumber = dataModel.phone_number ?: "",
                                            description = dataModel.description ?: "",
                                            yelpRating = dataModel.yelp_rating ?: 0.0,
                                            deliveryFee = dataModel.delivery_fee?.let { fee ->
                                                convertCenToDollar(fee)
                                            } ?: 0.0,
                                            averageRating = dataModel.average_rating ?: 0.0
                                        ))
                                    }
                                })
                            }
                        }
                    )
            }
        }

        addSource(detailRestaurantId) { update() }
        update()
    }.distinctUntilChanged()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun setRestaurantId(id: Long) {
        detailRestaurantId.value = id
    }

    private fun convertCenToDollar(fee: Long): Double = fee / RATIO

    companion object {
        @VisibleForTesting
        internal const val RATIO = 100.0
    }
}