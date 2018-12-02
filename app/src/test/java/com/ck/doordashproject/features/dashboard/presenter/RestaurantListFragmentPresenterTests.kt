package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.base.modules.data.RestaurantDataModel
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.amshove.kluent.any
import org.amshove.kluent.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(CompositeDisposable::class, LifecycleOwner::class)
class RestaurantListFragmentPresenterTests {
    companion object {
        val FAKE_LIST = ArrayList<RestaurantDataModel>()
    }

    private val viewMock: RestaurantListView = mock()
    private val compositeDisposableMock: CompositeDisposable = mock()
    private val interactorMock: RestaurantInteractors = mock()
    private val lifecycleOwnerMock: LifecycleOwner = mock()

    private var underTests: RestaurantListFragmentPresenter? = null

    @Before
    fun `setup tests`() {
        whenever(interactorMock.getRestaurantNearBy(RestaurantListFragmentPresenterImpl.DOOR_DASH_LAT, RestaurantListFragmentPresenterImpl.DOOR_DASH_LNG))
                .thenReturn(Observable.just(FAKE_LIST))
        underTests = RestaurantListFragmentPresenterImpl(viewMock, compositeDisposableMock, interactorMock)
    }

    @After
    fun `tear dow tests`() {
        underTests = null
    }

    @Test
    fun `test on start`() {
        underTests!!.onStart(lifecycleOwnerMock)
        verify(compositeDisposableMock).add(any(Disposable::class))
        verify(viewMock).setRestaurants(FAKE_LIST)
        verify(viewMock, never()).onRefreshDone()
    }

    @Test
    fun `test on stop`() {
        underTests!!.onStop(lifecycleOwnerMock)
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun `test resfresh`() {
        underTests!!.refresh()
        verify(compositeDisposableMock).add(any(Disposable::class))
        verify(viewMock).setRestaurants(FAKE_LIST)
        verify(viewMock).onRefreshDone()
    }

}