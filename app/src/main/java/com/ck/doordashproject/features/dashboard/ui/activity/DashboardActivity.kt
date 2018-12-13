package com.ck.doordashproject.features.dashboard.ui.activity

import android.os.Bundle
import android.text.TextUtils
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

    override fun onBackPressed() {
        if (handleBackPressed()) {
            return
        }
        super.onBackPressed()
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

    override fun setSelectedFragment() {
        if (TextUtils.isEmpty(mVisibleFragmentTag)) {
            mVisibleFragmentTag = getEntryFragmentTag()
        }
        if (TextUtils.equals(mVisibleFragmentTag, RestaurantListFragment.TAG)) {
            mVisibleFragment = restaurantListFragment
        } else if (TextUtils.equals(mVisibleFragmentTag, RestaurantDetailFragment.TAG)) {
            mVisibleFragment = restaurantDetailFragment
        }
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
        switchFragment(mVisibleFragment, restaurantListFragment!!, RestaurantListFragment.TAG)
    }

    override fun launchRestaurantDetail() {
        if (restaurantDetailFragment == null) {
            restaurantDetailFragment = RestaurantDetailFragment.newInstance()
        }
        switchFragment(mVisibleFragment, restaurantDetailFragment!!, RestaurantDetailFragment.TAG)
    }
}