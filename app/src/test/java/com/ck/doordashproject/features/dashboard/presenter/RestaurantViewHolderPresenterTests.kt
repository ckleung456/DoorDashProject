package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.models.actions.RestaurantActionEventModel
import com.ck.doordashproject.features.dashboard.view.RestaurantViewHolderView
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.picasso.Picasso
import org.amshove.kluent.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(RestaurantActionEventModel::class, ImageUtils::class, RestaurantDataModel::class, Bitmap::class)
class RestaurantViewHolderPresenterTests {
    companion object {
        private const val FAKE_NAME = "fake name"
        private const val FAKE_DESC = "fake desc"
        private const val FAKE_STATUS_TYPE = "fake status type"
        private const val FAKE_URL = "fake url"
        private const val FAKE_ID = 1L
    }

    private val viewMock: RestaurantViewHolderView = mock()
    private var actionEventModelMock: RestaurantActionEventModel? = null
    private var imageUtilsMock: ImageUtils? = null
    private var dataModelMock: RestaurantDataModel? = null
    private val drawableMock: Drawable = mock()
    private val bitmapMock: Bitmap = mock()
    private val exceptioMock: Exception = mock()

    private var underTests: RestaurantViewHolderPresenterImpl? = null

    @Before
    fun `setup tests`() {
        actionEventModelMock = PowerMockito.mock(RestaurantActionEventModel::class.java)
        imageUtilsMock = PowerMockito.mock(ImageUtils::class.java)
        dataModelMock = PowerMockito.mock(RestaurantDataModel::class.java)
        whenever(dataModelMock!!.cover_img_url).thenReturn(FAKE_URL)
        whenever(dataModelMock!!.name).thenReturn(FAKE_NAME)
        whenever(dataModelMock!!.description).thenReturn(FAKE_DESC)
        whenever(dataModelMock!!.id).thenReturn(FAKE_ID)
        whenever(dataModelMock!!.status_type).thenReturn(FAKE_STATUS_TYPE)
        underTests = RestaurantViewHolderPresenterImpl(viewMock, actionEventModelMock!!, imageUtilsMock!!)
    }

    @After
    fun `tear down tests`() {
        underTests = null
    }

    @Test
    fun `test on bind`() {
        underTests!!.onBind(dataModelMock!!)
        verify(viewMock).setRestaurantName(FAKE_NAME)
        verify(viewMock).setRestaurantSubTitle(FAKE_DESC)
        verify(viewMock).setRestaurantStatus(FAKE_STATUS_TYPE)
        verify(imageUtilsMock)!!.loadLogo(FAKE_URL, underTests!!)
    }

    @Test
    fun `test on prepare load`() {
        underTests!!.onPrepareLoad(null)
        verifyZeroInteractions(viewMock)

        underTests!!.onPrepareLoad(drawableMock)
        verify(viewMock).setRestaurantLogo(drawableMock)
    }

    @Test
    fun `test on bitmap failed`() {
        underTests!!.onBitmapFailed(null, null)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapFailed(exceptioMock, null)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapFailed(exceptioMock, drawableMock)
        verify(viewMock).setRestaurantLogo(drawableMock)
    }

    @Test
    fun `test on bitmap loaded`() {
        underTests!!.onBitmapLoaded(null, null)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapLoaded(null, Picasso.LoadedFrom.DISK)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapLoaded(null, Picasso.LoadedFrom.MEMORY)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapLoaded(null, Picasso.LoadedFrom.NETWORK)
        verifyZeroInteractions(viewMock)

        underTests!!.onBitmapLoaded(bitmapMock, null)
        verify(viewMock).setRestaurantLogo(bitmapMock)

        underTests!!.onBitmapLoaded(bitmapMock, Picasso.LoadedFrom.DISK)
        verify(viewMock, times(2)).setRestaurantLogo(bitmapMock)

        underTests!!.onBitmapLoaded(bitmapMock, Picasso.LoadedFrom.MEMORY)
        verify(viewMock, times(3)).setRestaurantLogo(bitmapMock)

        underTests!!.onBitmapLoaded(bitmapMock, Picasso.LoadedFrom.NETWORK)
        verify(viewMock, times(4)).setRestaurantLogo(bitmapMock)
    }

    @Test
    fun `test show restaurant detail`() {
        underTests!!.showRestaurantDetail()
        verifyZeroInteractions(actionEventModelMock!!)

        underTests!!.onBind(dataModelMock!!)
        underTests!!.showRestaurantDetail()
        verify(actionEventModelMock)!!.showRestaurantDetailById(FAKE_ID)
    }
}