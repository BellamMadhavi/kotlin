package com.example.newwowzkidsworld.teacher.postassignments

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.parents.notifications.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.util.*


class PostAssignmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostAssignmentScreen()
        }
    }
}

@Composable
fun PostAssignmentScreen() {
    var teacherId by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var homeworkType by remember { mutableStateOf("") }
    var homeworkText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val classes = listOf("Class 1", "Class 2", "Class 3")
    val homeworkTypes = listOf("Math", "Science", "History")

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://cleverlife.life/school_app/api/uploads/") // Make sure this ends with /
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiServiceFileUpload::class.java)

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fileUri = uri
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = teacherId,
            onValueChange = { teacherId = it },
            label = { Text("Teacher ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenu(
            value = className,
            items = classes,
            label = { Text("Class") },
            onValueChange = { className = it }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenu(
            value = homeworkType,
            items = homeworkTypes,
            label = { Text("Homework Type") },
            onValueChange = { homeworkType = it }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = homeworkText,
            onValueChange = { homeworkText = it },
            label = { Text("Homework") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(dueDate) { selectedDate ->
            dueDate = selectedDate
        }
        Spacer(modifier = Modifier.height(8.dp))

        FilePickerField(fileUri?.lastPathSegment ?: "") {
            filePickerLauncher.launch("*/*")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (teacherId.isEmpty() || className.isEmpty() || homeworkType.isEmpty() || homeworkText.isEmpty() || dueDate.isEmpty() || fileUri == null) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
            } else {
                val file = getFileFromUri(context, fileUri!!)
                val requestBody = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

                val teacherIdPart = teacherId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val classPart = className.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val homeworkPart = homeworkText.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val dueDatePart = dueDate.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                scope.launch {
                    try {
                        val response = apiService.uploadAssignment(
                            teacherIdPart, classPart, homeworkPart, dueDatePart, filePart
                        )
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to upload assignment: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }) {
            Text("Upload Assignment")
        }
    }
}

@Composable
fun DropdownMenu(value: String, items: List<String>, label: @Composable () -> Unit, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, Modifier.clickable {
                    expanded = true
                })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}

@Composable
fun DatePickerField(value: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    TextField(
        value = value,
        onValueChange = {},
        label = { Text("Due Date") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = null, Modifier.clickable {
                DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    onDateSelected(selectedDate)
                }, year, month, day).show()
            })
        }
    )
}

@Composable
fun FilePickerField(value: String, onFileSelected: () -> Unit) {
    TextField(
        value = value,
        onValueChange = {},
        label = { Text("File URL") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.FileUpload, contentDescription = null, Modifier.clickable {
                onFileSelected()
            })
        }
    )
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return if (filePath != null) File(filePath) else null
}
