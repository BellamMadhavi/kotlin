package com.example.newwowzkidsworld.parents.liveclasses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
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
import com.example.newwowzkidsworld.ui.theme.NewWowzKidsWorldTheme
import com.example.newwowzkidsworld.util.User

class LiveClassesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewWowzKidsWorldTheme {
                LiveClassesScreen(name = User.name!!)
            }
        }
    }
}

@Composable
fun LiveClassesScreen(name: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Live Classes", fontSize = 20.sp, color = Color.White) },
                backgroundColor = Color(0xFF6200EE),
                actions = {
                    IconButton(onClick = { /* Do something */ }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar() },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
                    .padding(16.dp)
            ) {
                ProfileHeader(name = name)
                CalendarView()
                Spacer(modifier = Modifier.height(16.dp))
                DatesList()
            }
        }
    )
}

@Composable
fun ProfileHeader(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6200EE))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_person_24), // Replace with your actual profile image resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "Hai, $name", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "Live Classes", fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun CalendarView() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "December 2024", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.baseline_calendar_month_24), // Replace with your actual calendar image resource
                contentDescription = "Calendar",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DatesList() {
    Column {
        DateItem(date = "06 December 2023", status = "Present", statusColor = Color.Green)
        DateItem(date = "07 December 2023", status = "Holiday", statusColor = Color.Blue)
        DateItem(date = "08 December 2023", status = "Present", statusColor = Color.Green)
        DateItem(date = "09 December 2023", status = "Leave", statusColor = Color.Red)
        DateItem(date = "10 December 2023", status = "Present", statusColor = Color.Green)
    }
}

@Composable
fun DateItem(date: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = date, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = status,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor,
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF6200EE)
    ) {
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { /* Handle navigation */ }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_list_view), contentDescription = "List View") },
            label = { Text("List View") },
            selected = false,
            onClick = { /* Handle navigation */ }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_notes), contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = false,
            onClick = { /* Handle navigation */ }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "User") },
            label = { Text("User") },
            selected = false,
            onClick = { /* Handle navigation */ }
        )
    }
}
