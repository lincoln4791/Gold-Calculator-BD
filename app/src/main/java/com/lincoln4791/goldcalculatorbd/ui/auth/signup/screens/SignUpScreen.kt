package com.lincoln4791.goldcalculatorbd.ui.auth.signup.screens
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginRequest
import com.lincoln4791.goldcalculatorbd.ui.auth.login.screens.LoginActivity
import com.lincoln4791.goldcalculatorbd.ui.auth.login.screens.LoginScreen
import com.lincoln4791.goldcalculatorbd.ui.auth.viewModels.AuthViewModel

@Composable
fun SignUpScreen(viewModel: AuthViewModel = hiltViewModel(), onSignUp: (String, String) -> Unit) {
    var name = remember { mutableStateOf("") }
    var phone = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var cPassword = remember { mutableStateOf("") }
    var isError = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsState()

    when (uiState) {
       /* null -> Text("Loading...")
        is Result.Success -> Text("Data: ${uiState.getOrNull()?.toString()}")
        is Result.Failure -> Text("Error: ${uiState.exceptionOrNull()?.message}")*/
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            singleLine = true,
            isError = isError.value && name.value.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError.value && name.value.isEmpty()) {
            Text(
                text = "Name cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Email TextField
        OutlinedTextField(
            value = phone.value,
            onValueChange = { phone.value = it },
            label = { Text("Phone") },
            singleLine = true,
            isError = isError.value && phone.value.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError.value && phone.value.isEmpty()) {
            Text(
                text = "Email cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            singleLine = true,
            isError = isError.value && email.value.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError.value && email.value.isEmpty()) {
            Text(
                text = "Email cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Password TextField
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            singleLine = true,
            isError = isError.value && password.value.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError.value && password.value.isEmpty()) {
            Text(
                text = "Password cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Password TextField
        OutlinedTextField(
            value = cPassword.value,
            onValueChange = { cPassword.value = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            isError = isError.value && cPassword.value.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError.value && cPassword.value.isEmpty()) {
            Text(
                text = "Confirm Password cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                if (email.value.isNotEmpty() && password.value.isNotEmpty() && cPassword.value==password.value) {
                    viewModel.login(LoginRequest(email.value, password.value))
                } else {
                    isError.value = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Signup Text
        TextButton(onClick = {
            (context as SignUpActivity).finish()
        }) {
            Text("Already have account? Login")
        }
    }
}
