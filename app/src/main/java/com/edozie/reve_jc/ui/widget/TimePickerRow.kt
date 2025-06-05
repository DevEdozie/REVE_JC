package com.edozie.reve_jc.ui.widget

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerRow(
    startTime: LocalTime?,
    endTime: LocalTime?,
    onStartTimeSelected: (LocalTime) -> Unit,
    onEndTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = {
            showTimePicker(context) { onStartTimeSelected(it) }
        }) {
            Text(startTime?.toString() ?: "Pick Start Time")
        }

        OutlinedButton(onClick = {
            showTimePicker(context) { onEndTimeSelected(it) }
        }) {
            Text(endTime?.toString() ?: "Pick End Time")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(context: Context, onTimeSelected: (LocalTime) -> Unit) {
    val now = LocalTime.now()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(LocalTime.of(hour, minute))
        },
        now.hour, now.minute, true
    ).show()
}
