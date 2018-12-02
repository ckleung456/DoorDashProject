package com.ck.doordashproject.features.dashboard.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import io.reactivex.disposables.CompositeDisposable

class RestaurantListFragmentPresenterImpl: RestaurantListFragmentPresenter {
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

    constructor(view: RestaurantListView): this(view, CompositeDisposable(), RestaurantInteractorsImpl())

    @VisibleForTesting
    constructor(view: RestaurantListView, compositeDisposable: CompositeDisposable, interactor: RestaurantInteractors) {
        this.view = view
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
    }

    override fun onStart(owner: LifecycleOwner) {
        subscribeGetRestaurants(false)
    }

    override fun onStop(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun refresh() {
        subscribeGetRestaurants(true)
    }

    private fun subscribeGetRestaurants(stopLoader: Boolean) {
        compositeDisposable.add(interactor
                .getRestaurantNearBy(DOOR_DASH_LAT, DOOR_DASH_LNG)
                .subscribe({
                    list ->
                    view.setRestaurants(list)
                    if (stopLoader) {
                        view.onRefreshDone()
                    }
                }, { e -> Log.e(TAG, e.message, e)}))
    }
}