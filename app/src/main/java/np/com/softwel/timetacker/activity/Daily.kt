package np.com.softwel.timetacker.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import np.com.softwel.timetacker.activity.ui.theme.TimeTackerTheme
import np.com.softwel.timetacker.database.WorkingHour
import np.com.softwel.timetacker.database.WorkingHour.Companion.WORKING
import np.com.softwel.timetacker.model.WorkingHr
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Daily : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val WorkingHour = WorkingHour(this)
        WorkingHour.writableDatabase

        enableEdgeToEdge()
        setContent {
            TimeTackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Time(db = WorkingHour)
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Time(db: WorkingHour) {
    val context = LocalContext.current


    var month by remember { mutableStateOf("") }
    var clockIn by remember { mutableStateOf("") }
    var clockOut by remember { mutableStateOf("") }
    var workingHour by remember { mutableStateOf("") }

    var selectedDay by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    val dayOptions = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val dateOptions = (1..31).map { it.toString() }



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
                title = { Text("WORK COUNTER") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
//            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),


                ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {



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
                        label = "Date",
                        options = dateOptions,
                        selectedOption = selectedDate,
                        onOptionSelected = { selectedDate = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp) // padding inside the card
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween, // Put texts at opposite ends
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        val now = Calendar.getInstance().time
                                        val formatter =
                                            SimpleDateFormat("hh:mm a", Locale.getDefault())
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

                                Text(text = clockIn,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }


                            Spacer(modifier = Modifier.padding(top = 6.dp))



                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween, // Put texts at opposite ends
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        val now = Calendar.getInstance().time
                                        val formatter =
                                            SimpleDateFormat("hh:mm a", Locale.getDefault())
                                        clockOut =
                                            formatter.format(now)  // <-- update clockOut here
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                ) {
                                    Text(text = "Clock Out")
                                }

                                Text(
                                    text = clockOut,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        val (workingTime, lateBy, earlyBy, deficitExcess) = calculateWorkingHours(
                            clockIn,
                            clockOut
                        )

                        val parts = workingTime.split(":")
                        val totalMinutesWorked =
                            if (parts.size == 2) parts[0].toInt() * 60 + parts[1].toInt() else 0
                        val requiredMinutes = 7 * 60 + 30

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

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween, // Put texts at opposite ends
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Today Working Hour:",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = workingTime.ifEmpty { "--:--" },
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween, // Put texts at opposite ends
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Arrival:",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = clockIn )

                                }


                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween, // Put texts at opposite ends
                                    verticalAlignment = Alignment.CenterVertically
                                )
                                {

                                    Text(
                                        text = "Leave: ",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = clockOut
                                    )

                                }
                                    if (totalMinutesWorked == requiredMinutes) {
                                        Text(
                                            text = "Complete Shift",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    } else {
                                        if (deficitExcess.isNotEmpty()) {
                                            Text(
                                                text = deficitExcess,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (deficitExcess.startsWith("Excess")) Color(
                                                    0xFF388E3C
                                                ) else Color(0xFFD32F2F),
                                            )
                                        }
                                    }
                                workingHour = workingTime

                            }
                        }
                    }
                }
            }


            Button(onClick = {
                context.startActivity(Intent(context, Daily::class.java))
            }) { }

            /////////// SAVE BUTTON
            Column {
                Button(onClick = {
                    val data = WorkingHr(
                        id = 0,
                        day = selectedDay.toString(),
                        date = selectedDate.toString(),
                        clockIn = clockIn.toString(),
                        clockOut = clockOut.toString(),
                        workHour = workingHour.toString()
                    )

                    val contentValues = ContentValues().apply {

                        put("day", data.day)
                        put("date", data.date)
                        put("clockIn", data.clockIn)
                        put("clockOut", data.clockOut)
                        put("workHour", data.workHour)

                    }

                    val dbHelper =
                        WorkingHour(context)

                    val result = dbHelper.insertDataInTable(
                        contentValues,
                        WORKING
                    )

                    if (result) {
                        Toast.makeText(
                            context,
                            "Saved successfully!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to save data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }) {
                    Text(text = "SAVE")
                }
            }    //////

        }
    }
}