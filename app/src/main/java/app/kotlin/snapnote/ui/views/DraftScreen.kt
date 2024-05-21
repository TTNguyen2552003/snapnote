package app.kotlin.snapnote.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun DraftScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var showDatePickerDialog: Boolean by remember {
            mutableStateOf(value = false)
        }
        Button(onClick = { showDatePickerDialog = true }) {
            Text(text = "Click me")
        }


        val timeFormatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())

        var time: LocalTime by remember {
            mutableStateOf(value = LocalTime.now())
        }

        val updateDate: (LocalTime) -> Unit = { localTime ->
            time = localTime
        }
        if (showDatePickerDialog) {
            TimePickerModel(
                onDismissRequest = { showDatePickerDialog = false },
                onConfirm = updateDate
            )
        }

        Text(text = timeFormatter.format(time))
    }
}