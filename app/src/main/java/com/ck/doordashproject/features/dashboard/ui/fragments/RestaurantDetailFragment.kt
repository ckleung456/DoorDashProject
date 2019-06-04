package com.ck.doordashproject.features.dashboard.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ck.doordashproject.R
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantDetailFragmentPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantDetailFragmentPresenterImpl
import kotlinx.android.synthetic.main.fragment_restaurat_detail.*

class RestaurantDetailFragment: Fragment() {
    companion object {
        val TAG = RestaurantDetailFragment::class.java.name
        private const val SOMETHING_WENT_WRONG = "Something went wrong on "
        fun newInstance(): RestaurantDetailFragment {
            return RestaurantDetailFragment()
        }
    }

    private var mPresenter: RestaurantDetailFragmentPresenter? = null
    private lateinit var mDetailViewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailViewModel = activity?.run {
            ViewModelProviders.of(this).get(RestaurantViewModel::class.java)
        } ?: throw Exception(SOMETHING_WENT_WRONG.plus(TAG))
        if (mPresenter == null) {
            mPresenter = RestaurantDetailFragmentPresenterImpl(mDetailViewModel)
            lifecycle.addObserver(mPresenter!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_restaurat_detail, container, false)

    override fun onStart() {
        super.onStart()
        mDetailViewModel.observeRestaurantDetail().observe(this, Observer {
            Log.w(TAG, "set")
            it.logoDrawable?.let {d ->
                img_restaurant_label.setImageDrawable(d)
            }
            it.logoBitmap?.let { b ->
                img_restaurant_label.setImageBitmap(b)
            }
            txt_restaurant_name.text = it.name
            txt_restaurant_status.text = it.status
            txt_restaurant_phone_number.text = getString(R.string.phone_number, it.phoneNumber)
            txt_restaurant_description.text = it.description
            txt_restaurant_delivery_fee.text = getString(R.string.delivery_fee, it.deliveryFee.toString())
            txt_restaurant_yelp_rating.text = getString(R.string.yelp_rating, it.yelpRating.toString())
            txt_restaurant_average_rating.text = getString(R.string.average_rating, it.averageRating.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.apply {
            lifecycle.removeObserver(mPresenter!!)
        }
        mPresenter = null
    }
}