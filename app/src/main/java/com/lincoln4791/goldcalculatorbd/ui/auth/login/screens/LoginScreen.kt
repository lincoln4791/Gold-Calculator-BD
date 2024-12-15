package com.lincoln4791.goldcalculatorbd.ui.auth.login.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginRequest
import com.lincoln4791.goldcalculatorbd.ui.auth.login.viewmodels.MyViewModel

@Composable
fun LoginScreen(viewModel: MyViewModel = hiltViewModel(),onLogin: (String, String) -> Unit) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var showPassword = remember { mutableStateOf(false) }
    var isError = remember { mutableStateOf(false) }

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
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email TextField
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

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        imageVector = if (showPassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
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

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                    //LaunchedEffect(Unit) {
                        viewModel.login(LoginRequest(email.value, password.value))
                    //}
                    //onLogin(email.value, password.value)
                    //isError.value = false
                } else {
                    isError.value = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Signup Text
        TextButton(onClick = { /* Navigate to Sign Up */ }) {
            Text("Don't have an account? Sign up")
        }
    }
}
