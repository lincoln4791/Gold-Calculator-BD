package com.lincoln4791.goldcalculatorbd.ui.components
import com.lincoln4791.goldcalculatorbd.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackButton(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24), // Replace with your desired icon
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        onBackClick()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                Text(text = title, color = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF018786) // AppBar Background
        )
    )
}
