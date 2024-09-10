package com.example.newwowzkidsworld.parents.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.example.newwowzkidsworld.parents.dashboard.DashboardActivity
import com.example.newwowzkidsworld.teacher.teacherdashboard.TeacherDashboardActivity
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.models.login.LoginRequest
import com.example.newwowzkidsworld.models.login.LoginResponse
import com.example.newwowzkidsworld.retrofit.retrofit.RetrofitClient
import com.example.newwowzkidsworld.util.User

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        val name = sharedPreferences.getString("name", "")
        val mobile = sharedPreferences.getString("mobile", "")
        val role = sharedPreferences.getString("role", "")

        if (userId != -1 && role != null) {
            User.updateUser(userId, name ?: "", mobile ?: "")
            when (role) {
                "parent" -> navigateToDashboardActivity()
                "teacher" -> navigateToTeacherDashboardActivity()
                else -> showLogin()
            }
        } else {
            showLogin()
        }
    }

    private fun showLogin() {
        setContent {
            LoginScreen(
                onParentLoginClick = { mobile, pin ->
                    login(mobile, pin, "parent")
                },
                onTeacherLoginClick = { mobile, pin ->
//                    login(mobile, pin, "teacher")
                    login("9948000124", "987654", "teacher")
                }
            )
        }
    }

    private fun saveUserDetails(userId: Int, name: String, mobile: String, role: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.putString("name", name)
        editor.putString("mobile", mobile)
        editor.putString("role", role)
        editor.apply()
    }

    private fun login(mobile: String, pin: String, role: String) {
        val loginRequest = LoginRequest(mobile, pin, role)
        lifecycleScope.launch {
            try {
                val response: Response<LoginResponse> = RetrofitClient.instance.login(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        loginResponse.user?.let {
                            User.updateUser(it.userId, it.name, it.mobile)
                            saveUserDetails(it.userId, it.name, it.mobile, role)
                        }
                        when (role) {
                            "parent" -> navigateToDashboardActivity()
                            "teacher" -> navigateToTeacherDashboardActivity()
                        }
                    } else {
                        showToast(loginResponse?.message ?: "Login failed")
                    }
                } else {
                    showToast("Unsuccessful response")
                }
            } catch (e: Exception) {
                showToast("Login failed: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTeacherDashboardActivity() {
        val intent = Intent(this, TeacherDashboardActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginScreen(onParentLoginClick: (String, String) -> Unit, onTeacherLoginClick: (String, String) -> Unit) {
    var selectedButton by remember { mutableStateOf("parent") }
    var mobileNumber by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var pinVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A00E0)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your actual drawable resource
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .border(2.dp, Color.White, RoundedCornerShape(20))
                    .background(Color.White, RoundedCornerShape(20))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Surface(
                        shape = RoundedCornerShape(20),
                        color = if (selectedButton == "parent") Color(0xFF6200EE) else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable { selectedButton = "parent" }
                    ) {
                        Text(
                            text = "Parent Login",
                            color = if (selectedButton == "parent") Color.White else Color(0xFF6200EE),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20),
                        color = if (selectedButton == "teacher") Color(0xFF6200EE) else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable { selectedButton = "teacher" }
                    ) {
                        Text(
                            text = "Teacher Login",
                            color = if (selectedButton == "teacher") Color.White else Color(0xFF6200EE),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to New Wow Kidz",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Mobile Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    leadingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("PIN") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val image = if (pinVisible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff

                    IconButton(onClick = { pinVisible = !pinVisible }) {
                        Icon(imageVector = image, contentDescription = if (pinVisible) "Hide PIN" else "Show PIN")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    leadingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Forgot PIN ?",
                color = Color.White,
                modifier = Modifier.align(Alignment.End),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (selectedButton == "parent") {
                        onParentLoginClick(mobileNumber, pin)
                    } else {
                        onTeacherLoginClick(mobileNumber, pin)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
