package com.ck.doordashproject.features.dashboard.usecase

import com.ck.doordashproject.base.repository.database.LikedDataDao
import com.ck.doordashproject.base.repository.database.entity.LikedDb
import com.ck.doordashproject.base.usecase.DispatchersContract
import com.ck.doordashproject.features.dashboard.data.LikedStatus
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LikeRestaurantUseCaseTests {
    companion object {
        private const val FAKE_RESTAURANT_ID = 2L
    }
    @MockK(relaxUnitFun = true)
    lateinit var dataDaoMock: LikedDataDao

    @MockK
    lateinit var inputMock: LikeRestaurantUseCase.Input

    @MockK
    lateinit var dispatchersContractMock: DispatchersContract

    private var underTests: LikeRestaurantUseCase? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        underTests = LikeRestaurantUseCase(
            dataDao = dataDaoMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `set like status`() {
        //GIVEN
        every {
            inputMock.restaurantId
        } returns FAKE_RESTAURANT_ID
        every {
            inputMock.statuts
        } returns LikedStatus.UN_LIKED
        every {
            dispatchersContractMock.IO
        } returns Dispatchers.Unconfined

        //WHEN
        runBlocking {
            underTests?.invoke(input = inputMock, dispatchersContext = dispatchersContractMock)

        }

        //THEN
        verify {
            dataDaoMock.insert(LikedDb(
                id = FAKE_RESTAURANT_ID,
                likedStatus = LikedStatus.UN_LIKED
            ))
        }
        confirmVerified(dataDaoMock)
    }
}