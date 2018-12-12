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
import com.ck.doordashproject.features.dashboard.modules.viewmodel.RestaurantListViewModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantListFragmentPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantListFragmentPresenterImpl
import com.ck.doordashproject.features.dashboard.ui.adapters.RestaurantAdapter
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import java.lang.ref.WeakReference

class RestaurantListFragment: Fragment(), RestaurantListView {
    companion object {
        val TAG = RestaurantListFragment::class.java.name

        fun newInstance(): RestaurantListFragment {
            return RestaurantListFragment()
        }
    }

    private var mAdatper: RestaurantAdapter? = null
    private var mPresenter: RestaurantListFragmentPresenter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mViewModel: RestaurantListViewModel? = null

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
        if (mAdatper == null) {
            mAdatper = RestaurantAdapter()
        }
        list_restaurants.adapter = mAdatper
        list_restaurants.setHasFixedSize(true)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        list_restaurants.addItemDecoration(decoration)
        list_refresh.setOnRefreshListener{
             -> mPresenter!!.refresh()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = activity?.run {
            ViewModelProviders.of(this).get(RestaurantListViewModel::class.java)
        }
        mViewModel!!.getRestaurants().observe(this, Observer {
            mAdatper?.setRestaurants(it)
            if (list_refresh.isRefreshing) {
                list_refresh.isRefreshing = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mPresenter!!)
    }

    override fun getRestaurantListViewModel(): WeakReference<RestaurantListViewModel?> {
        return WeakReference(mViewModel)
    }
}