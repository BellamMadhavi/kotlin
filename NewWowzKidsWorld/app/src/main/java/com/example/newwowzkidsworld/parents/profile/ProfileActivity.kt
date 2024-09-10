package com.example.newwowzkidsworld.parents.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.parents.login.LoginActivity
import com.example.newwowzkidsworld.util.User

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen { logout() }
        }
    }

    private fun logout() {
        // Perform logout logic, such as clearing user data and redirecting to the login screen
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var isLoggingOut by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", fontSize = 20.sp, color = Color.White) },
                backgroundColor = Color(0xFF6200EE),
                actions = {
                    IconButton(onClick = { /* Do something */ }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
            ) {
                ProfileHeader(name = User.name!!)
                ProfileOptions { action ->
                    if (action == "Sign Out") {
                        isLoggingOut = true
                    }
                }
            }
        }
    )

    if (isLoggingOut) {
        LaunchedEffect(Unit) {
            onLogout()
        }
    }
}

@Composable
fun ProfileHeader(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6200EE))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile), // Replace with your actual profile image resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Hai, $name", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileOptions(onOptionSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column {
            ProfileOptionItem(iconId = R.drawable.ic_edit_profile, label = "Edit Profile", onOptionSelected = onOptionSelected)
            Divider()
            ProfileOptionItem(iconId = R.drawable.baseline_password_24, label = "Change PIN", onOptionSelected = onOptionSelected)
            Divider()
            ProfileOptionItem(iconId = R.drawable.baseline_logout_24, label = "Sign Out", onOptionSelected = onOptionSelected)
        }
    }
}

@Composable
fun ProfileOptionItem(iconId: Int, label: String, onOptionSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onOptionSelected(label) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified // Keep original color
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Arrow",
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
    }
}
