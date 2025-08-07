package np.com.softwel.timetacker.activity

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import np.com.softwel.timetacker.activity.ui.theme.TimeTackerTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
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

fun calculateWorkingHours(clockIn: String, clockOut: String): Pair<String, String> {
    if (clockIn.isEmpty() || clockOut.isEmpty()) return Pair("", "")

    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return try {
        val inTime = sdf.parse(clockIn)
        val outTime = sdf.parse(clockOut)

        if (inTime == null || outTime == null) return Pair("", "")

        var diff = outTime.time - inTime.time
        if (diff < 0) {
            // If clockOut before clockIn, add 24 hours
            diff += 24 * 60 * 60 * 1000
        }

        val workingHours = diff / (1000 * 60 * 60)
        val workingMinutes = (diff / (1000 * 60)) % 60

        val workingTimeFormatted = String.format("%02d:%02d", workingHours, workingMinutes)

        // Target working time in milliseconds (7 hrs 30 mins)
        val targetMillis = (7 * 60 + 30) * 60 * 1000L

        val diffFromTarget = diff - targetMillis

        val status = if (diffFromTarget >= 0) {
            // Overtime by diffFromTarget
            val h = diffFromTarget / (1000 * 60 * 60)
            val m = (diffFromTarget / (1000 * 60)) % 60
            "Overtime by %02d:%02d".format(h, m)
        } else {
            // Early by absolute diffFromTarget
            val earlyMillis = -diffFromTarget
            val h = earlyMillis / (1000 * 60 * 60)
            val m = (earlyMillis / (1000 * 60)) % 60
            "Early by %02d:%02d".format(h, m)
        }

        Pair(workingTimeFormatted, status)
    } catch (e: Exception) {
        Pair("", "")
    }
}
