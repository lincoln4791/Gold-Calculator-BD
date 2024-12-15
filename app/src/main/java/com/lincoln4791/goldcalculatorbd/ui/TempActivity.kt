package com.lincoln4791.goldcalculatorbd.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.lincoln4791.goldcalculatorbd.Utils
import com.lincoln4791.goldcalculatorbd.ui.auth.login.screens.LoginActivity
import com.lincoln4791.goldcalculatorbd.ui.components.TopAppBarWithBackButton

class TempActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.changeNavBarColor(this, this)

        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBarWithBackButton(title = "Profile") {
                        }
                    },
                    content = {padding->
                        Column(
                            modifier = Modifier.padding(padding)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(this@TempActivity, "Clicked", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@TempActivity,LoginActivity::class.java))
                                }
                            ) {
                                Text("Login")
                            }
                        }
                    }
                )
            }
        }
    }
}