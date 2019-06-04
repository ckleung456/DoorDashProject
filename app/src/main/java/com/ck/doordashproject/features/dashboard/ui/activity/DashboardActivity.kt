package com.ck.doordashproject.features.dashboard.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ck.doordashproject.R
import com.ck.doordashproject.base.ui.BaseActivity
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.presenter.DashboardActivityPresenteImpl
import com.ck.doordashproject.features.dashboard.presenter.DashboardActivityPresenter
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantDetailFragment
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantListFragment

class DashboardActivity : BaseActivity() {
    private var restaurantListFragment: RestaurantListFragment? = null
    private var restaurantDetailFragment: RestaurantDetailFragment? = null
    private var mPresenter: DashboardActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel: RestaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel::class.java)
        viewModel.apply {
            viewModel.observeShowPage().observe(this@DashboardActivity, Observer {
                when(it) {
                    RestaurantListFragment.TAG -> launchRestaurantsList()
                    RestaurantDetailFragment.TAG -> launchRestaurantDetail()
                    else -> {
                    }
                }
            })
        }
        mPresenter = DashboardActivityPresenteImpl(
            viewModel,
            appNotificationViewModel

        )
        lifecycle.addObserver(mPresenter!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            lifecycle.removeObserver(mPresenter!!)
            mPresenter = null
        }
        restaurantListFragment = null
        restaurantDetailFragment = null
    }

    override fun getContainerId(): Int {
        return R.id.lContainer
    }

    override fun getSelectedFragment(toTag: String): Fragment {
        if (toTag == RestaurantDetailFragment.TAG) {
            return restaurantDetailFragment!!
        }
        return restaurantListFragment!!
    }

    override fun getEntryFragmentTag(): String {
        return RestaurantListFragment.TAG
    }

    private fun launchRestaurantsList() {
        if (restaurantListFragment == null) {
            restaurantListFragment = RestaurantListFragment.newInstance()
        }
        switchFragment(restaurantListFragment!!, RestaurantListFragment.TAG)
    }

    private fun launchRestaurantDetail() {
        if (restaurantDetailFragment == null) {
            restaurantDetailFragment = RestaurantDetailFragment.newInstance()
        }
        switchFragment(restaurantDetailFragment!!, RestaurantDetailFragment.TAG)
    }
}