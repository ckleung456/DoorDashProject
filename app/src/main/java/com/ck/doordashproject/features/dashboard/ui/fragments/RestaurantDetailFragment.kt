package com.ck.doordashproject.features.dashboard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.AppBaseViewModel
import com.ck.doordashproject.base.utils.observeEvent
import com.ck.doordashproject.databinding.FragmentRestauratDetailBinding
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity.Companion.EXTRA_RESTAURANT_DETAIL
import com.ck.doordashproject.features.dashboard.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.viewmodel.RestaurantDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment: Fragment() {
    companion object {
        val TAG = RestaurantDetailFragment::class.java.name
        fun newInstance(restaurantId: Long) = RestaurantDetailFragment().apply {
            arguments = bundleOf(
                EXTRA_RESTAURANT_DETAIL to restaurantId
            )
        }
    }

    private var binding: FragmentRestauratDetailBinding? = null

    private val detailViewModel: RestaurantDetailViewModel by viewModels()
    private val appBaseViewModel: AppBaseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRestauratDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.restaurantDetail.observeEvent(lifecycleOwner = viewLifecycleOwner) { data ->
            binding?.apply {
                data.logoDrawable?.let { logo ->
                    imgRestaurantLabel.setImageDrawable(logo)
                }
                data.logoBitmap?.let { bm ->
                    imgRestaurantLabel.setImageBitmap(bm)
                }
                txtRestaurantName.text = data.name
                txtRestaurantStatus.text = data.status
                txtRestaurantPhoneNumber.text = getString(R.string.phone_number, data.phoneNumber)
                txtRestaurantDescription.text = data.description
                txtRestaurantDeliveryFee.text = getString(R.string.delivery_fee, data.deliveryFee.toString())
                txtRestaurantYelpRating.text = getString(R.string.yelp_rating, data.yelpRating.toString())
                txtRestaurantAverageRating.text = getString(R.string.average_rating, data.averageRating.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        detailViewModel.onError = { errResId, errMsg ->
            appBaseViewModel.setErrorNotification(errResId, errMsg)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            arguments?.getLong(EXTRA_RESTAURANT_DETAIL)?.let {
                detailViewModel.setRestaurantId(id = it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}