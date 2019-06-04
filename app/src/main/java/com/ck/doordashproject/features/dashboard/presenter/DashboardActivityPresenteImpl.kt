package com.ck.doordashproject.features.dashboard.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantDetailFragment
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantListFragment
import io.reactivex.disposables.CompositeDisposable

class DashboardActivityPresenteImpl : DashboardActivityPresenter {
    companion object {
        private val TAG = DashboardActivityPresenter::class.java.name
    }

    private val viewModel: RestaurantViewModel
    private val appNotificationViewModel: AppNotificationViewModel
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors
    private val actionEventModel: RestaurantActionEventModel

    constructor(
        viewModel: RestaurantViewModel,
        appNotificationViewModel: AppNotificationViewModel
    ) : this(
        viewModel,
        appNotificationViewModel,
        CompositeDisposable(),
        RestaurantInteractorsImpl(),
        RestaurantActionEventModel.INSTANCE
    )

    @VisibleForTesting
    constructor(
        viewModel: RestaurantViewModel,
        appNotificationViewModel: AppNotificationViewModel,
        compositeDisposable: CompositeDisposable,
        interactor: RestaurantInteractors,
        actionEventModel: RestaurantActionEventModel
    ) {
        this.viewModel = viewModel
        this.appNotificationViewModel = appNotificationViewModel
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
        this.actionEventModel = actionEventModel
    }

    override fun onCreate(owner: LifecycleOwner) {
        viewModel.setShowPage(RestaurantListFragment.TAG)
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
    }

    private fun subscribeRestaurantDetail(restaurantId: Long) {
        compositeDisposable.add(
            interactor
                .getRestaurantDetail(restaurantId)
                .subscribe({ dataModel ->
                    viewModel.setShowPage(RestaurantDetailFragment.TAG)
                    actionEventModel.setPickedRestaurantDetail(dataModel)
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