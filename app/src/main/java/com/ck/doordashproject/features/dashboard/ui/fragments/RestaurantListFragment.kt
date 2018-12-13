package com.ck.doordashproject.features.dashboard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantListViewModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantListFragmentPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantListFragmentPresenterImpl
import com.ck.doordashproject.features.dashboard.ui.adapters.RestaurantAdapter
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import java.lang.ref.WeakReference

class RestaurantListFragment: Fragment(), RestaurantListView {
    companion object {
        val TAG = RestaurantListFragment::class.java.name
        private const val SOMETHING_WENT_WRONG = "Something went wrong on "

        fun newInstance(): RestaurantListFragment {
            return RestaurantListFragment()
        }
    }

    private var mAdapter: RestaurantAdapter? = null
    private var mPresenter: RestaurantListFragmentPresenter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private lateinit var mViewModel: RestaurantListViewModel
    private lateinit var mAppNotificationViewModel: AppNotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mPresenter == null) {
            mPresenter = RestaurantListFragmentPresenterImpl(this)
        }
        lifecycle.addObserver(mPresenter!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurants_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = LinearLayoutManager(context)
            list_restaurants.layoutManager = mLinearLayoutManager
        }
        if (mAdapter == null) {
            mAdapter = RestaurantAdapter()
        }
        list_restaurants.adapter = mAdapter
        list_restaurants.setHasFixedSize(true)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        list_restaurants.addItemDecoration(decoration)
        list_refresh.setOnRefreshListener{
             -> mPresenter!!.refresh()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            mViewModel = ViewModelProviders.of(this).get(RestaurantListViewModel::class.java)
            mAppNotificationViewModel = ViewModelProviders.of(this).get(AppNotificationViewModel::class.java)
        } ?: throw Exception(SOMETHING_WENT_WRONG.plus(TAG))

        mViewModel.observeRestaurantsList().observe(this, Observer {
                list ->
            mAdapter?.setRestaurants(list)
            list_refresh.isRefreshing = false
        })

        mAppNotificationViewModel.observeErrorNotification().observe(this, Observer {
            list_refresh.isRefreshing = false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mPresenter!!)
        mViewModel.observeRestaurantsList().removeObservers(this)
        mAppNotificationViewModel.observeErrorNotification().removeObservers(this)
    }

    override fun getAppNotificationViewModel(): WeakReference<AppNotificationViewModel> {
        return WeakReference(mAppNotificationViewModel)
    }

    override fun getRestaurantListViewModel(): WeakReference<RestaurantListViewModel> {
        return WeakReference(mViewModel)
    }
}