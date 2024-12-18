package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.utils.DataDummy
import com.dicoding.picodiploma.loginwithanimation.utils.MainDispatcherRule
import com.dicoding.picodiploma.loginwithanimation.utils.PagingDataDifferTest
import com.dicoding.picodiploma.loginwithanimation.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var userPreference: UserPreference

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var userRepository: UserRepository
    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `when fetchStoriesPaging successfully, returns data`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesResponse()
        coEvery { apiService.getStories(any(), any()) } returns dummyStories

        mainViewModel.fetchStoriesPaging()
        advanceUntilIdle()

        val result = mainViewModel.storiesPaging.getOrAwaitValue()
        assertNotNull(result)
        assertEquals(10, PagingDataDifferTest<StoryItem>().apply {
            submitData(result)
        }.snapshot().size)
    }

    @Test
    fun `when fetchStoriesPaging with no data, return empty list`() = runTest {
        val emptyPagingData = DataDummy.generateEmptyStories()

        coEvery {
            apiService.getStories(any(), any())
        } returns DataDummy.generateEmptyStoriesResponse()

        mainViewModel.fetchStoriesPaging()

        advanceUntilIdle()

        val result = mainViewModel.storiesPaging.getOrAwaitValue()

        val differ = PagingDataDifferTest<StoryItem>()
        differ.submitData(result)

        advanceUntilIdle()

        assertEquals(0, differ.snapshot().size)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { userPreference.getSession() } returns flowOf(
            UserModel(
                email = "test@example.com",
                token = "dummy_token",
                isLogin = true
            )
        )

        userRepository = UserRepository(userPreference, apiService)
        mainViewModel = MainViewModel(userRepository)
    }

    @Test
    fun `logout should set logoutStatus to true`() = runTest {
        coEvery { userPreference.logout() } returns Unit

        mainViewModel.logout()

        val logoutStatus = mainViewModel.logoutStatus.getOrAwaitValue()
        assertTrue(logoutStatus)
    }

    @Test
    fun `getSession should return user session from repository`() = runTest {
        val result = mainViewModel.getSession().getOrAwaitValue()

        assertNotNull(result)
        assertEquals("test@example.com", result.email)
        assertTrue(result.isLogin)
    }
}
