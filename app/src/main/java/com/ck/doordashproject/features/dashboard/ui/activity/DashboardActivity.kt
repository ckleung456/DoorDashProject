package com.ck.doordashproject.features.dashboard.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.ck.doordashproject.R
import com.ck.doordashproject.base.ui.BaseActivity
import com.ck.doordashproject.base.utils.observeEvent
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantDetailFragment
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantListFragment
import com.ck.doordashproject.features.dashboard.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.viewmodel.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {
    companion object {
        const val EXTRA_RESTAURANTS_LIST = "Doordash.Dashboard.EXTRA_RESTAURANTS_LIST"
        const val EXTRA_RESTAURANT_DETAIL = "Doordash.Dashboard.EXTRA_RESTAURANT_DETAIL"
    }

    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.setState(State.List)
        viewModel.dashboardStateMachine.observeEvent(lifecycleOwner = this) { state ->
            when(state) {
                is State.List -> launchRestaurantsList()
                is State.Detail -> launchRestaurantDetail(restaurantId = state.restaurantId)
            }
        }
    }

    private fun launchRestaurantsList() {
        RestaurantListFragment.newInstance().also {
            supportFragmentManager.beginTransaction().add(
                R.id.lContainer,
                it,
                RestaurantListFragment.TAG
            ).commit()
        }
    }

    private fun launchRestaurantDetail(restaurantId: Long) {
        RestaurantDetailFragment.newInstance(restaurantId = restaurantId).also {
            val transaction = supportFragmentManager.beginTransaction().add(
                R.id.lContainer,
                it,
                RestaurantDetailFragment.TAG
            )
            transaction.addToBackStack(RestaurantDetailFragment.TAG)
            transaction.commit()
        }
    }
}