package com.ck.doordashproject.features.dashboard.viewmodel

import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.ck.doordashproject.base.utils.Event
import com.ck.doordashproject.base.utils.fireEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject


@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dashboardStateMachine = savedStateHandle.getLiveData<Event<State>>(EXTRA_CURRENT_STATE)
    val dashboardStateMachine: LiveData<Event<State>> = _dashboardStateMachine

    fun setState(state: State) = _dashboardStateMachine.fireEvent(state)

    companion object {
        @VisibleForTesting
        const val EXTRA_CURRENT_STATE = "Dashboard.EXTRA_CURRENT_STATE"
    }
}

sealed class State: Parcelable {
    @Parcelize
    object List: State()

    @Parcelize
    data class Detail(
        val restaurantId: Long
    ): State()
}