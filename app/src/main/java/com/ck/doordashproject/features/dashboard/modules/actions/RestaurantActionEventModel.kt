package com.ck.doordashproject.features.dashboard.modules.actions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RestaurantActionEventModel private constructor(private val pickedRestaurant: PublishSubject<Long>){
    object Holder {
        val event = RestaurantActionEventModel(PublishSubject.create())
    }

    companion object {
        val INSTANCE: RestaurantActionEventModel by lazy { Holder.event }
    }

    fun showRestaurantDetailById(restaurantId: Long) {
        pickedRestaurant.onNext(restaurantId)
    }

    fun observeShowRestaurantById(): Observable<Long> {
        return pickedRestaurant.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}