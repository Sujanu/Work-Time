package np.com.softwel.timetacker.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.softwel.timetacker.activity.ui.theme.TimeTackerTheme
import np.com.softwel.timetacker.database.dbHelper
import np.com.softwel.timetacker.model.WorkingHr
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Daily : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = dbHelper(this)
        dbHelper.writableDatabase

        enableEdgeToEdge()
        setContent {
            TimeTackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Time()
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Time() {
    val context = LocalContext.current

    var month by remember { mutableStateOf("") }
    var clockIn by remember { mutableStateOf("") }
    var clockOut by remember { mutableStateOf("") }

    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    val dayOptions = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val dateOptions = (1 .. 31).map { it.toString() }

    val monthOptions = listOf(
        "Baishakh",  // बैशाख
        "Jestha",    // जेठ
        "Ashadh",    // असार
        "Shrawan",   // साउन
        "Bhadra",    // भदौ
        "Ashwin",    // असोज
        "Kartik",    // कार्तिक
        "Mangsir",   // मंसिर
        "Poush",     // पुष
        "Magh",      // माघ
        "Falgun",    // फागुन
        "Chaitra"    // चैत
    )

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    var workingHours by remember { mutableStateOf("") }

    var workingStatus by remember { mutableStateOf("") }

    LaunchedEffect(clockIn, clockOut) {
        val (hours, status) = calculateWorkingHours(clockIn, clockOut)
        workingHours = hours
        workingStatus = status
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TIME COUNTER") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    CustomDropdown(
                        label = "Month",
                        options = monthOptions,
                        selectedOption = selectedMonth,
                        onOptionSelected = { selectedMonth = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    CustomDropdown(
                        label = "Day",
                        options = dayOptions,
                        selectedOption = selectedDay,
                        onOptionSelected = { selectedDay = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    CustomDropdown(
                        label = "Dates",
                        options = dateOptions,
                        selectedOption = selectedDate,
                        onOptionSelected = { selectedDate = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    Box() {

                        Column {

                            Button(
                                onClick = {
                                    val now = Calendar.getInstance().time
                                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                                    clockIn =
                                        formatter.format(now)  // Set current time on button click
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(text = "Clock In")
                            }

                            OutlinedTextField(
                                value = clockIn,
                                onValueChange = { clockIn = it },
                                label = { Text("Clock In") },
                                modifier = Modifier
                            )

                            Spacer(modifier = Modifier.padding(top = 6.dp))

                            Button(
                                onClick = {
                                    val now = Calendar.getInstance().time
                                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                                    clockOut = formatter.format(now)  // <-- update clockOut here
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(text = "Clock Out")
                            }

                            OutlinedTextField(
                                value = clockOut,
                                onValueChange = { clockOut = it },
                                label = { Text("Clock Out") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Working Time",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "$clockIn - $clockOut",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Column {
                                Text(
                                    text = "Today Working Hour:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = workingHours.ifEmpty { "--:--" },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.align(Alignment.End)
                                )

                                if (workingStatus.isNotEmpty()) {
                                    Text(
                                        text = workingStatus,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (workingStatus.startsWith("Overtime")) Color(0xFF388E3C) else Color(0xFFD32F2F),
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }

                            }
                        }
                    }

                    Button(onClick = {
                        val workingHr = WorkingHr(
                            id = 0,
                            month = selectedMonth,
                            day = selectedDay,
                            date = selectedDate,
                            clockIn = clockIn,
                            clockOut = clockOut
                        )

                        // Get context and open DB helper
                        val context = LocalContext.current
                        val dbHelper = dbHelper(context)

                        // Insert data
                        val result = dbHelper.insertWorkingHour(workingHr)

                        if (result != -1L) {
                            Toast.makeText(context, "Saved Successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Save Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(text = "SAVE")
                    }


                }
            }
        }
    }
}
