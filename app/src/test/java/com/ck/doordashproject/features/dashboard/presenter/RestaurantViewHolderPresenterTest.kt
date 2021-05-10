package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals

class RestaurantViewHolderPresenterTest {
    companion object {
        private const val FAKE_NAME = "FAKE NAME"
        private const val FAKE_DESC = "FAKE DESC"
        private const val FAKE_STATUS = "FAKE STATUS"
        private const val FAKE_IMG = "FAKE IMG"
        private const val FAKE_ID = 1L
    }
    @MockK(relaxUnitFun = true)
    lateinit var viewMock: RestaurantViewHolderView

    @MockK(relaxUnitFun = true)
    lateinit var imageUtilsMock: ImageUtils

    @MockK(relaxed = true)
    lateinit var showRestaurantDetailMock: (Long) -> Unit

    @MockK(relaxed = true)
    lateinit var setRestaurantLikeStatusMock: (RestaurantDataModelWrapper) -> Unit

    @MockK(relaxUnitFun = true)
    lateinit var dataModelWrapperMock: RestaurantDataModelWrapper

    @MockK
    lateinit var dataModel: RestaurantDataModel

    @MockK
    lateinit var drawableMock: Drawable

    @MockK
    lateinit var bitmapMock: Bitmap

    @MockK
    lateinit var exceptionMock: Exception

    @MockK
    lateinit var loadedFrom: Picasso.LoadedFrom

    private var underTests: RestaurantViewHolderPresenterImpl? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        every {
            dataModelWrapperMock.restaurantData
        } returns dataModel
        every {
            dataModel.name
        } returns FAKE_NAME
        every {
            dataModel.description
        } returns FAKE_DESC
        every {
            dataModel.status_type
        } returns FAKE_STATUS
        every {
            dataModel.cover_img_url
        } returns FAKE_IMG
        every {
            dataModel.id
        } returns FAKE_ID
        underTests = RestaurantViewHolderPresenterImpl(
            view = viewMock,
            imageUtils = imageUtilsMock,
            showRestaurantDetail = showRestaurantDetailMock,
            setRestaurantLikeStatus = setRestaurantLikeStatusMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `on bind, liked`() {
        //GIVEN
        every {
            dataModelWrapperMock.likeStatus
        } returns LikedStatus.LIKED

        //WHEN
        underTests?.onBind(restaurantDataModel = dataModelWrapperMock)

        //THEN
        assertEquals(dataModelWrapperMock, underTests?.dataModel)
        verify {
            viewMock.setRestaurantName(FAKE_NAME)
            viewMock.setRestaurantSubTitle(FAKE_DESC)
            viewMock.setRestaurantStatus(FAKE_STATUS)
            imageUtilsMock.loadLogo(FAKE_IMG, target = underTests as Target)
            viewMock.setRestaurantLikedStatus(R.string.like_status_liked)
        }
    }

    @Test
    fun `on bind, un-liked`() {
        //GIVEN
        every {
            dataModelWrapperMock.likeStatus
        } returns LikedStatus.UN_LIKED

        //WHEN
        underTests?.onBind(restaurantDataModel = dataModelWrapperMock)

        //THEN
        assertEquals(dataModelWrapperMock, underTests?.dataModel)
        verify {
            viewMock.setRestaurantName(FAKE_NAME)
            viewMock.setRestaurantSubTitle(FAKE_DESC)
            viewMock.setRestaurantStatus(FAKE_STATUS)
            imageUtilsMock.loadLogo(FAKE_IMG, target = underTests as Target)
            viewMock.setRestaurantLikedStatus(R.string.like_status_unliked)
        }
    }

    @Test
    fun `on bind, no preference`() {
        //GIVEN
        every {
            dataModelWrapperMock.likeStatus
        } returns LikedStatus.NO_PREF

        //WHEN
        underTests?.onBind(restaurantDataModel = dataModelWrapperMock)

        //THEN
        assertEquals(dataModelWrapperMock, underTests?.dataModel)
        verify {
            viewMock.setRestaurantName(FAKE_NAME)
            viewMock.setRestaurantSubTitle(FAKE_DESC)
            viewMock.setRestaurantStatus(FAKE_STATUS)
            imageUtilsMock.loadLogo(FAKE_IMG, target = underTests as Target)
            viewMock.setRestaurantLikedStatus(R.string.like_status_no_pref)
        }
    }

    @Test
    fun `on prepare load`() {
        //WHEN
        underTests?.onPrepareLoad(null)

        //THEN
        verify {
            viewMock wasNot called
        }

        //WHEN
        underTests?.onPrepareLoad(drawableMock)

        //THEN
        verify {
            viewMock.setRestaurantLogo(drawableMock)
        }
    }

    @Test
    fun `on bitmap failed`() {
        //WHEN
        underTests?.onBitmapFailed(exceptionMock, null)

        //THEN
        verify {
            viewMock wasNot called
        }

        //WHEN
        underTests?.onBitmapFailed(exceptionMock, drawableMock)

        //THEN
        verify {
            viewMock.setRestaurantLogo(drawableMock)
        }
    }

    @Test
    fun `on bitmap loaded`() {
        //WHEN
        underTests?.onBitmapLoaded(null, loadedFrom)

        //THEN
        verify {
            viewMock wasNot called
        }

        //WHEN
        underTests?.onBitmapLoaded(bitmapMock, loadedFrom)

        //THEN
        verify {
            viewMock.setRestaurantLogo(bitmapMock)
        }
    }

    @Test
    fun `show restaurant detail`() {
        //GIVEN
        every {
            dataModelWrapperMock.likeStatus
        } returns LikedStatus.NO_PREF

        //WHEN
        underTests?.showRestaurantDetail()

        //THEN
        verify {
            showRestaurantDetailMock wasNot called
        }

        //WHEN
        underTests?.onBind(restaurantDataModel = dataModelWrapperMock)
        underTests?.showRestaurantDetail()

        //THEN
        verify {
            showRestaurantDetailMock.invoke(FAKE_ID)
        }
    }

    @Test
    fun `on like option from liked to un-liked`() {
        //GIVEN
        underTests?.dataModel = dataModelWrapperMock
        underTests?.likeStatus = LikedStatus.LIKED

        //WHEN
        underTests?.performLikeOption()

        //THEN
        verify {
            viewMock.setRestaurantLikedStatus(R.string.like_status_unliked)
            dataModelWrapperMock.likeStatus = LikedStatus.UN_LIKED
            setRestaurantLikeStatusMock.invoke(dataModelWrapperMock)
        }
    }

    @Test
    fun `on like option from un-liked to liked`() {
        //GIVEN
        underTests?.dataModel = dataModelWrapperMock
        underTests?.likeStatus = LikedStatus.UN_LIKED

        //WHEN
        underTests?.performLikeOption()

        //THEN
        verify {
            viewMock.setRestaurantLikedStatus(R.string.like_status_liked)
            dataModelWrapperMock.likeStatus = LikedStatus.LIKED
            setRestaurantLikeStatusMock.invoke(dataModelWrapperMock)
        }
    }

    @Test
    fun `on like option from no pref to liked`() {
        //GIVEN
        underTests?.dataModel = dataModelWrapperMock
        underTests?.likeStatus = LikedStatus.NO_PREF

        //WHEN
        underTests?.performLikeOption()

        //THEN
        verify {
            viewMock.setRestaurantLikedStatus(R.string.like_status_liked)
            dataModelWrapperMock.likeStatus = LikedStatus.LIKED
            setRestaurantLikeStatusMock.invoke(dataModelWrapperMock)
        }
    }
}