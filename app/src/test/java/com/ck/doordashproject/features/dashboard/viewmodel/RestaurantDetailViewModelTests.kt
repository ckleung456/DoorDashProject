package com.ck.doordashproject.features.dashboard.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.base.repository.network.RetrofitException
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.models.data.RestaurantDetailViewDataModel
import com.ck.doordashproject.features.dashboard.ui.activity.DashboardActivity
import com.ck.doordashproject.features.dashboard.usecase.GetRestaurantDetailUseCase
import com.ck.doordashproject.features.utils.RxTrampolineRule
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RestaurantDetailViewModelTests {

    companion object {
        @ClassRule
        @JvmField
        var rxTrampolineRule = RxTrampolineRule()

        private const val FAKE_ID = 1L
        private const val FAKE_COVER_IMAGE_URL = "FAKE COVER IMAGE URL"
        private const val FAKE_NAME = "FAKE NAME"
        private const val FAKE_STATUS = "FAKE STATUS"
        private const val FAKE_PHONE_NUMBER = "FAKE PHONE NUMBER"
        private const val FAKE_DESC = "FAKE DESC"
        private const val FAKE_YELP_RATING = 4.0
        private const val FAKE_DELIVERY_FEE = 3L
        private const val FAKE_AVERAGE_RATING = 3.0
        private const val FAKE_ERROR = "FAKE ERROR"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var savedStateHandleMock: SavedStateHandle

    @MockK
    lateinit var getRestaurantDetailUseCaseMock: GetRestaurantDetailUseCase

    @MockK
    lateinit var picasso: Picasso

    @MockK(relaxed = true)
    lateinit var compositeDisposableMock: CompositeDisposable

    @MockK(relaxed = true)
    lateinit var onErrorMock: (Int?, String?) -> Unit

    @MockK
    lateinit var detailDataModelMock: RestaurantDetailDataModel

    @MockK
    lateinit var bitmapMock: Bitmap

    @MockK
    lateinit var errorDrawableMock: Drawable

    @MockK
    lateinit var placeHolderDrawableMock: Drawable

    @MockK
    lateinit var picassoFrom : Picasso.LoadedFrom

    @MockK
    lateinit var retrofitExceptionMock: RetrofitException

    private val restaurantIdLiveData = MutableLiveData<Long>()

    private var underTests: RestaurantDetailViewModel? = null

    private lateinit var imageUtilsMock: ImageUtils

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        imageUtilsMock = spyk(ImageUtils(picasso))
        every {
            savedStateHandleMock.getLiveData<Long>(DashboardActivity.EXTRA_RESTAURANT_DETAIL)
        } returns restaurantIdLiveData
        underTests = RestaurantDetailViewModel(
            savedStateHandle = savedStateHandleMock,
            getRestaurantDetailUseCase = getRestaurantDetailUseCaseMock,
            imageUtils = imageUtilsMock,
            compositeDisposable = compositeDisposableMock
        )
        underTests?.onError = onErrorMock
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `restaurant detail when id from argument`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.just(detailDataModelMock)
        every {
            imageUtilsMock.loadLogo(imageUrl = any(), target = any())
        } answers {
            secondArg<Target>().onBitmapLoaded(bitmapMock, picassoFrom)
        }
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        restaurantIdLiveData.value = FAKE_ID

        //THEN
        verify {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
        }

        assertEquals(RestaurantDetailViewDataModel(
            logoDrawable = null,
            logoBitmap = bitmapMock,
            name = FAKE_NAME,
            status = FAKE_STATUS,
            phoneNumber = FAKE_PHONE_NUMBER,
            description = FAKE_DESC,
            yelpRating = FAKE_YELP_RATING,
            deliveryFee = FAKE_DELIVERY_FEE / RestaurantDetailViewModel.RATIO,
            averageRating = FAKE_AVERAGE_RATING
        ), underTests?.restaurantDetail?.value?.peekContent())
    }

    @Test
    fun `set restaurant detail with logo bitmap`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.just(detailDataModelMock)
        every {
            imageUtilsMock.loadLogo(imageUrl = any(), target = any())
        } answers {
            secondArg<Target>().onBitmapLoaded(bitmapMock, picassoFrom)
        }
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        underTests?.setRestaurantId(FAKE_ID)

        //THEN
        verify {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
        }

        assertEquals(RestaurantDetailViewDataModel(
            logoDrawable = null,
            logoBitmap = bitmapMock,
            name = FAKE_NAME,
            status = FAKE_STATUS,
            phoneNumber = FAKE_PHONE_NUMBER,
            description = FAKE_DESC,
            yelpRating = FAKE_YELP_RATING,
            deliveryFee = FAKE_DELIVERY_FEE / RestaurantDetailViewModel.RATIO,
            averageRating = FAKE_AVERAGE_RATING
        ), underTests?.restaurantDetail?.value?.peekContent())
    }

    @Test
    fun `set restaurant detail with error drawable`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.just(detailDataModelMock)
        every {
            imageUtilsMock.loadLogo(imageUrl = any(), target = any())
        } answers {
            secondArg<Target>().onBitmapFailed(Exception(), errorDrawableMock)
        }
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        underTests?.setRestaurantId(FAKE_ID)

        //THEN
        verify {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
        }

        assertEquals(RestaurantDetailViewDataModel(
            logoDrawable = errorDrawableMock,
            logoBitmap = null,
            name = FAKE_NAME,
            status = FAKE_STATUS,
            phoneNumber = FAKE_PHONE_NUMBER,
            description = FAKE_DESC,
            yelpRating = FAKE_YELP_RATING,
            deliveryFee = FAKE_DELIVERY_FEE / RestaurantDetailViewModel.RATIO,
            averageRating = FAKE_AVERAGE_RATING
        ), underTests?.restaurantDetail?.value?.peekContent())
    }

    @Test
    fun `set restaurant detail with place holder`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.just(detailDataModelMock)
        every {
            imageUtilsMock.loadLogo(imageUrl = any(), target = any())
        } answers {
            secondArg<Target>().onPrepareLoad(placeHolderDrawableMock)
        }
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        underTests?.setRestaurantId(FAKE_ID)

        //THEN
        verify {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
        }

        assertEquals(RestaurantDetailViewDataModel(
            logoDrawable = placeHolderDrawableMock,
            logoBitmap = null,
            name = FAKE_NAME,
            status = FAKE_STATUS,
            phoneNumber = FAKE_PHONE_NUMBER,
            description = FAKE_DESC,
            yelpRating = FAKE_YELP_RATING,
            deliveryFee = FAKE_DELIVERY_FEE / RestaurantDetailViewModel.RATIO,
            averageRating = FAKE_AVERAGE_RATING
        ), underTests?.restaurantDetail?.value?.peekContent())
    }

    @Test
    fun `set restaurant detail but no network`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.error(retrofitExceptionMock)
        every {
            retrofitExceptionMock.getKind()
        } returns RetrofitException.Kind.NETWORK
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        underTests?.setRestaurantId(FAKE_ID)

        //THEN
        verifyOrder {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
            onErrorMock.invoke(R.string.network_error, null)
        }
        assertNull(underTests?.restaurantDetail?.value?.peekContent())
    }

    @Test
    fun `set restaurant detail but http error`() {
        //GIVEN
        `init data model`()
        every {
            getRestaurantDetailUseCaseMock.getSingle(any())
        } returns Single.error(retrofitExceptionMock)
        every {
            retrofitExceptionMock.getKind()
        } returns RetrofitException.Kind.HTTP
        every {
            retrofitExceptionMock.message
        } returns FAKE_ERROR
        underTests?.restaurantDetail?.observeForever {  }

        //WHEN
        underTests?.setRestaurantId(FAKE_ID)

        //THEN
        verifyOrder {
            getRestaurantDetailUseCaseMock.getSingle(
                input = GetRestaurantDetailUseCase.Input(restaurantId = FAKE_ID)
            )
            onErrorMock.invoke(null, FAKE_ERROR)
        }
        assertNull(underTests?.restaurantDetail?.value?.peekContent())
    }

    private fun `init data model`() {
        every {
            detailDataModelMock.cover_img_url
        } returns FAKE_COVER_IMAGE_URL
        every {
            detailDataModelMock.name
        } returns FAKE_NAME
        every {
            detailDataModelMock.status
        } returns FAKE_STATUS
        every {
            detailDataModelMock.phone_number
        } returns FAKE_PHONE_NUMBER
        every {
            detailDataModelMock.description
        } returns FAKE_DESC
        every {
            detailDataModelMock.yelp_rating
        } returns FAKE_YELP_RATING
        every {
            detailDataModelMock.delivery_fee
        } returns FAKE_DELIVERY_FEE
        every {
            detailDataModelMock.average_rating
        } returns FAKE_AVERAGE_RATING
    }
}