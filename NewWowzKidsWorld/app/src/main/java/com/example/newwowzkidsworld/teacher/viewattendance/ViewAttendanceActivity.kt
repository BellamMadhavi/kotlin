package com.example.newwowzkidsworld.teacher.viewattendance

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.newwowzkidsworld.R
import com.example.newwowzkidsworld.models.attendance.AttendanceGetRecord
import com.example.newwowzkidsworld.retrofit.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ViewAttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewAttendanceApp(
                onShareReport = { attendanceRecords ->
                    shareAttendanceReport(this, attendanceRecords)
                }
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed with sharing the report
            // You might need to store attendanceRecords somewhere and use it here
        } else {
            Toast.makeText(this, "Permission denied to write to external storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareAttendanceReport(context: Context, attendanceRecords: List<AttendanceGetRecord>) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        var yPosition = 50

        attendanceRecords.forEach { record ->
            canvas.drawText("Roll No: ${record.roll_no}, Name: ${record.student_name}, Status: ${record.status}, Comment: ${record.comment}", 10f, yPosition.toFloat(), paint)
            yPosition += 20
        }

        pdfDocument.finishPage(page)

        val file = File(Environment.getExternalStorageDirectory(), "attendance_report.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()

        val uri = Uri.fromFile(file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage("com.whatsapp")
        }
        context.startActivity(shareIntent)
    }
}

@Composable
fun ViewAttendanceApp(onShareReport: (List<AttendanceGetRecord>) -> Unit) {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    var attendancePostRecords by remember { mutableStateOf(listOf<AttendanceGetRecord>()) }
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var selectedClass by remember { mutableStateOf("Play") }
    var selectedSection by remember { mutableStateOf("NA") }
    var selectedSubject by remember { mutableStateOf("NA") }

    // Set default date to current date
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    var selectedDate by remember { mutableStateOf(currentDate) }

    val classes = listOf("Play", "Nursery", "LKG")
    val sections = listOf("NA", "A", "B")

    // Fetch attendance records when selectedClass, selectedSection, or selectedDate changes
    LaunchedEffect(selectedClass, selectedSection, selectedDate) {
        val sectionToSend = if (selectedSection == "NA") "" else selectedSection
        try {
            isLoading = true
            attendancePostRecords = RetrofitClient.instance.getAttendanceExistingRecords(selectedClass, sectionToSend, selectedSubject, selectedDate)
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "Failed to load data: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        // Dropdown for selecting class
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_class), // Replace with your actual drawable resource
                contentDescription = "Class",
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Class:", fontSize = 18.sp)
            DropdownMenu(
                items = classes,
                selectedValue = selectedClass,
                onValueChange = { selectedClass = it }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Dropdown for selecting section
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_section), // Replace with your actual drawable resource
                contentDescription = "Section",
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Section:", fontSize = 18.sp)
            DropdownMenu(
                items = sections,
                selectedValue = selectedSection,
                onValueChange = { selectedSection = it }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Date picker for selecting date
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar), // Replace with your actual drawable resource
                contentDescription = "Date",
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified // Keep original color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Date:", fontSize = 18.sp)
            DatePickerDialog(selectedDate) { date ->
                selectedDate = date
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Count of Total: Present, Absent, and Total Students
        val totalStudents = attendancePostRecords.size
        val totalPresent = attendancePostRecords.count { it.status.equals("present", ignoreCase = true) }
        val totalAbsent = attendancePostRecords.count { it.status.equals("absent", ignoreCase = true) }
        val attendancePercentage = if (totalStudents > 0) (totalPresent * 100 / totalStudents) else 0

        Text(text = "Total Students: $totalStudents", fontSize = 18.sp)
        Text(text = "Total Present: $totalPresent", fontSize = 18.sp)
        Text(text = "Total Absent: $totalAbsent", fontSize = 18.sp)
        Text(text = "Attendance Percentage: $attendancePercentage%", fontSize = 18.sp)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colors.error, fontSize = 16.sp)
            }

            LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                items(attendancePostRecords) { record ->
                    AttendanceRecordRow(record)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onShareReport(attendancePostRecords) }) {
                Text("Share Report", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun DropdownMenu(items: List<String>, selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selectedValue, fontSize = 16.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded = false
                }) {
                    Text(item, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun DatePickerDialog(selectedDate: String, onDateSelected: (String) -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val calendar = Calendar.getInstance()
    val date = dateFormat.parse(selectedDate)
    date?.let { calendar.time = it }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(onDateSelected = {
            onDateSelected(it)
            showDialog = false
        }, calendar = calendar)
    }

    TextButton(onClick = { showDialog = true }) {
        Text(selectedDate, fontSize = 16.sp)
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
fun AttendanceRecordRow(record: AttendanceGetRecord) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .shadow(4.dp)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val kidIcon: Painter = if (record.gender.equals("Male", ignoreCase = true)) {
                painterResource(id = R.drawable.boy) // Replace with your actual drawable resource for male
            } else {
                painterResource(id = R.drawable.girl) // Replace with your actual drawable resource for female
            }

            Image(
                painter = kidIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray, CircleShape)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(text = "${record.roll_no} - ${record.student_name}", style = MaterialTheme.typography.body1, fontSize = 20.sp)
                if (record.status.equals("absent", ignoreCase = true)) {
                    Text(text = "Comment: ${record.comment}", style = MaterialTheme.typography.body2, color = Color.Blue, fontSize = 18.sp)
                }
            }
            if (record.status.equals("present", ignoreCase = true)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_tick), // Your green tick icon
                    contentDescription = "Present",
                    tint = Color.Unspecified, // Keep original color
                    modifier = Modifier.size(32.dp)
                )
            } else if (record.status.equals("absent", ignoreCase = true)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_red_cross), // Your red cross icon
                    contentDescription = "Absent",
                    tint = Color.Unspecified, // Keep original color
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
    Divider()
}
