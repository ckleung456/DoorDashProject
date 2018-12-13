package com.ck.doordashproject.features.dashboard.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ck.doordashproject.R
import com.ck.doordashproject.base.ui.BaseActivity
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantDetailViewModel
import com.ck.doordashproject.features.dashboard.presenter.DashboardActivityPresenteImpl
import com.ck.doordashproject.features.dashboard.presenter.DashboardActivityPresenter
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantDetailFragment
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantListFragment
import com.ck.doordashproject.features.dashboard.view.DashboardActivityView
import kotlinx.android.synthetic.main.toolbar.*

class DashboardActivity : BaseActivity(), DashboardActivityView {
    private var restaurantListFragment: RestaurantListFragment? = null
    private var restaurantDetailFragment: RestaurantDetailFragment? = null
    private var mPresenter: DashboardActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = DashboardActivityPresenteImpl(
            this,
            ViewModelProviders.of(this).get(RestaurantDetailViewModel::class.java),
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

    override fun setTitle(title: String) {
        txt_title.text = title
    }

    override fun launchRestaurantsList() {
        if (restaurantListFragment == null) {
            restaurantListFragment = RestaurantListFragment.newInstance()
        }
        switchFragment(restaurantListFragment!!, RestaurantListFragment.TAG)
    }

    override fun launchRestaurantDetail() {
        if (restaurantDetailFragment == null) {
            restaurantDetailFragment = RestaurantDetailFragment.newInstance()
        }
        switchFragment(restaurantDetailFragment!!, RestaurantDetailFragment.TAG)
    }
}