package com.ck.doordashproject.features.dashboard.presenter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import com.ck.doordashproject.base.modules.data.RestaurantDetailDataModel
import com.ck.doordashproject.base.utils.ImageUtils
import com.ck.doordashproject.features.dashboard.view.RestaurantDetailFragmentView
import com.nhaarman.mockito_kotlin.*
import org.amshove.kluent.any
import org.amshove.kluent.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(RestaurantDetailDataModel::class, LifecycleOwner::class, ImageUtils::class, Bitmap::class)
class RestaurantDetailFragmentPresenterTests {
    companion object {
        private const val FAKE_URL = "fake url"
        private const val FAKE_NAME = "fake name"
        private const val FAKE_STATUS = "fake status"
        private const val FAKE_STATUS_TPYE = "fake status type"
        private const val FAKE_PHONE = "fake phone"
        private const val FAKE_DESC = "fake desc"
        private const val FAKE_FEE = 1L
        private const val FAKE_FEE_CONVERTED = 0.01
        private const val FAKE_FEE2 = 20L
        private const val FAKE_FEE2_CONVERTED = 0.2
        private const val FAKE_FEE3 = 100L
        private const val FAKE_FEE3_CONVERTED = 1.0
        private const val FAKE_FEE0 = 0L
        private const val FAKE_YELP_RATING = 2.0
        private const val FAKE_AVE_RATING = 3.0
    }

    private val viewMock: RestaurantDetailFragmentView = mock()
    private var imageUtilsMock: ImageUtils? = null
    private val lifecycleOwnerMock: LifecycleOwner = mock()
    private val drawableMock: Drawable = mock()
    private val bitmapMock: Bitmap = mock()

    private var dataModelMock: RestaurantDetailDataModel? = null
    private var underTest: RestaurantDetailFragmentPresenterImpl? = null

