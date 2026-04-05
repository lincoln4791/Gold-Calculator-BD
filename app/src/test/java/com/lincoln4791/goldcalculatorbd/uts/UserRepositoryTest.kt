package com.lincoln4791.goldcalculatorbd.uts

import com.lincoln4791.goldcalculatorbd.uts.viewmodelTestWithMokito.User
import com.lincoln4791.goldcalculatorbd.uts.viewmodelTestWithMokito.UserRepository
import com.lincoln4791.goldcalculatorbd.uts.viewmodelTestWithMokito.UserService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService


    @Before
    fun setup() {
        userService = mock(UserService::class.java)
        userRepository = UserRepository(userService)
    }

    @Test
    fun testGetUsername() {
        val mockUser = User("123", "Lincoln")
        `when`(userService.getUser("123")).thenReturn(mockUser)

        val result = userRepository.getUsername("123")

        assertEquals("Lincoln", result)
    }
}
