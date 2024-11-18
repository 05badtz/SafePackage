package com.dk.safepackage.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.dk.safepackage.entities.PackageEntity
import com.dk.safepackage.ui.theme.Pinkish
import com.dk.safepackage.ui.theme.PurpleBg
import com.dk.safepackage.ui.theme.PurpleCo
import com.dk.safepackage.ui.theme.Yellowish
import com.dk.safepackage.viewmodel.PackageViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun RegisterScreen(navController: NavController, packageViewModel: PackageViewModel) {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        FullScreenDialog(
            onDismiss = {
                showDialog = false
                navController.popBackStack()
            },
            packageViewModel = packageViewModel
        )
    }
}

@Composable
fun FullScreenDialog(
    onDismiss: () -> Unit,
    packageViewModel: PackageViewModel
) {
    val context = LocalContext.current

    var nombreDestinatario by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var transporte by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var fechaIngreso by remember { mutableStateOf(Date()) }
    var horaIngreso by remember { mutableStateOf(Date()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleBg)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                "Cancelar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Pinkish
                            )
                        }
                        TextButton(onClick = {
                            if(nombreDestinatario.isBlank() ||
                                departamento.isBlank() ||
                                tipo.isBlank() ||
                                transporte.isBlank()){
                                Toast.makeText(
                                    context,
                                    "Se deben ingresar datos en los campos obligatorios",
                                    Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val calendar = Calendar.getInstance(
                                    TimeZone.getTimeZone("Chile/Continental")
                                ).apply {
                                    time = fechaIngreso
                                    val hourMinuteCalendar =
                                        Calendar.getInstance().apply { time = horaIngreso }
                                    set(Calendar.HOUR_OF_DAY, hourMinuteCalendar.get(Calendar.HOUR_OF_DAY))
                                    set(Calendar.MINUTE, hourMinuteCalendar.get(Calendar.MINUTE))
                                }
                                calendar.timeInMillis
                                val combinedDate = calendar.time
                                val packageEntity = PackageEntity(
                                    destinatario = nombreDestinatario,
                                    departamento = departamento,
                                    tipoPaquete = tipo,
                                    fechaHoraIngreso = combinedDate.time,
                                    fechaHoraRetiro = null,
                                    empresaTransporte = transporte,
                                    estado = false,
                                    comentario = comentario.ifBlank { null }
                                )
                                packageViewModel.insertPackage(packageEntity)
                                onDismiss()
                            }
                        }) {
                            Text(
                                "Guardar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Pinkish
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Nombre Destinatario (*)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Yellowish,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    TextField(
                        value = nombreDestinatario,
                        onValueChange = { nombreDestinatario = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PurpleCo,
                            unfocusedContainerColor = PurpleCo,
                            cursorColor = Pinkish,
                            focusedTextColor = Pinkish,
                            unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Departamento (*)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Yellowish,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    TextField(
                        value = departamento,
                        onValueChange = { departamento = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PurpleCo,
                            unfocusedContainerColor = PurpleCo,
                            cursorColor = Pinkish,
                            focusedTextColor = Pinkish,
                            unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Tipo de paquete (*)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Yellowish,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    DropdownField(
                        value = tipo,
                        options = listOf("Sobre", "Paquete", "Caja"),
                        onValueChange = { tipo = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Fecha de ingreso (*)",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Yellowish,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Text(
                            "Hora de ingreso (*)",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Yellowish,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DatePickerField(
                            date = fechaIngreso,
                            onDateSelected = { fechaIngreso = it }
                        )
                        TimePickerField(
                            time = horaIngreso,
                            onTimeSelected = { horaIngreso = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Empresa de transporte (*)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Yellowish,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    DropdownField(
                        value = transporte,
                        options = listOf("BlueExpress", "CorreosChile", "ChileExpress", "Starken"),
                        onValueChange = { transporte = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Comentario (Opcional)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Yellowish,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    TextField(
                        value = comentario,
                        onValueChange = { if (it.length <= 100) comentario = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PurpleCo,
                            unfocusedContainerColor = PurpleCo,
                            cursorColor = Pinkish,
                            focusedTextColor = Pinkish,
                            unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(1f)
                    )
                }
            }

        }
    }
}

@Composable
fun DropdownField(
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextField(
            value = value,
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PurpleCo,
                unfocusedContainerColor = PurpleCo,
                cursorColor = Pinkish,
                focusedTextColor = Pinkish,
                unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Pinkish
                    )
                }
            },
            modifier = Modifier.width(160.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(PurpleCo)
        ) {
            options.forEach { options ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(options)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(textColor = Pinkish),
                    text = {
                        Text(
                            text = options,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    date: Date,
    onDateSelected: (Date) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    /*val calendar = remember { Calendar.getInstance(TimeZone.getTimeZone("America/Santiago")) }
    calendar.time = date
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("es","CL")).apply {
        this.timeZone = TimeZone.getTimeZone("America/Santiago")
    }
    var formattedDate by remember { mutableStateOf(dateFormat.format(date)) }*/

    val calendar = remember { Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental")) }
    calendar.time = date
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "CL")).apply {
        this.timeZone = TimeZone.getTimeZone("Chile/Continental")
    }
    var formattedDate by remember { mutableStateOf(dateFormat.format(date)) }

    Box {
        TextField(
            value = formattedDate,
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PurpleCo,
                unfocusedContainerColor = PurpleCo,
                cursorColor = Pinkish,
                focusedTextColor = Pinkish,
                unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = !showDialog }) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Pinkish
                    )
                }
            },
            modifier = Modifier.width(200.dp)
        )
        if (showDialog) {
            val state = rememberDatePickerState(initialSelectedDateMillis = date.time)
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            state.selectedDateMillis?.let {
                                val selectedCalendar =
                                    Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental")).apply {
                                        timeInMillis = it
                                    }
                                val selectedDate = selectedCalendar.time
                                formattedDate = dateFormat.format(selectedDate)
                                onDateSelected(selectedDate)
                            }
                            showDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PurpleCo
                        )
                    ) {
                        Text(
                            "Aceptar",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PurpleCo
                        )
                    ) {
                        Text(
                            "Cancelar",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            ) { DatePicker(state) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    time: Date,
    onTimeSelected: (Date) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    calendar.time = time
    val timeFormat = SimpleDateFormat("HH:mm", Locale("es", "CL")).apply {
        this.timeZone = TimeZone.getTimeZone("Chile/Continental")
    }
    var formattedTime by remember { mutableStateOf(timeFormat.format(calendar.time)) }

    Box {
        TextField(
            value = formattedTime,
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PurpleCo,
                unfocusedContainerColor = PurpleCo,
                cursorColor = Pinkish,
                focusedTextColor = Pinkish,
                unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = !showDialog }) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Pinkish
                    )
                }
            },
            modifier = Modifier.width(120.dp)
        )
    }
    if (showDialog) {
        TimeDialog(
            onConfirm = { timePickerState ->
                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }
                onTimeSelected(calendar.time)
                formattedTime = timeFormat.format(calendar.time)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    TimeAlertDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },
    ) {
        TimePicker(timePickerState)
    }
}

@Composable
fun TimeAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    "Aceptar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "Cancelar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        text = { content() }
    )
}
