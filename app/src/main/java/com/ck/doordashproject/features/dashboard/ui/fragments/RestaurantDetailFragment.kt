package com.ck.doordashproject.features.dashboard.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ck.doordashproject.R
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantDetailViewModel
import com.ck.doordashproject.features.dashboard.presenter.RestaurantDetailFragmentPresenter
import com.ck.doordashproject.features.dashboard.presenter.RestaurantDetailFragmentPresenterImpl
import com.ck.doordashproject.features.dashboard.view.RestaurantDetailFragmentView
import kotlinx.android.synthetic.main.fragment_restaurat_detail.*

class RestaurantDetailFragment: Fragment(), RestaurantDetailFragmentView {
    companion object {
        val TAG = RestaurantDetailFragment::class.java.name
        private const val SOMETHING_WENT_WRONG = "Something went wrong on "
        fun newInstance(): RestaurantDetailFragment {
            return RestaurantDetailFragment()
        }
    }

    private var mPresenter: RestaurantDetailFragmentPresenter? = null
    private lateinit var mDetailViewModel: RestaurantDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mPresenter == null) {
            mPresenter = RestaurantDetailFragmentPresenterImpl(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurat_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDetailViewModel = activity?.run {
            ViewModelProviders.of(this).get(RestaurantDetailViewModel::class.java)
        } ?: throw Exception(SOMETHING_WENT_WRONG.plus(TAG))
        mDetailViewModel.observeRestaurantDetail().observe(this, Observer {
            mPresenter?.setDetail(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter = null
        mDetailViewModel.observeRestaurantDetail().removeObservers(this)
    }

    override fun setRestaurantLogo(drawable: Drawable) {
        img_restaurant_label.setImageDrawable(drawable)
    }

    override fun setRestaurantLogo(bitmap: Bitmap) {
        img_restaurant_label.setImageBitmap(bitmap)
    }

    override fun setRestaurantName(name: String) {
        txt_restaurant_name.text = name
    }

    override fun setRestaurantStatus(status: String) {
        txt_restaurant_status.text = status
    }

    override fun setRestaurantPhoneNumber(phoneNumber: String) {
        txt_restaurant_phone_number.text = getString(R.string.phone_number, phoneNumber)
    }

    override fun setRestaurantDescription(description: String) {
        txt_restaurant_description.text = description
    }

    override fun setRestaurantDeliveryFee(deliveryFee: Double) {
        txt_restaurant_delivery_fee.text = getString(R.string.delivery_fee, deliveryFee.toString())
    }

    override fun setRestaurantYelpRating(rating: Double) {
        txt_restaurant_yelp_rating.text = getString(R.string.yelp_rating, rating.toString())
    }

    override fun setRestaurantAverageRating(rating: Double) {
        txt_restaurant_average_rating.text = getString(R.string.average_rating, rating.toString())
    }
}