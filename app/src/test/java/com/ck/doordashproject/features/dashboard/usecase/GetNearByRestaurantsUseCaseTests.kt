package com.ck.doordashproject.features.dashboard.usecase

import com.ck.doordashproject.base.models.data.restaurants.RestaurantDataModel
import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.repository.database.entity.LikedDb
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import com.ck.doordashproject.features.dashboard.data.RestaurantDataModelWrapper
import com.ck.doordashproject.features.dashboard.models.repository.network.RestaurantInteractors
import com.ck.doordashproject.features.utils.RxTrampolineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class GetNearByRestaurantsUseCaseTests {
    companion object {
        @ClassRule
        @JvmField
        var rxTrampolineRule = RxTrampolineRule()

        private const val FAKE_LAT = 1.0f
        private const val FAKE_LNG = 2.0f
        private const val FAKE_ID_1 = 1L
        private const val FAKE_ID_2 = 2L
    }

    @MockK
    lateinit var interactorMock: RestaurantInteractors

    @MockK
    lateinit var likeDaoMock: LikedDataDao

    @MockK
    lateinit var inputMock: GetNearByRestaurantsUseCase.Input

    @MockK
    lateinit var likedDbMock: LikedDb

    @MockK
    lateinit var dataModel1Mock: RestaurantDataModel

    @MockK
    lateinit var dataModel2Mock: RestaurantDataModel

    private var underTests: GetNearByRestaurantsUseCase? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        underTests = GetNearByRestaurantsUseCase(
            interactor = interactorMock,
            likedDataDao = likeDaoMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `get nearby restaurants`() {
        //GIVEN
        every {
            inputMock.lat
        } returns FAKE_LAT
        every {
            inputMock.lng
        } returns FAKE_LNG
        every {
            interactorMock.getRestaurantNearBy(
                lat = any(),
                lng = any()
            )
        } returns Single.just(listOf(dataModel1Mock, dataModel2Mock))
        every {
            dataModel1Mock.id
        } returns FAKE_ID_1
        every {
            dataModel2Mock.id
        } returns FAKE_ID_2
        every {
            likeDaoMock.getById(FAKE_ID_1)
        } returns likedDbMock
        every {
            likeDaoMock.getById(FAKE_ID_2)
        } returns null
        every {
            likedDbMock.likedStatus
        } returns LikedStatus.LIKED

        //WHEN
        val test = underTests?.getSingle(inputMock)?.test()

        //THEN
        test?.assertResult(listOf(
            RestaurantDataModelWrapper(dataModel1Mock, LikedStatus.LIKED),
            RestaurantDataModelWrapper(dataModel2Mock, LikedStatus.NO_PREF)
        ))
        verifyOrder {
            interactorMock.getRestaurantNearBy(
                lat = FAKE_LAT,
                lng = FAKE_LNG
            )
            likeDaoMock.getById(FAKE_ID_1)
            likedDbMock.likedStatus
            likeDaoMock.getById(FAKE_ID_2)
        }
        confirmVerified(interactorMock, likeDaoMock)
    }
}