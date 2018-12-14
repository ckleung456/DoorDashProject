package com.ck.doordashproject.features.dashboard.presenter

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractorsImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable

class RestaurantListFragmentPresenterImpl : RestaurantListFragmentPresenter {
    companion object {
        private val TAG = RestaurantListFragmentPresenter::class.java.name
        @VisibleForTesting
        const val DOOR_DASH_LAT = 37.422740F
        @VisibleForTesting
        const val DOOR_DASH_LNG = -122.139956F
        const val MAP_KEY = "map_key"
    }

    private val map: HashMap<Long, Int> = HashMap()

    private val view: RestaurantListView
    private val preferences: SharedPreferences
    private val compositeDisposable: CompositeDisposable
    private val interactor: RestaurantInteractors
    private val actionEventModel: RestaurantActionEventModel
    private val gson: Gson

    constructor(view: RestaurantListView, preferences: SharedPreferences) : this(
        view,
        preferences,
        CompositeDisposable(),
        RestaurantInteractorsImpl(),
        RestaurantActionEventModel.INSTANCE,
        Gson()
    )

    @VisibleForTesting
    constructor(
        view: RestaurantListView,
        preferences: SharedPreferences,
        compositeDisposable: CompositeDisposable,
        interactor: RestaurantInteractors,
        actionEventModel: RestaurantActionEventModel,
        gson: Gson
    ) {
        this.view = view
        this.preferences = preferences
        this.compositeDisposable = compositeDisposable
        this.interactor = interactor
        this.actionEventModel = actionEventModel
        this.gson = gson
    }

    override fun onCreate(owner: LifecycleOwner) {
        loadMap()
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
        saveMap()
    }

    override fun refresh() {
        subscribeGetRestaurants()
    }

    private fun subscribeGetRestaurants() {
        compositeDisposable.add(interactor
            .getRestaurantNearBy(DOOR_DASH_LAT, DOOR_DASH_LNG)
            .subscribe({
                val tmp = ArrayList<RestaurantDataModelWrapper>()
                for ( item in it) {
                    val wrapper =
                    if (map.containsKey(item.id)) {
                        RestaurantDataModelWrapper(item, intToType(map.get(item.id)!!))
                    } else {
                        RestaurantDataModelWrapper(item, LikedStatus.NO_PREF)
                    }
                    tmp.add(wrapper)
                }
                view.getRestaurantListViewModel().get()?.setRestaurants(tmp)
            }, { e ->
                if (e is RetrofitException) {
                    if (e.getKind() == RetrofitException.Kind.NETWORK) {
                        view.getAppNotificationViewModel().get()?.setErrorNotification(R.string.network_error)
                    }
                }
            })
        )
    }

    private fun subscribeLikeStatus() {
        compositeDisposable.add(
            actionEventModel.observeLikeStatus().subscribe({
                data -> map.put(data.restaurantData.id, typeToInt(data.likeStatus))
            }))
    }

    private fun saveMap() {
        val hashMapString = gson.toJson(map)
        preferences.edit().putString(MAP_KEY, hashMapString).apply()
    }

    private fun loadMap() {
        val storedHashMapString = preferences.getString(MAP_KEY, "")
        if (!storedHashMapString.isNullOrEmpty()) {
            val type = object : TypeToken<HashMap<Long, Int>>() {

            }.type
            val tmpMap = gson.fromJson(storedHashMapString, type) as HashMap<Long, Int>
            map.putAll(tmpMap)
        }
    }

    private fun typeToInt(likedStatus: LikedStatus): Int {
        val i =
        when(likedStatus){
            LikedStatus.LIKED -> 1
            LikedStatus.UN_LIKED -> 0
            else -> -1
        }
        return i
    }

    private fun intToType(i : Int): LikedStatus {
        val d =
            when(i){
                1 -> LikedStatus.LIKED
                0 -> LikedStatus.UN_LIKED
                else -> LikedStatus.NO_PREF
            }
        return d
    }
}