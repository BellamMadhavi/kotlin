package com.example.newwowzkidsworld.teacher.postattendance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.models.attendance.AttendancePostRecord
import com.example.newwowzkidsworld.models.attendance.AttendanceRequest
import com.example.newwowzkidsworld.models.attendance.Student
import com.example.newwowzkidsworld.retrofit.retrofit.ApiService
import com.example.newwowzkidsworld.retrofit.retrofit.RetrofitClient
import com.example.newwowzkidsworld.util.User
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import java.text.SimpleDateFormat
import java.util.*

class PostAttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceApp()
        }
    }
}

@Composable
fun AttendanceApp() {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    var students by remember { mutableStateOf(listOf<Student>()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var selectedClass by remember { mutableStateOf("Play") }
    var selectedSection by remember { mutableStateOf("NA") }
    var selectedTeacherId by remember { mutableStateOf("1") }
    var selectedSubject by remember { mutableStateOf("NA") }

    // Set default date to current date
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    var selectedDate by remember { mutableStateOf(currentDate) }

    val classes = listOf("Play", "Nursery", "LKG")
    val sections = listOf("NA", "A", "B")

    val context = LocalContext.current

    LaunchedEffect(selectedClass, selectedSection, selectedDate) {
        val sectionToSend = if (selectedSection == "NA") "" else selectedSection
        if (selectedClass.isNotEmpty()) {
            isLoading = true
            try {
                students = RetrofitClient.instance.getStudentsAttendanceForTakingAttendance(selectedClass, sectionToSend, selectedDate)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Dropdown for selecting class
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_class), // Replace with your actual drawable resource
                contentDescription = "Class",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Class:")
            DropdownMenu(
                items = classes,
                selectedValue = selectedClass,
                onValueChange = { selectedClass = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for selecting section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_section), // Replace with your actual drawable resource
                contentDescription = "Section",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Section:")
            DropdownMenu(
                items = sections,
                selectedValue = selectedSection,
                onValueChange = { selectedSection = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Date picker for selecting date
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar), // Replace with your actual drawable resource
                contentDescription = "Date",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Date:")
            DatePickerDialog(selectedDate) { date ->
                selectedDate = date
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(students.size) { index ->
                    StudentAttendanceRow(students[index], selectedDate, onStatusChange = { status ->
                        students = students.toMutableList().apply {
                            this[index] = this[index].copy(status = status)
                        }
                    }, onCall = { mobile ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$mobile"))
                        context.startActivity(intent)
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val msg = postAllAttendance(
                        RetrofitClient.instance,
                        students,
                        User.userId.toString(),
                        selectedSubject,
                        selectedDate
                    )
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Attendance")
        }

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colors.error)
        }
    }
}

@Composable
fun DropdownMenu(items: List<String>, selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selectedValue)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
fun DatePickerDialog(selectedDate: String, onDateSelected: (String) -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val calendar = Calendar.getInstance()
    val date = dateFormat.parse(selectedDate)
    date?.let { calendar.time = it }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(onDateSelected = {
            val newDate = dateFormat.parse(it)
            onDateSelected(dateFormat.format(newDate!!))
            showDialog = false
        }, calendar = calendar)
    }

    TextButton(onClick = { showDialog = true }) {
        Text(displayDateFormat.format(date!!))
    }
}

@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, calendar: Calendar) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    AndroidView(factory = { context ->
        DatePicker(context).apply {
            init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { _, year, month, day ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(year, month, day)
                onDateSelected(dateFormat.format(newCalendar.time))
            }
        }
    })
}


@Composable
fun StudentAttendanceRow(student: Student, selectedDate: String, onStatusChange: (String) -> Unit, onCall: (String) -> Unit) {
    var attendanceStatus by remember { mutableStateOf(student.status ?: "present") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Not Picking") }
    var customMessage by remember { mutableStateOf("") }
    val callOptions = listOf("Not Picking", "Out of Station", "Not Well")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .padding(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val kidIcon: Painter = if (student.gender.equals("Male", ignoreCase = true)) {
                        painterResource(id = R.drawable.boy) // Replace with your actual drawable resource for male
                    } else {
                        painterResource(id = R.drawable.girl) // Replace with your actual drawable resource for female
                    }

                    Image(
                        painter = kidIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray, CircleShape)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${student.roll_no} - ",
                        style = MaterialTheme.typography.body1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = student.student_name,
                        style = MaterialTheme.typography.body1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    IconButton(
                        onClick = {
                            attendanceStatus = "present"
                            onStatusChange("present")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_green_tick), // Your green tick icon
                            contentDescription = "Present",
                            tint = if (attendanceStatus == "present") Color.Green else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            attendanceStatus = "absent"
                            onStatusChange("absent")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_red_cross), // Your red cross icon
                            contentDescription = "Absent",
                            tint = if (attendanceStatus == "absent") Color.Red else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    student.mobile?.let { mobile ->
                        IconButton(onClick = {
                            onCall(mobile)
                            showDialog = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_call), // Your call icon resource
                                contentDescription = "Call",
                                tint = Color.Unspecified, // Keep original color
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            if (student.student_name.length > 10) { // Adjust the threshold as needed
                Text(
                    text = student.student_name,
                    style = MaterialTheme.typography.body1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (attendanceStatus.equals("absent", ignoreCase = true)) {
                Text(
                    text = "Comment: ${student.comment ?: "NA"}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Blue
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Call Status for ${student.student_name}") },
            text = {
                Column {
                    callOptions.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedOption == option,
                                onClick = { selectedOption = option }
                            )
                            Text(option)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        label = { Text("Custom Message") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val finalMessage = if (customMessage.isNotEmpty()) customMessage else selectedOption

                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.updateAttendanceComment(
                                studentId = student.id,
                                comment = finalMessage,
                                date = selectedDate // Pass the selected date
                            )
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Attendance updated with comment: $finalMessage", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Failed to update attendance", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

suspend fun postAllAttendance(
    apiService: ApiService,
    students: List<Student>,
    teacherId: String,
    subject: String,
    date: String
): String {
    val attendancePostRecords = students.map { student ->
        AttendancePostRecord(
            student_id = student.id,
            roll_no = student.roll_no,
            name = student.student_name,
            date = date,
            status = student.status ?: "present",
            teacher_id = teacherId,
            subject = subject,
            class_id = student.class_id // Assuming Student class has a class_id field
        )
    }

    val attendanceRequest = AttendanceRequest(
        action = "post_attendance",
        attendance_data = attendancePostRecords
    )

    return try {
        val response = apiService.postAttendance(attendanceRequest)
        response.message ?: "Attendance records added successfully"
    } catch (e: Exception) {
        e.printStackTrace()
        "Failed to submit attendance: ${e.message}"
    }
}



@Composable
fun AttendanceStatsView(stats: AttendanceStats) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Total Students: ${stats.totalStudents}", fontSize = 18.sp)
        Text(text = "Total Present: ${stats.totalPresent}", fontSize = 18.sp)
        Text(text = "Total Absent: ${stats.totalAbsent}", fontSize = 18.sp)
        Text(text = "Attendance Percentage: ${stats.attendancePercentage}%", fontSize = 18.sp)
    }
}

data class AttendanceStats(
    val totalStudents: Int,
    val totalPresent: Int,
    val totalAbsent: Int
) {
    val attendancePercentage: Int
        get() = if (totalStudents > 0) (totalPresent * 100 / totalStudents) else 0
}