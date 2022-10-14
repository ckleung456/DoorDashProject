package com.ck.doordashproject.features.dashboard.viewmodel

import androidx.lifecycle.*
import com.ck.doordashproject.R
import com.ck.doordashproject.base.repository.network.RetrofitException
import com.ck.doordashproject.base.usecase.UseCaseOutputWithStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity.Companion.EXTRA_RESTAURANTS_LIST
import com.ck.doordashproject.features.dashboard.usecase.GetNearByRestaurantsUseCase
import com.ck.doordashproject.features.dashboard.usecase.LikeRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNearByRestaurantUseCase: GetNearByRestaurantsUseCase,
    private val likeRestaurantUseCase: LikeRestaurantUseCase
): ViewModel() {
    private val _restaurantList = savedStateHandle.getLiveData<List<RestaurantDataModelWrapper>>(EXTRA_RESTAURANTS_LIST)
    val restaurantList: LiveData<List<RestaurantDataModelWrapper>> = _restaurantList

    fun fetchRestaurantNearBy(
        lat: Float = DOOR_DASH_LAT,
        lng: Float = DOOR_DASH_LNG,
        showError: (Int?, String?) -> Unit
    ) {
        viewModelScope.launch {
            getNearByRestaurantUseCase
                .invoke(
                    input = GetNearByRestaurantsUseCase.Input(
                        lat = lat,
                        lng = lng
                    )
                ) { state ->
                    when (state) {
                        is UseCaseOutputWithStatus.Progress -> { }
                        is UseCaseOutputWithStatus.Failed -> {
                            val error = state.error
                            if (error.getKind() == RetrofitException.Kind.NETWORK) {
                                showError.invoke(R.string.network_error, null)
                            } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                                showError.invoke(null, error.message)
                            }
                        }
                        is UseCaseOutputWithStatus.Success -> {
                            val list = state.result
                            _restaurantList.postValue(list)
                        }
                    }
                }
        }
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