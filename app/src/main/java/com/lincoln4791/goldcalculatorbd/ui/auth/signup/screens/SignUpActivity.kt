package com.lincoln4791.goldcalculatorbd.ui.auth.signup.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.lincoln4791.goldcalculatorbd.Utils
import com.lincoln4791.goldcalculatorbd.ui.components.TopAppBarWithBackButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.changeNavBarColor(this, this)
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBarWithBackButton(title = "Sign Up") {
                            //val context = LocalContext.current as Activity
                            //onBackPress();
                        }
                    },
                    content = {
                        SignUpScreen(onSignUp = { email, password ->
                            Toast.makeText(this,"email -> $email :: pass -> $password", Toast.LENGTH_SHORT).show()

                        }
                        )
                    }
                )
            }
        }
    }
}

