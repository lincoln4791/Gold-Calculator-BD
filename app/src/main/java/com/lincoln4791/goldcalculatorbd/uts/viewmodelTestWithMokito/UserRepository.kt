package com.lincoln4791.goldcalculatorbd.uts.viewmodelTestWithMokito;

class UserRepository(private val userService: UserService) {
    fun getUsername(userId: String): String {
        return userService.getUser(userId).name
    }
}