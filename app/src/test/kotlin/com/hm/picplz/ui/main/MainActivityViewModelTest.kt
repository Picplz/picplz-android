package com.hm.picplz.ui.main

import com.hm.picplz.common.model.User
import com.hm.picplz.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.times

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `refreshUserData calls fetchUserData again after initial load`() =
        runTest {
            val mainRepository = Mockito.mock(MainRepository::class.java)
            Mockito.`when`(mainRepository.userData).thenReturn(flowOf(User("0")))

            val viewModel = MainActivityViewModel(mainRepository)
            advanceUntilIdle()

            Mockito.verify(mainRepository, times(1)).fetchUserData()

            viewModel.refreshUserData()
            advanceUntilIdle()

            Mockito.verify(mainRepository, times(2)).fetchUserData()
        }
}
