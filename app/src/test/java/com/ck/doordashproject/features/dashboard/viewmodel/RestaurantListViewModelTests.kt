package com.ck.doordashproject.features.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.repository.network.RetrofitException
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity
import com.ck.doordashproject.features.dashboard.usecase.GetNearByRestaurantsUseCase
import com.ck.doordashproject.features.dashboard.usecase.LikeRestaurantUseCase
import com.ck.doordashproject.features.utils.CoroutinesTestRule
import com.ck.doordashproject.features.utils.RxTrampolineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import kotlin.test.assertEquals

class RestaurantListViewModelTests {
    companion object {
        @ClassRule
        @JvmField
        var rxTrampolineRule = RxTrampolineRule()

        private const val DOOR_DASH_LAT = 37.422740F
        private const val DOOR_DASH_LNG = -122.139956F
        private const val FAKE_ERROR_MSG = "FAKE ERROR MSG"
        private const val FAKE_ID = 1L
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK
    lateinit var savedStateHandleMock: SavedStateHandle

    @MockK
    lateinit var getNearByRestaurantUseCaseMock: GetNearByRestaurantsUseCase

    @MockK(relaxed = true)
    lateinit var likeRestaurantUseCaseMock: LikeRestaurantUseCase

    @MockK(relaxed = true)
    lateinit var compositeDisposableMock: CompositeDisposable

    @MockK(relaxed = true)
    lateinit var showErrorMock: (Int?, String?) -> Unit

    @MockK
    lateinit var dataModelWrapperMock: RestaurantDataModelWrapper

    @MockK
    lateinit var retrofitExceptionMock: RetrofitException

    @MockK
    lateinit var dataModelMock: RestaurantDataModel

    private var underTests: RestaurantListViewModel? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        every {
            savedStateHandleMock.getLiveData<List<RestaurantDataModelWrapper>>(DashboardActivity.EXTRA_RESTAURANTS_LIST)
        } returns MutableLiveData()
        underTests = RestaurantListViewModel(
            savedStateHandle = savedStateHandleMock,
            getNearByRestaurantUseCase = getNearByRestaurantUseCaseMock,
            likeRestaurantUseCase = likeRestaurantUseCaseMock,
            compositeDisposable = compositeDisposableMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `fetch restaurant near by when succeed`() {
        //GIVEN
        every {
            getNearByRestaurantUseCaseMock.getSingle(input = any())
        } returns Single.just(listOf(dataModelWrapperMock))

        //WHEN
        underTests?.fetchRestaurantNearBy(showError = showErrorMock)

        //THEN
        verify {
            getNearByRestaurantUseCaseMock.getSingle(
                input = GetNearByRestaurantsUseCase.Input(
                    lat = DOOR_DASH_LAT,
                    lng = DOOR_DASH_LNG
                )
            )
        }
        assertEquals(listOf(dataModelWrapperMock), underTests?.restaurantList?.value)
    }

    @Test
    fun `fetch restaurant near by when no network`() {
        //GIVEN
        every {
            getNearByRestaurantUseCaseMock.getSingle(input = any())
        } returns Single.error(retrofitExceptionMock)
        every {
            retrofitExceptionMock.getKind()
        } returns RetrofitException.Kind.NETWORK

        //WHEN
        underTests?.fetchRestaurantNearBy(showError = showErrorMock)

        //THEN
        verify {
            getNearByRestaurantUseCaseMock.getSingle(
                input = GetNearByRestaurantsUseCase.Input(
                    lat = DOOR_DASH_LAT,
                    lng = DOOR_DASH_LNG
                )
            )
            showErrorMock.invoke(R.string.network_error, null)
        }
    }

    @Test
    fun `fetch restaurant near by when http error`() {
        //GIVEN
        every {
            getNearByRestaurantUseCaseMock.getSingle(input = any())
        } returns Single.error(retrofitExceptionMock)
        every {
            retrofitExceptionMock.getKind()
        } returns RetrofitException.Kind.HTTP
        every {
            retrofitExceptionMock.message
        } returns FAKE_ERROR_MSG

        //WHEN
        underTests?.fetchRestaurantNearBy(showError = showErrorMock)

        //THEN
        verify {
            getNearByRestaurantUseCaseMock.getSingle(
                input = GetNearByRestaurantsUseCase.Input(
                    lat = DOOR_DASH_LAT,
                    lng = DOOR_DASH_LNG
                )
            )
            showErrorMock.invoke(null, FAKE_ERROR_MSG)
        }
    }

    @Test
    fun `set restaurant like status`() {
        //GIVEN
        every {
            dataModelMock.id
        } returns FAKE_ID
        every {
            dataModelWrapperMock.restaurantData
        } returns dataModelMock
        every {
            dataModelWrapperMock.likeStatus
        } returns LikedStatus.LIKED

        //WHEN
        coroutinesTestRule.testDispatcher.runBlockingTest {
            underTests?.setRestaurantLikeStatus(dataModelWrapperMock)
        }

        //THEN
        verify {
            coroutinesTestRule.testDispatcher.runBlockingTest {
                likeRestaurantUseCaseMock.invoke(
                    input = LikeRestaurantUseCase.Input(
                        restaurantId = FAKE_ID,
                        statuts = LikedStatus.LIKED
                    )
                )
            }
        }
    }
}