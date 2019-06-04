package com.ck.doordashproject.features.dashboard.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.repository.database.LikedDatabase
import com.ck.doordashproject.features.dashboard.models.repository.database.entity.LikedDb
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RestaurantListFragmentPresenterImpl : RestaurantListFragmentPresenter {
    companion object {
        private val TAG = RestaurantListFragmentPresenter::class.java.name
        @VisibleForTesting
        const val DOOR_DASH_LAT = 37.422740F
        @VisibleForTesting
        const val DOOR_DASH_LNG = -122.139956F
    }
    
    private val db: LikedDatabase
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors
    private val actionEventModel: RestaurantActionEventModel
    private val viewModel: RestaurantViewModel
    private val appNotificationViewModel: AppNotificationViewModel

    constructor(viewModel: RestaurantViewModel, appNotificationViewModel: AppNotificationViewModel, db: LikedDatabase) : this(
        viewModel,
        appNotificationViewModel,
        db,
        CompositeDisposable(),
        RestaurantInteractorsImpl(),
        RestaurantActionEventModel.INSTANCE
    )

    @VisibleForTesting
    constructor(
        viewModel: RestaurantViewModel,
        appNotificationViewModel: AppNotificationViewModel,
        db: LikedDatabase,
        compositeDisposable: CompositeDisposable,
        interactor: RestaurantInteractors,
        actionEventModel: RestaurantActionEventModel
    ) {
        this.viewModel = viewModel
        this.appNotificationViewModel = appNotificationViewModel
        this.db = db
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
        this.actionEventModel = actionEventModel
    }

    override fun onCreate(owner: LifecycleOwner) {
    }

    override fun onStart(owner: LifecycleOwner) {
        subscribeGetRestaurants()
        subscribeLikeStatus()
    }

    override fun onStop(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    /**
     *  restaurant list fragment ondestroy will only be called when this project is destroyed
     */
    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }

    override fun refresh() {
        subscribeGetRestaurants()
    }

    private fun subscribeGetRestaurants() {
        compositeDisposable.add(
            interactor
                .getRestaurantNearBy(DOOR_DASH_LAT, DOOR_DASH_LNG)
                .subscribe({
                    val tmp = ArrayList<RestaurantDataModelWrapper>()
                    for (item in it) {
                        val entity = db.getDao().getById(item.id)
                        val wrapper = if (entity == null) {
                            RestaurantDataModelWrapper(item, LikedStatus.NO_PREF)
                        } else {
                            RestaurantDataModelWrapper(item, entity.likedStatus)
                        }
                        tmp.add(wrapper)
                    }
                    viewModel.setRestaurants(tmp)
                }, { e ->
                    if (e is RetrofitException) {
                        if (e.getKind() == RetrofitException.Kind.NETWORK) {
                            appNotificationViewModel.setErrorNotification(R.string.network_error)
                        }
                    }
                })
        )
    }

    private fun subscribeLikeStatus() {
        compositeDisposable.add(
            actionEventModel.observeLikeStatus()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    db.getDao().insert(LikedDb(it.restaurantData.id, it.likeStatus))
                }) { e -> Log.e(TAG, e.message!!, e) })
    }
}