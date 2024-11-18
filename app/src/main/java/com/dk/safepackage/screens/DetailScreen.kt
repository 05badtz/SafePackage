package com.dk.safepackage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.dk.safepackage.ui.theme.Pinkish
import com.dk.safepackage.ui.theme.PurpleBg
import com.dk.safepackage.ui.theme.PurpleCo
import com.dk.safepackage.ui.theme.Yellowish
import com.dk.safepackage.viewmodel.PackageViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreen(navController: NavController, packageId: Int, packageViewModel: PackageViewModel) {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        DetailFullDialog(
            onDismiss = {
                showDialog = false
                navController.popBackStack()
            },
            packageId = packageId,
            packageViewModel = packageViewModel
        )
    }
}

@Composable
fun DetailFullDialog(
    onDismiss: () -> Unit,
    packageId: Int,
    packageViewModel: PackageViewModel,
) {
    val packageById by packageViewModel.getPackageById(packageId).collectAsState(null)
    val dateFormat = SimpleDateFormat("dd MMM yyyy",Locale("es", "CL"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale("es", "CL"))

    packageById?.let { packageEntity ->
        val formattedDate = dateFormat.format(Date(packageEntity.fechaHoraIngreso))
        val formattedTime = timeFormat.format(Date(packageEntity.fechaHoraIngreso))
        var fechaHoraRetiro by remember { mutableLongStateOf(packageEntity.fechaHoraRetiro ?: 0L)  }

        val fechaRetiroFormat by remember(fechaHoraRetiro) {
            derivedStateOf {
                if (fechaHoraRetiro == 0L) ""
                else dateFormat.format(Date(fechaHoraRetiro))
            }
        }

        val horaRetiroFormat by remember(fechaHoraRetiro) {
            derivedStateOf {
                if (fechaHoraRetiro == 0L) ""
                else timeFormat.format(Date(fechaHoraRetiro))
            }
        }

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
                        Spacer(modifier = Modifier.height(50.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text(
                                    "Volver",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Pinkish
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                                .background(Yellowish, RoundedCornerShape(60.dp))
                                .align(Alignment.Center)
                                .padding(20.dp)

                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        packageEntity.destinatario,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PurpleCo,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                    thickness = 1.dp,
                                    color = PurpleCo
                                )
                                Spacer(Modifier.height(16.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 10.dp, end = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    InfoRow("Departamento: ", packageEntity.departamento)
                                    InfoRow("Tipo de encomienda: ", packageEntity.tipoPaquete)
                                    InfoRow("Empresa de transporte: ", packageEntity.empresaTransporte)
                                    InfoRow("Fecha de ingreso: ", formattedDate)
                                    InfoRow("Hora de ingreso: ", formattedTime)
                                    InfoRow("Comentario: ",
                                        if (packageEntity.comentario.isNullOrEmpty()) "Sin comentario"
                                        else packageEntity.comentario)
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                        thickness = 0.5.dp,
                                        color = PurpleCo
                                    )
                                    InfoRow("Estado: ",
                                        if (packageEntity.estado) "Entregado"
                                        else "Sin entregar")
                                    if (packageEntity.estado)
                                    {
                                        InfoRow("Fecha de entrega: ", fechaRetiroFormat)
                                        InfoRow("Hora de entrega: ", horaRetiroFormat)
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    val currentDate = System.currentTimeMillis()

                                    packageViewModel.updatePackageStatus(
                                        packageEntity.id,
                                        fechaHoraRetiro = currentDate,
                                        estado = true
                                    )
                                    fechaHoraRetiro = currentDate
                                },
                                enabled = !packageEntity.estado,
                                colors = ButtonColors(
                                    containerColor = PurpleCo,
                                    contentColor = Pinkish,
                                    disabledContainerColor = PurpleCo.copy(0f),
                                    disabledContentColor = Pinkish.copy(0f)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(30.dp)
                            ) {
                                Text(
                                    "Entregar encomienda",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = PurpleCo
        )
        Text(
            value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = PurpleCo
        )
    }
}