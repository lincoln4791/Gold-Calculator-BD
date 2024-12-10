package com.lincoln4791.goldcalculatorbd.activities

import com.lincoln4791.goldcalculatorbd.R
import android.app.Activity
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lincoln4791.goldcalculatorbd.models.getSampleProfile
import com.lincoln4791.goldcalculatorbd.ui.components.SetNavigationBarColor
import com.lincoln4791.goldcalculatorbd.ui.components.TopAppBarWithBackButton

class DeveloperProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val profile = getSampleProfile()
            MaterialTheme {
                Scaffold(
                    topBar = {
                        SetNavigationBarColor(Color(0xFF018786))
                        TopAppBarWithBackButton(title = "ডেভেলাপার প্রোফাইল") {
                            //val context = LocalContext.current as Activity
                            (this as Activity).onBackPressed();
                        }
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.dev_pic2), // Replace with your image resource ID
                                        contentDescription = "Local Image",
                                        modifier = Modifier
                                            .height(150.dp)
                                            .width(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = profile.name,
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                    Text(
                                        text = profile.role,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Contact:",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "  Email: ${profile.email}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "  Address: ${profile.address}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Skills:",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            profile.skills.forEach { skill ->
                                Text(
                                    text = " • $skill",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Experience: ${profile.experienceYears} years",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "About Me:",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = profile.about,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

