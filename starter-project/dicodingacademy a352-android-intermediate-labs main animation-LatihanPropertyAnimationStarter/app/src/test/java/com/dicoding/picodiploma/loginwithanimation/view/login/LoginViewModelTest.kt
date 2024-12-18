package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResult
import com.dicoding.picodiploma.loginwithanimation.utils.MainDispatcherRule
import com.dicoding.picodiploma.loginwithanimation.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when login successful, should return success result`() = runTest {
        val dummyEmail = "test@example.com"
        val dummyPassword = "password123"
        val dummyLoginResponse = LoginResponse(
            loginResult = LoginResult(
                userId = "user1",
                name = "Test User",
                token = "dummy_token"
            ),
            error = false,
            message = "Login successful"
        )

        coEvery { userRepository.loginUser(dummyEmail, dummyPassword) } returns dummyLoginResponse

        val result = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        assertTrue(result.isSuccess)
        assertEquals(dummyLoginResponse, result.getOrNull())
    }

    @Test
    fun `when login failed, should return failure result`() = runTest {
        val dummyEmail = "test@example.com"
        val dummyPassword = "wrongpassword"
        val errorException = Exception("Login failed")

        coEvery { userRepository.loginUser(dummyEmail, dummyPassword) } throws errorException

        val result = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        assertTrue(result.isFailure)
        assertEquals(errorException, result.exceptionOrNull())
    }

    @Test
    fun `saveSession should call repository saveSession`() = runTest {
        val dummyUser = UserModel(
            email = "test@example.com",
            token = "dummy_token",
            isLogin = true
        )

        coEvery { userRepository.saveSession(dummyUser) } returns Unit

        loginViewModel.saveSession(dummyUser)
        coVerify { userRepository.saveSession(dummyUser) }
    }
}