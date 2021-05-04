package com.ck.doordashproject.features.dashboard.usecase

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDetailDataModel
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.utils.RxTrampolineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class GetRestaurantDetailUseCaseTests {
    companion object {
        @ClassRule
        @JvmField
        var rxTrampolineRule = RxTrampolineRule()

        private const val FAKE_RESTAURANT_ID = 1L
    }

    @MockK
    lateinit var interactorMock: RestaurantInteractors

    @MockK
    lateinit var inputMock: GetRestaurantDetailUseCase.Input

    @MockK
    lateinit var dataModelMock: RestaurantDetailDataModel

    private var underTests: GetRestaurantDetailUseCase? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)

        underTests = GetRestaurantDetailUseCase(
            interactor = interactorMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `get restaurant detail`() {
        //GIVEN
        every {
            inputMock.restaurantId
        } returns FAKE_RESTAURANT_ID
        every {
            interactorMock.getRestaurantDetail(
                restaurantId = any()
            )
        } returns Single.just(dataModelMock)

        //WHEN
        val test = underTests?.getSingle(inputMock)?.test()

        //THEN
        test?.assertResult(dataModelMock)
        verifyOrder {
            interactorMock.getRestaurantDetail(restaurantId = FAKE_RESTAURANT_ID)
        }
        confirmVerified(interactorMock)
    }
}