    @Before
    fun `setup tests`() {
        imageUtilsMock = PowerMockito.mock(ImageUtils::class.java)
        dataModelMock = PowerMockito.mock(RestaurantDetailDataModel::class.java)
        whenever(dataModelMock!!.cover_img_url).thenReturn(FAKE_URL)
        whenever(dataModelMock!!.name).thenReturn(FAKE_NAME)
        whenever(dataModelMock!!.status).thenReturn(FAKE_STATUS)
        whenever(dataModelMock!!.status_type).thenReturn(FAKE_STATUS_TPYE)
        whenever(dataModelMock!!.phone_number).thenReturn(FAKE_PHONE)
        whenever(dataModelMock!!.description).thenReturn(FAKE_DESC)
        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE0)
        whenever(dataModelMock!!.yelp_rating).thenReturn(FAKE_YELP_RATING)
        whenever(dataModelMock!!.average_rating).thenReturn(FAKE_AVE_RATING)
        underTest = RestaurantDetailFragmentPresenterImpl(viewMock, imageUtilsMock!!)
    }

    @After
    fun `tear down tests`() {
        underTest = null
    }

    @Test
    fun `test on start`() {
        underTest!!.onStart(lifecycleOwnerMock)
        verifyZeroInteractions(imageUtilsMock!!)

        underTest!!.setDetail(dataModelMock!!)
        underTest!!.onStart(lifecycleOwnerMock)
        verify(imageUtilsMock)!!.loadLogo(FAKE_URL, underTest!!)
    }

    @Test
    fun `test set data modle`() {
        assertThat(underTest!!.mData).isNull()
        underTest!!.setDetail(dataModelMock!!)
        assertThat(underTest!!.mData).isEqualTo(dataModelMock!!)
    }

    @Test
    fun `test on prepare load`() {
        underTest!!.setDetail(dataModelMock!!)
        underTest!!.onPrepareLoad(null)
        verify(viewMock, never()).setRestaurantLogo(any(Drawable::class))
        verify(viewMock).setRestaurantName(FAKE_NAME)
        verify(viewMock).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE0.toDouble())
        verify(viewMock).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE)
        underTest!!.onPrepareLoad(drawableMock)
        verify(viewMock).setRestaurantLogo(drawableMock)
        verify(viewMock, times(2)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(2)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(2)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(2)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE_CONVERTED)
        verify(viewMock, times(2)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(2)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE2)
        underTest!!.onPrepareLoad(drawableMock)
        verify(viewMock, times(2)).setRestaurantLogo(drawableMock)
        verify(viewMock, times(3)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(3)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(3)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(3)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE2_CONVERTED)
        verify(viewMock, times(3)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(3)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE3)
        underTest!!.onPrepareLoad(drawableMock)
        verify(viewMock, times(3)).setRestaurantLogo(drawableMock)
        verify(viewMock, times(4)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(4)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(4)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(4)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE3_CONVERTED)
        verify(viewMock, times(4)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(4)).setRestaurantAverageRating(FAKE_AVE_RATING)
    }

    @Test
    fun `test on bitmap failed`() {
        underTest!!.setDetail(dataModelMock!!)
        underTest!!.onBitmapFailed(null,null)
        verify(viewMock, never()).setRestaurantLogo(any(Drawable::class))
        verify(viewMock).setRestaurantName(FAKE_NAME)
        verify(viewMock).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE0.toDouble())
        verify(viewMock).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE)
        underTest!!.onBitmapFailed(null, drawableMock)
        verify(viewMock).setRestaurantLogo(drawableMock)
        verify(viewMock, times(2)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(2)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(2)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(2)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE_CONVERTED)
        verify(viewMock, times(2)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(2)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE2)
        underTest!!.onBitmapFailed(null, drawableMock)
        verify(viewMock, times(2)).setRestaurantLogo(drawableMock)
        verify(viewMock, times(3)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(3)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(3)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(3)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE2_CONVERTED)
        verify(viewMock, times(3)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(3)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE3)
        underTest!!.onBitmapFailed(null, drawableMock)
        verify(viewMock, times(3)).setRestaurantLogo(drawableMock)
        verify(viewMock, times(4)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(4)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(4)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(4)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE3_CONVERTED)
        verify(viewMock, times(4)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(4)).setRestaurantAverageRating(FAKE_AVE_RATING)
    }

    @Test
    fun `test on bitmap loaded`() {
        underTest!!.setDetail(dataModelMock!!)
        underTest!!.onBitmapLoaded(null, null)
        verify(viewMock, never()).setRestaurantLogo(any(Bitmap::class))
        verify(viewMock).setRestaurantName(FAKE_NAME)
        verify(viewMock).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE0.toDouble())
        verify(viewMock).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE)
        underTest!!.onBitmapLoaded(bitmapMock, null)
        verify(viewMock).setRestaurantLogo(bitmapMock)
        verify(viewMock, times(2)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(2)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(2)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(2)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE_CONVERTED)
        verify(viewMock, times(2)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(2)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE2)
        underTest!!.onBitmapLoaded(bitmapMock, null)
        verify(viewMock, times(2)).setRestaurantLogo(bitmapMock)
        verify(viewMock, times(3)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(3)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(3)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(3)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE2_CONVERTED)
        verify(viewMock, times(3)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(3)).setRestaurantAverageRating(FAKE_AVE_RATING)

        whenever(dataModelMock!!.delivery_fee).thenReturn(FAKE_FEE3)
        underTest!!.onBitmapLoaded(bitmapMock, null)
        verify(viewMock, times(3)).setRestaurantLogo(bitmapMock)
        verify(viewMock, times(4)).setRestaurantName(FAKE_NAME)
        verify(viewMock, times(4)).setRestaurantStatus(FAKE_STATUS)
        verify(viewMock, times(4)).setRestaurantPhoneNumber(FAKE_PHONE)
        verify(viewMock, times(4)).setRestaurantDescription(FAKE_DESC)
        verify(viewMock).setRestaurantDeliveryFee(FAKE_FEE3_CONVERTED)
        verify(viewMock, times(4)).setRestaurantYelpRating(FAKE_YELP_RATING)
        verify(viewMock, times(4)).setRestaurantAverageRating(FAKE_AVE_RATING)
    }
}