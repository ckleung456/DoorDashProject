package com.ck.doordashproject.features.dashboard.models.actions

import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RestaurantActionEventModel private constructor(private val pickedRestaurant: PublishSubject<Long>, private val restaurantLikeOptionPublisher: PublishSubject<RestaurantDataModelWrapper>){
    object Holder {
        val event = RestaurantActionEventModel(PublishSubject.create(), PublishSubject.create())
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

    fun likeOption(dataModelWrapper: RestaurantDataModelWrapper) {
        restaurantLikeOptionPublisher.onNext(dataModelWrapper)
    }

    fun observeLikeStatus(): Observable<RestaurantDataModelWrapper> {
        return restaurantLikeOptionPublisher.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
}