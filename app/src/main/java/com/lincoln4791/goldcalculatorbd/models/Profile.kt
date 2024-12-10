package com.lincoln4791.goldcalculatorbd.models

data class Profile(
    val name: String,
    val role: String,
    val skills: List<String>,
    val email: String,
    val address: String,
    val experienceYears: Int,
    val about: String
)

fun getSampleProfile(): Profile {
    return Profile(
        name = "Mahmudul Karim Lincoln",
        role = "Software Engineer (Mobile)",
        skills = listOf("Android","Kotlin", "KMP", "Jetpack Compose", "Flutter","PHP Laravel"),
        email = "dev.lincoln47@gmail.com",
        address = "Dhaka, Bangladesh",
        experienceYears = 5,
        about = "Passionate about building cross-platform mobile apps with modern technologies."
    )
}
