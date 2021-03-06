package com.ck.doordashproject.features.dashboard.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel
import com.ck.doordashproject.base.network.RetrofitException
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.dashboard.models.viewmodel.RestaurantViewModel
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantDetailFragment
import com.ck.doordashproject.features.dashboard.ui.fragments.RestaurantListFragment
import com.nhaarman.mockito_kotlin.*
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
@PrepareForTest(
    CompositeDisposable::class,
    LifecycleOwner::class,
    RestaurantActionEventModel::class,
    RestaurantDetailDataModel::class,
    RestaurantViewModel::class,
    AppNotificationViewModel::class,
    RetrofitException::class
)
class DashboardActivityPresenterTests {
    companion object {
        private const val FAKE_ID = 1L
    }

    private lateinit var viewModelMock: RestaurantViewModel
    private lateinit var appNotificationViewModel: AppNotificationViewModel
    private val compositeDisposableMock: CompositeDisposable = mock()
    private val interactorMock: RestaurantInteractors = mock()
    private lateinit var actionEventModelMock: RestaurantActionEventModel
    private val lifecycleOwnerMock: LifecycleOwner = mock()
    private val restaurantDetailDataModelMock: RestaurantDetailDataModel = mock()
    private lateinit var retrofitExceptionMock: RetrofitException

    private var underTests: DashboardActivityPresenter? = null

    @Before
    fun `setup tests`() {
        viewModelMock = PowerMockito.mock(RestaurantViewModel::class.java)
        appNotificationViewModel = PowerMockito.mock(AppNotificationViewModel::class.java)
        actionEventModelMock = PowerMockito.mock(RestaurantActionEventModel::class.java)
        retrofitExceptionMock = PowerMockito.mock(RetrofitException::class.java)
        underTests = DashboardActivityPresenteImpl(
            viewModelMock,
            appNotificationViewModel,
            compositeDisposableMock,
            interactorMock,
            actionEventModelMock
        )
    }

    @After
    fun `tear down tests`() {
        underTests = null
    }

    @Test
    fun `test on create`() {
        whenever(actionEventModelMock.observeShowRestaurantById()).thenReturn(Observable.just(FAKE_ID))
        whenever(interactorMock.getRestaurantDetail(FAKE_ID)).thenReturn(Observable.just(restaurantDetailDataModelMock))
        underTests!!.onCreate(lifecycleOwnerMock)
        verify(viewModelMock).setShowPage(RestaurantListFragment.TAG)
        verify(compositeDisposableMock, times(2)).add(any())
        verify(actionEventModelMock).observeShowRestaurantById()
        verify(interactorMock).getRestaurantDetail(FAKE_ID)
        verify(viewModelMock).setShowPage(RestaurantDetailFragment.TAG)
        verify(actionEventModelMock).setPickedRestaurantDetail(restaurantDetailDataModelMock)
        verifyZeroInteractions(appNotificationViewModel)

        whenever(interactorMock.getRestaurantDetail(FAKE_ID)).thenReturn(Observable.error(retrofitExceptionMock))
        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.CONVERSION)
        underTests!!.onCreate(lifecycleOwnerMock)
        verifyZeroInteractions(appNotificationViewModel)

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.UNEXPECTED)
        underTests!!.onCreate(lifecycleOwnerMock)
        verifyZeroInteractions(appNotificationViewModel)

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.HTTP)
        underTests!!.onCreate(lifecycleOwnerMock)
        verifyZeroInteractions(appNotificationViewModel)

        whenever(retrofitExceptionMock.getKind()).thenReturn(RetrofitException.Kind.NETWORK)
        underTests!!.onCreate(lifecycleOwnerMock)
        verify(viewModelMock, times(5)).setShowPage(RestaurantListFragment.TAG)
        verify(compositeDisposableMock, times(10)).add(any())
        verify(actionEventModelMock, times(5)).observeShowRestaurantById()
        verify(interactorMock, times(5)).getRestaurantDetail(FAKE_ID)
        verify(viewModelMock).setShowPage(RestaurantDetailFragment.TAG)
        verify(actionEventModelMock).setPickedRestaurantDetail(restaurantDetailDataModelMock)
        verify(appNotificationViewModel).setErrorNotification(R.string.network_error)
    }

    @Test
    fun `test on destroy`() {
        underTests!!.onDestroy(lifecycleOwnerMock)
        verify(compositeDisposableMock).clear()
    }
}