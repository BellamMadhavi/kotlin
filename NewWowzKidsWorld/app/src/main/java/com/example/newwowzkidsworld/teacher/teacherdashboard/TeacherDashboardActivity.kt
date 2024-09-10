package com.example.newwowzkidsworld.teacher.teacherdashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.parents.profile.ProfileActivity
import com.example.newwowzkidsworld.parents.liveclasses.LiveClassesActivity
import com.example.newwowzkidsworld.parents.livestream.LiveStreamActivity
import com.example.newwowzkidsworld.teacher.postassignments.PostAssignmentActivity
import com.example.newwowzkidsworld.teacher.postattendance.PostAttendanceActivity
import com.example.newwowzkidsworld.teacher.viewattendance.ViewAttendanceActivity
import com.example.newwowzkidsworld.util.User

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen(
                navigateToUserProfile = { navigateToUserProfile() },
                navigateToPostAssignments = { navigateToPostAssignments() },
                navigateToLiveClasses = { navigateToLiveClasses() },
                navigateToViewAttendance = { navigateToViewAttendance() },
                navigateToPostAttendance = { navigateToPostAttendance() }
            )
        }
    }

    private fun navigateToViewAttendance() {
        val intent = Intent(this, ViewAttendanceActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToUserProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPostAssignments() {
        val intent = Intent(this, PostAssignmentActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLiveClasses() {
        val intent = Intent(this, LiveClassesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPostAttendance() {
        val intent = Intent(this, PostAttendanceActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun DashboardScreen(
    navigateToUserProfile: () -> Unit,
    navigateToPostAssignments: () -> Unit,
    navigateToLiveClasses: () -> Unit,
    navigateToViewAttendance: () -> Unit,
    navigateToPostAttendance: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var selectedStreamIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Dashboard", fontSize = 20.sp, color = Color.White)
                },
                backgroundColor = Color(0xFF6200EE),
                actions = {
                    IconButton(onClick = { /* Do something */ }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex) { index ->
                selectedIndex = index
                when (index) {
                    1 -> navigateToPostAssignments()
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp) // Adjust height if needed to control the overlap
                ) {
                    CurvedBottomGreetingSection(name = User.name!!)
                    ActionButtons(
                        navigateToPostAssignments = navigateToPostAssignments,
                        navigateToPostAttendance = navigateToPostAttendance,
                        navigateToViewAttendance = navigateToViewAttendance,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 40.dp) // Adjust the offset to overlap with the GreetingSection
                            .padding(10.dp)
                    )
                }
                LiveClassesSection {
                    navigateToLiveClasses
                }
            }
        }
    )
}

@Composable
fun LiveCard(thumbnailResId: Int, className: String,
             sectionName: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = thumbnailResId),
            contentDescription = "Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // Adjust height as needed
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$className - $sectionName",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF6200EE)
    ) {
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) },
            alwaysShowLabel = true,
            modifier = Modifier.weight(1f)
        )
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Assignment, contentDescription = "Assignments") },
            label = { Text("Attendance") },
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) },
            alwaysShowLabel = true,
            modifier = Modifier.weight(1.5f)
        )
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Note, contentDescription = "Attendance") },
            label = { Text("Announcements") },
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) },
            alwaysShowLabel = true,
            modifier = Modifier.weight(2f)
        )
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Account") },
            label = { Text("Account") },
            selected = selectedIndex == 3,
            onClick = { onItemSelected(3) },
            alwaysShowLabel = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CurvedBottomGreetingSection(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(10.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.7f)
                quadraticBezierTo(
                    size.width / 2f, size.height,
                    size.width, size.height * 0.7f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            clipPath(path) {
                drawRect(Color(0xFF6200EE))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Hi, $name", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = "Welcome to New Wow Kidz",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionButtons(
    navigateToPostAssignments: () -> Unit,
    navigateToPostAttendance: () -> Unit,
    navigateToViewAttendance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
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
                ActionButton(iconId = R.drawable.post_attendance, label = "Post Attendance", onClick = navigateToPostAttendance)
                ActionButton(iconId = R.drawable.view_attendance, label = "View Attendance", onClick = navigateToViewAttendance)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(iconId = R.drawable.post_results, label = "Post Marks", onClick = navigateToPostAttendance)
                ActionButton(iconId = R.drawable.view_results, label = "View Marks", onClick = navigateToPostAssignments)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(iconId = R.drawable.post_announcements, label = "Post Announcements", onClick = navigateToPostAttendance)
                ActionButton(iconId = R.drawable.view_announcements, label = "View Announcements", onClick = navigateToPostAssignments)
            }
        }
    }
}

@Composable
fun ActionButton(iconId: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            modifier = Modifier.size(40.dp),
            tint = Color.Unspecified // No tint applied, original icon color will be used
        )
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LiveClassesSection(navigateToLiveClasses: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        val context = LocalContext.current

        // Title and "See All" button
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Live Classes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* Navigate to see all classes */ }) {
                Text(text = "See All Classes", fontSize = 14.sp, color = Color(0xFF6200EE))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display thumbnails in a horizontally scrollable row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between items
        ) {
            items(4) { index ->
                LiveCard(
                    thumbnailResId = when (index) {
                        0 -> R.drawable.live1
                        1 -> R.drawable.live2
                        2 -> R.drawable.live3
                        3 -> R.drawable.live4
                        else -> R.drawable.live1
                    },
                    className = when (index) {
                        0 -> "Class 1"
                        1 -> "Class 2"
                        2 -> "Class 3"
                        3 -> "Class 4"
                        else -> "Class"
                    },
                    sectionName = when (index) {
                        0 -> "Section A"
                        1 -> "Section B"
                        2 -> "Section C"
                        3 -> "Section D"
                        else -> "Section"
                    },
                    onClick = {
                        val className = when (index) {
                            0 -> "Class 1"
                            1 -> "Class 2"
                            2 -> "Class 3"
                            3 -> "Class 4"
                            else -> "Class"
                        }
                        val sectionName = when (index) {
                            0 -> "Section A"
                            1 -> "Section B"
                            2 -> "Section C"
                            3 -> "Section D"
                            else -> "Section"
                        }
                        val streamUrl = when (index) {
                            0 -> "rtsp://rtspstream:271959daf25d6873e7d84b50dafdbd8e@zephyr.rtsp.stream/pattern"
                            1 -> "rtsp://rtspstream:271959daf25d6873e7d84b50dafdbd8e@zephyr.rtsp.stream/pattern"
                            2 -> "rtsp://rtspstream:271959daf25d6873e7d84b50dafdbd8e@zephyr.rtsp.stream/pattern"
                            3 -> "rtsp://rtspstream:271959daf25d6873e7d84b50dafdbd8e@zephyr.rtsp.stream/pattern"
                            else -> "rtsp://rtspstream:271959daf25d6873e7d84b50dafdbd8e@zephyr.rtsp.stream/pattern"
                        }
                        LiveStreamActivity.start(context, className, sectionName, streamUrl, R.drawable.live1)
                    },
                    modifier = Modifier.size(width = 250.dp, height = 200.dp) // Increase the size of the thumbnail
                )
            }
        }
    }
}

