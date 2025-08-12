package np.com.softwel.timetacker.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import np.com.softwel.timetacker.activity.ui.theme.TimeTackerTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Locale

class Helper : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CustomDro()
                }
            }
        }
    }
}

@Composable
fun CustomDro(){}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")

fun calculateWorkingHours(clockIn: String, clockOut: String): Quadruple<String, String, String, String> {
    if (clockIn.isEmpty() || clockOut.isEmpty()) return Quadruple("", "", "", "")

    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return try {
        val inTime = sdf.parse(clockIn)
        val outTime = sdf.parse(clockOut)
        val fixedStart = sdf.parse("10:00 AM")
        val fixedEnd = sdf.parse("05:30 PM")

        if (inTime == null || outTime == null || fixedStart == null || fixedEnd == null) {
            return Quadruple("", "", "", "")
        }

        // Calculate actual worked time
        var diff = outTime.time - inTime.time
        if (diff < 0) diff += 24 * 60 * 60 * 1000 // Handle midnight crossing

        val workingHours = diff / (1000 * 60 * 60)
        val workingMinutes = (diff / (1000 * 60)) % 60
        val workingTimeFormatted = String.format("%02d:%02d", workingHours, workingMinutes)

        // --- NEW: Expected clock-out time = clock-in + 7.5 hours ---
        val requiredWorkDurationMillis = 7 * 60 * 60 * 1000 + 30 * 60 * 1000 // 7.5 hrs in ms
        val expectedClockOutTime = inTime.time + requiredWorkDurationMillis
        val expectedClockOutFormatted = sdf.format(expectedClockOutTime)

        // Check lateness
        var lateBy = ""
        var requiredEndTime = fixedEnd.time
        if (inTime.after(fixedStart)) {
            val lateMillis = inTime.time - fixedStart.time
            val h = lateMillis / (1000 * 60 * 60)
            val m = (lateMillis / (1000 * 60)) % 60
            lateBy = "Late by %02d:%02d".format(h, m)

            // Push required clock-out time forward by the lateness duration
            requiredEndTime += lateMillis
        }

        // Check early leave (compared to adjusted requiredEndTime)
        val earlyBy = if (outTime.time < requiredEndTime) {
            val earlyMillis = requiredEndTime - outTime.time
            val h = earlyMillis / (1000 * 60 * 60)
            val m = (earlyMillis / (1000 * 60)) % 60
            "Early by %02d:%02d".format(h, m)
        } else {
            ""
        }

        // Required work duration = 7.5 hours in milliseconds
        val requiredWorkMillis = (7 * 60 + 30) * 60 * 1000L

        val diffWork = diff - requiredWorkMillis
        val deficitExcess = when {
            diffWork < 0 -> {
                val deficitMillis = -diffWork
                val h = deficitMillis / (1000 * 60 * 60)
                val m = (deficitMillis / (1000 * 60)) % 60
                "Deficit by %02d:%02d".format(h, m)
            }
            diffWork > 0 -> {
                val excessMillis = diffWork
                val h = excessMillis / (1000 * 60 * 60)
                val m = (excessMillis / (1000 * 60)) % 60
                "Excess by %02d:%02d".format(h, m)
            }
            else -> ""
        }

        Quadruple(workingTimeFormatted, lateBy, earlyBy, deficitExcess)
    } catch (e: Exception) {
        Quadruple("", "", "", "")
    }
}

// Define a Quadruple class since Kotlin doesn't have built-in one:
data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
