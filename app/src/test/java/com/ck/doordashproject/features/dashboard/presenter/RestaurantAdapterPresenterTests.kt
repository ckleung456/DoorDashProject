package com.ck.doordashproject.features.dashboard.presenter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.ck.doordashproject.base.modules.data.RestaurantDataModel
import com.ck.doordashproject.features.dashboard.ui.viewholders.RestaurantViewHolder
import com.ck.doordashproject.features.dashboard.utils.RestaurantsDiffCallback
import com.ck.doordashproject.features.dashboard.view.RestaurantAdapterView
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(RestaurantAdapterPresenterImpl::class, RestaurantsDiffCallback::class, RestaurantDataModel::class, DiffUtil::class,
        RestaurantViewHolder::class, Bundle::class)
class RestaurantAdapterPresenterTests {
    companion object {
        val FAKE_POS = 0
        val FAKE_NEW_LIST = ArrayList<RestaurantDataModel>()
        val FAKE_PAYLOADS = ArrayList<Any>()
    }

    private val viewMock: RestaurantAdapterView = mock()
    private val diffCallbackMock: RestaurantsDiffCallback = mock()
    private val diffResultMock: DiffUtil.DiffResult = mock()
    private val restaurantDataModel: RestaurantDataModel = mock()
    private val restaurantDataModel2: RestaurantDataModel = mock()
    private var viewHolderMock: RestaurantViewHolder? = null
    private val bundle: Bundle = mock()

    private var underTests: RestaurantAdapterPresenterImpl? = null

    @Before
    fun `setup tests`() {
        mockStatic(DiffUtil::class.java)
        viewHolderMock = PowerMockito.mock(RestaurantViewHolder::class.java)
        underTests = RestaurantAdapterPresenterImpl(viewMock)
        PowerMockito.whenNew(RestaurantsDiffCallback::class.java).withArguments(underTests!!.restaurantsList, FAKE_NEW_LIST).thenReturn(diffCallbackMock)
        PowerMockito.`when`(DiffUtil.calculateDiff(diffCallbackMock)).thenReturn(diffResultMock)
    }

    @After
    fun `tear down tests`() {
        underTests = null
    }

    @Test
    fun `test get item count`() {
        assertThat(underTests!!.getItemCount()).isEqualTo(0)
        underTests!!.restaurantsList.add(restaurantDataModel)
        assertThat(underTests!!.getItemCount()).isEqualTo(1)
        underTests!!.restaurantsList.clear()
        assertThat(underTests!!.getItemCount()).isEqualTo(0)
    }

    @Test
    fun `test set restaurants`() {
        FAKE_NEW_LIST.add(restaurantDataModel)
        assertThat(underTests!!.getItemCount()).isEqualTo(0)
        underTests!!.setRestaurants(FAKE_NEW_LIST)
        verify(viewMock).updateContents(diffResultMock)
        assertThat(underTests!!.getItemCount()).isEqualTo(1)
        assertThat(underTests!!.restaurantsList.contains(restaurantDataModel)).isTrue()

        FAKE_NEW_LIST.clear()
        FAKE_NEW_LIST.add(restaurantDataModel2)
        underTests!!.setRestaurants(FAKE_NEW_LIST)
        verify(viewMock, times(2)).updateContents(diffResultMock)
        assertThat(underTests!!.getItemCount()).isEqualTo(1)
        assertThat(underTests!!.restaurantsList.contains(restaurantDataModel)).isFalse()
        assertThat(underTests!!.restaurantsList.contains(restaurantDataModel2)).isTrue()
    }

    @Test
    fun `test bind data to holder`() {
        whenever(bundle.getParcelable<RestaurantDataModel>(RestaurantsDiffCallback.EXTRA_DIFF_DATA)).thenReturn(restaurantDataModel2)
        underTests!!.restaurantsList.add(restaurantDataModel)
        underTests!!.bindDataToHolder(viewHolderMock!!, FAKE_POS, FAKE_PAYLOADS)
        verify(viewHolderMock)!!.onBind(restaurantDataModel)
        FAKE_PAYLOADS.add(bundle)
        underTests!!.bindDataToHolder(viewHolderMock!!, FAKE_POS, FAKE_PAYLOADS)
        verify(viewHolderMock)!!.onBind(restaurantDataModel2)
    }
}