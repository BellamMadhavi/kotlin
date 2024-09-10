package com.example.newwowzkidsworld.parents.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.parents.profile.ProfileActivity
import com.example.newwowzkidsworld.parents.feedetails.FeeDetailsActivity
import com.example.newwowzkidsworld.parents.liveclasses.LiveClassesActivity
import com.example.newwowzkidsworld.parents.livestream.LiveStreamActivity
import com.example.newwowzkidsworld.parents.notifications.ParentNotificationsActivity
import com.example.newwowzkidsworld.parents.viewattendance.ViewAttendanceActivity
import com.example.newwowzkidsworld.util.User

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen(
                navigateToUserProfile = { navigateToUserProfile() },
                navigateToFeeDetails = { navigateToFeeDetails() },
                navigateToLiveClasses = { navigateToLiveClasses() },
                navigateToLiveStream = { navigateToLiveStream() },
                navigateToViewAttendance = { navigateToViewAttendance()},
                navigateToParentNotification={navigateToParentNotification()}
            )
        }
    }

    private fun navigateToUserProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToParentNotification() {
        val intent = Intent(this, ParentNotificationsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFeeDetails() {
        val intent = Intent(this, FeeDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLiveClasses() {
        val intent = Intent(this, LiveClassesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLiveStream() {
        val intent = Intent(this, LiveStreamActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToViewAttendance() {
        val intent = Intent(this, ViewAttendanceActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun DashboardScreen(
    navigateToUserProfile: () -> Unit,
    navigateToFeeDetails: () -> Unit,
    navigateToLiveClasses: () -> Unit,
    navigateToLiveStream: () -> Unit,
    navigateToViewAttendance: () -> Unit,
    navigateToParentNotification: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Dashboard", fontSize = 20.sp, color = Color.White)
                },
                backgroundColor = Color(0xFF6200EE),
                actions = {
                    IconButton(onClick = { /* Do something */ }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex) { index ->
                selectedIndex = index
                when (index) {
                    3 -> navigateToUserProfile()
                    // Add other cases for different indices if needed
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
                    .padding(16.dp)
            ) {
                GreetingSection(name = User.name!!)
                ActionButtons(navigateToFeeDetails, navigateToViewAttendance, navigateToParentNotification)
                LiveClassesSection(navigateToLiveClasses, navigateToLiveStream)
            }
        }
    )
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF6200EE)
    ) {
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_list_view), contentDescription = "ListView") },
            label = { Text("ListView") },
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) }
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_notes), contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) }
        )
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "User") },
            label = { Text("User") },
            selected = selectedIndex == 3,
            onClick = { onItemSelected(3) }
        )
    }
}

@Composable
fun GreetingSection(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6200EE))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hai, $name", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text(
            text = "Welcome to New Wow Kidz",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButtons(
    navigateToFeeDetails: () -> Unit,
    navigateToViewAttendance: () -> Unit,
    navigateToParentNotification: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(iconId = R.drawable.ic_take_attendance, label = "Take Attendance")
                ActionButton(iconId = R.drawable.ic_view_attendance, label = "View Attendance", onClick = navigateToViewAttendance)
                ActionButton(iconId = R.drawable.ic_marks_post, label = "Marks Post")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(iconId = R.drawable.ic_post_activities, label = "Post Activities")
                ActionButton(iconId = R.drawable.ic_home_class_works, label = "Home & class Works", onClick = navigateToParentNotification)
                ActionButton(iconId = R.drawable.ic_view_activities, label = "View Activities")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(iconId = R.drawable.baseline_calendar_month_24, label = "Fee Details", onClick = navigateToFeeDetails)
            }
        }
    }
}

@Composable
fun ActionButton(iconId: Int, label: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier.size(40.dp),
            tint = Color(0xFF6200EE)
        )
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LiveClassesSection(navigateToLiveClasses: () -> Unit, navigateToLiveStream: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Live Classes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = navigateToLiveClasses) {
                Text(text = "See All classes", fontSize = 14.sp, color = Color(0xFF6200EE))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LiveClassCard(title = "Educational information", date = "29 Mar 2024 14:31 WIB")
        Spacer(modifier = Modifier.height(8.dp))
        LiveStreamCard(title = "Live Stream", date = "29 Mar 2024 14:31 WIB", onClick = navigateToLiveStream)
    }
}

@Composable
fun LiveClassCard(title: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your actual drawable resource
                contentDescription = title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = date, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}


@Composable
fun LiveStreamCard(title: String, date: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick), // Ensure onClick is correctly set here
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your actual drawable resource
                contentDescription = title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = date, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
