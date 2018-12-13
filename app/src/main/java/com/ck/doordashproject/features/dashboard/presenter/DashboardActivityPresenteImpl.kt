package com.ck.doordashproject.features.dashboard.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantDetailViewModel
import com.ck.doordashproject.features.dashboard.view.DashboardActivityView
import io.reactivex.disposables.CompositeDisposable

class DashboardActivityPresenteImpl : DashboardActivityPresenter {
    companion object {
        private val TAG = DashboardActivityPresenter::class.java.name
    }

    private val view: DashboardActivityView
    private val detailViewModel: RestaurantDetailViewModel
    private val appNotificationViewModel: AppNotificationViewModel
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors
    private val actionEventModel: RestaurantActionEventModel

    constructor(
        view: DashboardActivityView,
        detailViewModel: RestaurantDetailViewModel,
        appNotificationViewModel: AppNotificationViewModel
    ) : this(
        view,
        detailViewModel,
        appNotificationViewModel,
        CompositeDisposable(),
        RestaurantInteractorsImpl(),
        RestaurantActionEventModel.INSTANCE
    )

    @VisibleForTesting
    constructor(
        view: DashboardActivityView,
        detailViewModel: RestaurantDetailViewModel,
        appNotificationViewModel: AppNotificationViewModel,
        compositeDisposable: CompositeDisposable,
        interactor: RestaurantInteractors,
        actionEventModel: RestaurantActionEventModel
    ) {
        this.view = view
        this.detailViewModel = detailViewModel
        this.appNotificationViewModel = appNotificationViewModel
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
        this.actionEventModel = actionEventModel
    }

    override fun onCreate(owner: LifecycleOwner) {
        view.launchRestaurantsList()
        compositeDisposable.add(
            actionEventModel
                .observeShowRestaurantById()
                .subscribe({ restaurantId ->
                    subscribeRestaurantDetail(restaurantId)
                }, { e -> Log.e(TAG, e.message!!, e) })
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
        detailViewModel.observeRestaurantDetail().removeObservers(owner)
    }

    private fun subscribeRestaurantDetail(restaurantId: Long) {
        compositeDisposable.add(
            interactor
                .getRestaurantDetail(restaurantId)
                .subscribe({ dataModel ->
                    view.launchRestaurantDetail()
                    detailViewModel.setRestaurantDetail(dataModel)
                }, { e ->
                    if (e is RetrofitException) {
                        if (e.getKind() == RetrofitException.Kind.NETWORK) {
                            appNotificationViewModel.setErrorNotification(R.string.network_error)
                        }
                    }
                })
        )
    }
}