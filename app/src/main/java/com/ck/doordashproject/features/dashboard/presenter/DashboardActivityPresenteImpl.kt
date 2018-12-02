package com.ck.doordashproject.features.dashboard.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.features.dashboard.modules.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.view.DashboardActivityView
import io.reactivex.disposables.CompositeDisposable

class DashboardActivityPresenteImpl: DashboardActivityPresenter {
    companion object {
        private val TAG = DashboardActivityPresenter::class.java.name
    }
    private val view: DashboardActivityView
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors
    private val actionEventModel: RestaurantActionEventModel

    constructor(view: DashboardActivityView): this(
            view,
            CompositeDisposable(),
            RestaurantInteractorsImpl(),
            RestaurantActionEventModel.INSTANCE
    )

    @VisibleForTesting
    constructor(view: DashboardActivityView, compositeDisposable: CompositeDisposable, interactor: RestaurantInteractors,
                actionEventModel: RestaurantActionEventModel) {
        this.view = view
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
        this.actionEventModel = actionEventModel
    }

    override fun onStart(owner: LifecycleOwner) {
        view.launchRestaurantsList()
        compositeDisposable.add(actionEventModel
                .observeShowRestaurantById()
                .subscribe({
                    restaurantId ->
                    subscribeRestaurantDetail(restaurantId)
                }, { e -> Log.e(TAG, e.message!!, e)}))
    }

    override fun onStop(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    private fun subscribeRestaurantDetail(restaurantId: Long) {
        compositeDisposable.add(interactor
                .getRestaurantDetail(restaurantId)
                .subscribe({
                    dataModel -> view.launchRestaurantDetail(dataModel)
                }, { e -> Log.e(TAG, e.message!!, e)}))
    }
}