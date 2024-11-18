package com.dk.safepackage.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.dk.safepackage.R
import com.dk.safepackage.components.BottomNavBar
import com.dk.safepackage.components.Fab
import com.dk.safepackage.components.PackageRow
import com.dk.safepackage.entities.PackageEntity
import com.dk.safepackage.ui.theme.Pinkish
import com.dk.safepackage.ui.theme.PurpleBg
import com.dk.safepackage.ui.theme.PurpleCo
import com.dk.safepackage.ui.theme.Yellowish
import com.dk.safepackage.viewmodel.PackageViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController, packageViewModel: PackageViewModel) {
    val selectedImageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        selectedImageUri.value.ifEmpty { R.drawable.emptyprofile }
    )
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri.value = it.toString() }
    }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedPackages by packageViewModel.selectedPackages.collectAsState(emptyList())

    LaunchedEffect(selectedDate.value) {
        packageViewModel.getPackagesFromDate(
            selectedDate.value.atStartOfDay(
                ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PurpleBg)
                    .padding(paddingValues)
            ) {
                Image(
                        painter = painter,
                        contentDescription = "profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 30.dp)
                            .size(150.dp)
                            .clip(CircleShape)
                            .clickable { launcher.launch("image/*") }
                    )
                Spacer(Modifier.height(16.dp))
                WeekView(
                    selectedDate = selectedDate.value,
                    onDateSelected = { date->
                        selectedDate.value = date
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    thickness = 0.5.dp,
                    color = Pinkish
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "PAQUETES",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = Yellowish,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Box(
                    modifier = Modifier
                        .padding(25.dp)
                        .height(370.dp)
                        .background(Yellowish, RoundedCornerShape(40.dp))
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Destinatario",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = PurpleCo,
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(start = 20.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                "Depto",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = PurpleCo,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Estado",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = PurpleCo,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 20.dp),
                                textAlign = TextAlign.End
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            thickness = 1.dp,
                            color = PurpleCo
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (selectedPackages.isNotEmpty()) {
                                items(selectedPackages) { packageEntity ->
                                    PackageRow(navController,packageEntity)
                                }
                            } else {
                                item {
                                    Text(
                                        "No hay paquetes para la fecha seleccionada",
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PurpleCo
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = { Fab(navController) }
    )
}

@Composable
fun WeekView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val currentDate = remember { LocalDate.now() }
    val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(weekDates) { date ->
            val selected = date == selectedDate
            DayView(
                date = date,
                isSelected = selected,
                onClick = {
                    onDateSelected(date)
                }
            )
        }
    }
}

@Composable
fun DayView(
    date: LocalDate,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    val backgroundColor = if (isSelected) Pinkish else PurpleCo
    val textColor = if (isSelected) PurpleCo else Pinkish
    val dayText = remember { date.format(DateTimeFormatter.ofPattern("EEEEE d")) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable { onClick(date) }
            .padding(5.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
            .width(35.dp)
            .background(backgroundColor)

    ) {
        Text(
            dayText,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
