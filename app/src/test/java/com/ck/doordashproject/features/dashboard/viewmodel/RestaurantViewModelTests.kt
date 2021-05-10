package com.ck.doordashproject.features.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ck.doordashproject.base.utils.Event
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RestaurantViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var savedStateHandleMock: SavedStateHandle

    private var underTests: RestaurantViewModel? = null

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        every {
            savedStateHandleMock.getLiveData<Event<State>>(RestaurantViewModel.EXTRA_CURRENT_STATE)
        } returns MutableLiveData()
        underTests = RestaurantViewModel(
            savedStateHandle = savedStateHandleMock
        )
    }

    @After
    fun `tear down`() {
        underTests = null
        unmockkAll()
    }

    @Test
    fun `set state`() {
        //GIVEN
        assertEquals(null, underTests?.dashboardStateMachine?.value)

        //WHEN
        underTests?.setState(State.List)

        //THEN
        assertEquals(State.List, underTests?.dashboardStateMachine?.value?.peekContent())
    }
}