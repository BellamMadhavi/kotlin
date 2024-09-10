package com.example.newwowzkidsworld.parents.notifications

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val attachment_url: String?,
    val created_at: String
)

interface ApiService {
    @GET("fetch_notifications.php")
    suspend fun fetchNotifications(): List<Notification>
}

class ParentNotificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParentNotificationsScreen()
        }
    }
}

@Composable
fun ParentNotificationsScreen() {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://cleverlife.life/school_app/api/notifications/") // Replace with your server address
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            scope.launch {
                try {
                    notifications = apiService.fetchNotifications()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to fetch notifications: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text("Fetch Notifications")
        }

        Spacer(modifier = Modifier.height(16.dp))

        notifications.forEach { notification ->
            NotificationCard(notification)
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = notification.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notification.message)
            Spacer(modifier = Modifier.height(4.dp))
            notification.attachment_url?.let { url ->
                BasicText(text = "Attachment: $url")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notification.created_at, color = Color.Gray)
        }
    }
}
