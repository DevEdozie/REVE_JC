package com.edozie.reve_jc.ui.widget

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerRow(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = {
            showDatePicker(context) { date -> onStartDateSelected(date) }
        }) {
            Text(startDate?.toString() ?: "Pick Start Date")
        }

        OutlinedButton(onClick = {
            showDatePicker(context) { date -> onEndDateSelected(date) }
        }) {
            Text(endDate?.toString() ?: "Pick End Date")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(context: Context, onDateSelected: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val picker = DatePickerDialog(
        context,
        { _, year, month, day ->
            onDateSelected(LocalDate.of(year, month + 1, day))
        },
        today.year, today.monthValue - 1, today.dayOfMonth
    )
    picker.show()
}

