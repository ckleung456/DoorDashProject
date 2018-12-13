package com.ck.doordashproject.features.dashboard.presenter

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import io.reactivex.disposables.CompositeDisposable

class RestaurantListFragmentPresenterImpl : RestaurantListFragmentPresenter {
    companion object {
        private val TAG = RestaurantListFragmentPresenter::class.java.name
        @VisibleForTesting
        const val DOOR_DASH_LAT = 37.422740F
        @VisibleForTesting
        const val DOOR_DASH_LNG = -122.139956F
    }

    private val view: RestaurantListView
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors

    constructor(view: RestaurantListView) : this(
        view,
        CompositeDisposable(),
        RestaurantInteractorsImpl()
    )

    @VisibleForTesting
    constructor(
        view: RestaurantListView,
        compositeDisposable: CompositeDisposable,
        interactor: RestaurantInteractors
    ) {
        this.view = view
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
    }

    override fun onStart(owner: LifecycleOwner) {
        subscribeGetRestaurants()
    }

    override fun onStop(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun refresh() {
        subscribeGetRestaurants()
    }

    private fun subscribeGetRestaurants() {
        compositeDisposable.add(interactor
            .getRestaurantNearBy(DOOR_DASH_LAT, DOOR_DASH_LNG)
            .subscribe({
                view.getRestaurantListViewModel().get()?.setRestaurants(it)
            }, { e ->
                if (e is RetrofitException) {
                    if (e.getKind() == RetrofitException.Kind.NETWORK) {
                        view.getAppNotificationViewModel().get()?.setErrorNotification(R.string.network_error)
                    }
                }
            })
        )
    }
}