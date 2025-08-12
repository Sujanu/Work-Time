package np.com.softwel.timetacker.activity.ui.theme

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.softwel.timetacker.activity.CustomDropdown
import np.com.softwel.timetacker.activity.ui.theme.ui.theme.TimeTackerTheme
import np.com.softwel.timetacker.database.WorkingHour
import np.com.softwel.timetacker.database.WorkingHour.Companion.MONTHLY
import np.com.softwel.timetacker.model.MonthData

class Month : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Monthly()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Monthly() {

    val context = LocalContext.current


    val monthOptions = listOf(
        "Baishakh",
        "Jestha",
        "Ashadh",
        "Shrawan",
        "Bhadra",
        "Ashwin",
        "Kartik",
        "Mangsir",
        "Poush",
        "Magh",
        "Falgun",
        "Chaitra"
    )

    var selectedMonth by remember { mutableStateOf<String?>(null) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Select Month",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    CustomDropdown(
                        label = "Month",
                        options = monthOptions,
                        selectedOption = selectedMonth,
                        onOptionSelected = { selectedMonth = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = {
                        val data = MonthData(
                            id = 0,
                            date = "12",
                             month  = selectedMonth?:""
                        )
                        val contentValues = ContentValues().apply {

                            put("month", data.month)
                            put("date", data.date)

                        }

                        val dbHelper =
                            WorkingHour(context)

                        val result = dbHelper.insertDataInTable(
                            contentValues,
                            MONTHLY
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
                        Text(text = "Set")
                    }

                    Text(text = "")
            }
        }
    }
}
