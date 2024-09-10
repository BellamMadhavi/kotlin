package com.example.newwowzkidsworld.parents.feedetails

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
import androidx.compose.material.icons.filled.CalendarToday
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
import com.example.newwowzkidsworld.util.User

class FeeDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeeDetailsScreen(name = User.name!!)
        }
    }
}

@Composable
fun FeeDetailsScreen(name: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Fee Details", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Fee Details", fontSize = 20.sp, color = Color.White)
                    }
                },
                backgroundColor = Color(0xFF6200EE)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
                    .padding(16.dp)
            ) {
                ProfileHeader(name = name)
                Spacer(modifier = Modifier.height(16.dp))
                FeeDetailsContent()
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
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "Hai, $name", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "Welcome to your fee details", fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun FeeDetailsContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Term Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_discount_24), // Replace with your actual discount icon resource
                    contentDescription = "Discount",
                    modifier = Modifier.size(50.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "30% Discount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
            }
            Spacer(modifier = Modifier.height(16.dp))
            TermDetails()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Total Payment Due: 125422", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
        }
    }
}

@Composable
fun TermDetails() {
    Column {
        TermItem(
            term = "Term 1",
            dueAmount = "Due Amount",
            paidAmount = "Paid Amount",
            date = "Jun 24 00: 04"
        )
        TermItem(
            term = "Term 2",
            dueAmount = "Due Amount",
            paidAmount = "Paid Amount",
            date = "Jun 25 00: 04"
        )
        TermItem(
            term = "Term 3",
            dueAmount = "Due Amount",
            paidAmount = "Paid Amount",
            date = ""
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Total Due", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Total Due", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TermItem(term: String, dueAmount: String, paidAmount: String, date: String) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24), contentDescription = null, tint = Color(0xFF6200EE))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = term, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = dueAmount, fontSize = 14.sp)
                }
            }
            if (date.isNotEmpty()) {
                Text(text = date, fontSize = 14.sp, color = Color.Gray)
            }
            Text(text = paidAmount, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
    }
}
