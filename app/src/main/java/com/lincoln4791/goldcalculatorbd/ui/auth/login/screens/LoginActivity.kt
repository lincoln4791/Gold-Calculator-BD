package com.lincoln4791.goldcalculatorbd.ui.auth.login.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.lincoln4791.goldcalculatorbd.Utils
import com.lincoln4791.goldcalculatorbd.ui.components.TopAppBarWithBackButton

class LoginActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.changeNavBarColor(this, this)
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBarWithBackButton(title = "Profile") {
                            //val context = LocalContext.current as Activity
                            //onBackPress();
                        }
                    },
                    content = {
                        LoginScreen(onLogin = { email, password ->
                            Toast.makeText(this,"email -> $email :: pass -> $password", Toast.LENGTH_SHORT).show()

                        }
                        )
                    }
                )
            }
        }
    }
}

