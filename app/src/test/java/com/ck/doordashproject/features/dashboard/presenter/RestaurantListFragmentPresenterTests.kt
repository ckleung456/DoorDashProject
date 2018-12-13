package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.models.repository.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantListViewModel
import com.ck.doordashproject.features.dashboard.view.RestaurantListView
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
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
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.lang.ref.WeakReference

@RunWith(PowerMockRunner::class)
@PrepareForTest(
    CompositeDisposable::class,
    LifecycleOwner::class,
    RestaurantListViewModel::class,
    RetrofitException::class,
    AppNotificationViewModel::class
)
class RestaurantListFragmentPresenterTests {
    companion object {
        val FAKE_LIST = ArrayList<RestaurantDataModel>()
    }

    private val viewMock: RestaurantListView = mock()
    private val compositeDisposableMock: CompositeDisposable = mock()
    private val interactorMock: RestaurantInteractors = mock()
    private val lifecycleOwnerMock: LifecycleOwner = mock()
    private val weakReferenceMock: WeakReference<RestaurantListViewModel> = mock()
    private val weakReference2Mock: WeakReference<AppNotificationViewModel> = mock()
    private lateinit var viewModelMock: RestaurantListViewModel
    private lateinit var appNotificationViewModel: AppNotificationViewModel
    private lateinit var retrofitExceptionMock: RetrofitException

    private var underTests: RestaurantListFragmentPresenter? = null

    @Before
    fun `setup tests`() {
        viewModelMock = PowerMockito.mock(RestaurantListViewModel::class.java)
        appNotificationViewModel = PowerMockito.mock(AppNotificationViewModel::class.java)
        retrofitExceptionMock = PowerMockito.mock(RetrofitException::class.java)
        whenever(weakReferenceMock.get()).thenReturn(null)
        whenever(viewMock.getRestaurantListViewModel()).thenReturn(weakReferenceMock)
        whenever(viewMock.getAppNotificationViewModel()).thenReturn(weakReference2Mock)
        whenever(
            interactorMock.getRestaurantNearBy(
                RestaurantListFragmentPresenterImpl.DOOR_DASH_LAT,
                RestaurantListFragmentPresenterImpl.DOOR_DASH_LNG
            )
        )
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

        whenever(weakReferenceMock.get()).thenReturn(viewModelMock)
        underTests!!.onStart(lifecycleOwnerMock)
        verify(viewModelMock)!!.setRestaurants(FAKE_LIST)
    }

    @Test
    fun `test on stop`() {
        underTests!!.onDestroy(lifecycleOwnerMock)
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun `test refresh`() {
        underTests!!.refresh()
        verify(compositeDisposableMock).add(any(Disposable::class))

        whenever(weakReferenceMock.get()).thenReturn(viewModelMock)
        underTests!!.refresh()
        verify(viewModelMock)!!.setRestaurants(FAKE_LIST)
    }

    @Test
    fun `test no network when fetching list`() {
        whenever(weakReferenceMock.get()).thenReturn(viewModelMock)
        whenever(weakReference2Mock.get()).thenReturn(appNotificationViewModel)
        whenever(
            interactorMock.getRestaurantNearBy(
                RestaurantListFragmentPresenterImpl.DOOR_DASH_LAT,
                RestaurantListFragmentPresenterImpl.DOOR_DASH_LNG
            )
        ).thenReturn(Observable.error(retrofitExceptionMock))
        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.CONVERSION)
        underTests!!.refresh()

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.HTTP)
        underTests!!.refresh()

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.UNEXPECTED)
        underTests!!.refresh()
        verifyZeroInteractions(viewModelMock)
        verifyZeroInteractions(appNotificationViewModel)

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.NETWORK)
        underTests!!.refresh()
        verifyZeroInteractions(viewModelMock)
        verify(appNotificationViewModel).setErrorNotification(R.string.network_error)
    }
}