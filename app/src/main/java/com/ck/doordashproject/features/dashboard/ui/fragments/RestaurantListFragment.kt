package com.ck.doordashproject.features.dashboard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ck.doordashproject.base.models.AppBaseViewModel
import com.ck.doordashproject.base.utils.observe
import com.ck.doordashproject.databinding.FragmentRestaurantsListBinding
import com.ck.doordashproject.features.dashboard.ui.adapters.RestaurantAdapter
import com.ck.doordashproject.features.dashboard.viewmodel.RestaurantListViewModel
import com.ck.doordashproject.features.dashboard.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.viewmodel.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {
    companion object {
        val TAG: String = RestaurantListFragment::class.java.name

        fun newInstance() = RestaurantListFragment()
    }

    private var binding: FragmentRestaurantsListBinding? = null

    private val viewModel: RestaurantViewModel by activityViewModels()

    private val restaurantListViewModel: RestaurantListViewModel by viewModels()

    private val appBaseViewModel: AppBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            listRestaurants.layoutManager = LinearLayoutManager(requireContext())
            val adapter = RestaurantAdapter(
                showRestaurantDetail = this@RestaurantListFragment::showRestaurantDetail,
                setRestaurantLikeStatus = restaurantListViewModel::setRestaurantLikeStatus
            )
            listRestaurants.adapter = adapter
            listRestaurants.setHasFixedSize(true)
            listRestaurants.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            )

            listRefresh.setOnRefreshListener {
                fetchRestaurantNearBy()
            }

            restaurantListViewModel.restaurantList.observe(lifecycleOwner = viewLifecycleOwner) {
                adapter.items = it
                listRefresh.isRefreshing = false
            }

            appBaseViewModel.appNotificationLiveData.observe(lifecycleOwner = viewLifecycleOwner) {
                listRefresh.isRefreshing = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fetchRestaurantNearBy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            fetchRestaurantNearBy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun fetchRestaurantNearBy() {
        restaurantListViewModel.fetchRestaurantNearBy { errResId, errMsg ->
            appBaseViewModel.setErrorNotification(errorResId = errResId, errorMsg = errMsg)
        }
    }

    private fun showRestaurantDetail(restaurantId: Long) {
        viewModel.setState(State.Detail(restaurantId = restaurantId))
    }
}