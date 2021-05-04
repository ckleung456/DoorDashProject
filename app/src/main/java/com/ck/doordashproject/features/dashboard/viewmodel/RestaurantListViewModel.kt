package com.ck.doordashproject.features.dashboard.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.ck.doordashproject.R
import com.ck.doordashproject.base.repository.network.RetrofitException
import com.ck.doordashproject.base.utils.Event
import com.ck.doordashproject.base.utils.fireEvent
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity.Companion.EXTRA_RESTAURANTS_LIST
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity.Companion.EXTRA_RESTAURANT_DETAIL
import com.ck.doordashproject.features.dashboard.usecase.GetNearByRestaurantsUseCase
import com.ck.doordashproject.features.dashboard.usecase.LikeRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNearByRestaurantUseCase: GetNearByRestaurantsUseCase,
    private val likeRestaurantUseCase: LikeRestaurantUseCase,
    private val compositeDisposable: CompositeDisposable
): ViewModel() {
    private val _restaurantList = savedStateHandle.getLiveData<List<RestaurantDataModelWrapper>>(EXTRA_RESTAURANTS_LIST)
    val restaurantList: LiveData<List<RestaurantDataModelWrapper>> = _restaurantList

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun fetchRestaurantNearBy(
        lat: Float = DOOR_DASH_LAT,
        lng: Float = DOOR_DASH_LNG,
        showError: (Int?, String?) -> Unit
    ) {
        getNearByRestaurantUseCase
            .getSingle(
                input = GetNearByRestaurantsUseCase.Input(
                    lat = lat,
                    lng = lng
                )
            )
            .subscribeBy(
                onError = { e ->
                    if (e is RetrofitException) {
                        if (e.getKind() == RetrofitException.Kind.NETWORK) {
                            showError.invoke(R.string.network_error, null)
                        } else if (e.getKind() == RetrofitException.Kind.HTTP) {
                            showError.invoke(null, e.message)
                        }
                    }
                },
                onSuccess = {
                    _restaurantList.postValue(it)
                }
            ).addTo(compositeDisposable)
    }

    fun setRestaurantLikeStatus(data: RestaurantDataModelWrapper) {
        viewModelScope.launch {
            likeRestaurantUseCase.invoke(
                input = LikeRestaurantUseCase.Input(
                    restaurantId = data.restaurantData.id,
                    statuts = data.likeStatus
                )
            )
        }
    }

    companion object {
        private const val DOOR_DASH_LAT = 37.422740F
        private const val DOOR_DASH_LNG = -122.139956F
    }
}