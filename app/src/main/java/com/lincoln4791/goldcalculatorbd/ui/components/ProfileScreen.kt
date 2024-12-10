package com.lincoln4791.goldcalculatorbd.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lincoln4791.goldcalculatorbd.models.Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profile: Profile,onBackPress : ()->Unit) {
    Scaffold(
        topBar = {
            TopAppBarWithBackButton(title = "Profile"){
                //val context = LocalContext.current as Activity
                onBackPress();
            }
        },
        content = {paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = profile.role,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Skills:",
                    style = MaterialTheme.typography.headlineMedium
                )
                profile.skills.forEach { skill ->
                    Text(
                        text = "• $skill",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Experience: ${profile.experienceYears} years",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "About Me:",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = profile.about,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )

}