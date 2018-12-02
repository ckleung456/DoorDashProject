package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel
import com.ck.doordashproject.features.dashboard.modules.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.modules.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.view.DashboardActivityView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.amshove.kluent.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(CompositeDisposable::class, LifecycleOwner::class, RestaurantActionEventModel::class, RestaurantDetailDataModel::class)
class DashboardActivityPresenterTests {
    companion object {
        private const val FAKE_ID = 1L
    }

    private val viewMock: DashboardActivityView = mock()
    private val compositeDisposableMock: CompositeDisposable = mock()
    private val interactorMock: RestaurantInteractors = mock()
    private var actionEventModelMock: RestaurantActionEventModel? = null
    private val lifecycleOwnerMock: LifecycleOwner = mock()
    private val restaurantDetailDataModelMock: RestaurantDetailDataModel = mock()

    private var underTests: DashboardActivityPresenter? = null

    @Before
    fun `setup tests`() {
        actionEventModelMock = PowerMockito.mock(RestaurantActionEventModel::class.java)
        underTests = DashboardActivityPresenteImpl(viewMock, compositeDisposableMock, interactorMock, actionEventModelMock!!)
    }

    @After
    fun `tear down tests`() {
        underTests = null
    }

    @Test
    fun `test on start`() {
        whenever(actionEventModelMock!!.observeShowRestaurantById()).thenReturn(Observable.just(FAKE_ID))
        whenever(interactorMock.getRestaurantDetail(FAKE_ID)).thenReturn(Observable.just(restaurantDetailDataModelMock))
        underTests!!.onStart(lifecycleOwnerMock)
        verify(viewMock).launchRestaurantsList()
        verify(compositeDisposableMock, times(2)).add(any())
        verify(actionEventModelMock)!!.observeShowRestaurantById()
        verify(interactorMock).getRestaurantDetail(FAKE_ID)
        verify(viewMock).launchRestaurantDetail(restaurantDetailDataModelMock)
    }

    @Test
    fun `test on stop`() {
        underTests!!.onStop(lifecycleOwnerMock)
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun `test on destroy`() {
        underTests!!.onDestroy(lifecycleOwnerMock)
        verify(compositeDisposableMock).clear()
    }
}