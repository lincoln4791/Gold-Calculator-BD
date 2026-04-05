package com.lincoln4791.goldcalculatorbd.uts.viewmodelTestWithMokito

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryTest {

    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository

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
