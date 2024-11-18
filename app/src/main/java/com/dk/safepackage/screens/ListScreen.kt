package com.dk.safepackage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dk.safepackage.components.BottomNavBar
import com.dk.safepackage.components.Fab
import com.dk.safepackage.components.PackageRow
import com.dk.safepackage.entities.PackageEntity
import com.dk.safepackage.ui.theme.Pinkish
import com.dk.safepackage.ui.theme.PurpleBg
import com.dk.safepackage.ui.theme.PurpleCo
import com.dk.safepackage.ui.theme.SafePackageTheme
import com.dk.safepackage.ui.theme.Yellowish
import com.dk.safepackage.viewmodel.PackageViewModel

@Composable
fun ListScreen(navController: NavController, packageViewModel: PackageViewModel) {
    var searchPackage by remember { mutableStateOf("") }
    val selectedPackages by packageViewModel.packages.collectAsState(emptyList())
    val filteredPackages =
        if (searchPackage.isEmpty()) selectedPackages
        else selectedPackages.filter {
            it.destinatario.contains(searchPackage, false)
                    || it.departamento.contains(searchPackage, false)
        }
    val packagesCount by packageViewModel.packageCount.collectAsState(0)

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PurpleBg)
                    .padding(paddingValues)
            ) {
                Text(
                    "Sin entregar: $packagesCount",
                    style = MaterialTheme.typography.titleMedium,
                    letterSpacing = 1.sp,
                    color = Pinkish,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    thickness = 0.5.dp,
                    color = Pinkish
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "SearchIcon",
                        tint = Pinkish,
                        modifier = Modifier.size(40.dp)
                    )
                    TextField(
                        value = searchPackage,
                        onValueChange = { searchPackage = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PurpleCo,
                            unfocusedContainerColor = PurpleCo,
                            cursorColor = Pinkish,
                            focusedTextColor = Pinkish,
                            unfocusedTextColor = Pinkish.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
                        .height(400.dp)
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
                        Spacer(Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredPackages) { packageEntity ->
                                if (selectedPackages.isNotEmpty()) {
                                    PackageRow(navController, packageEntity)
                                } else {
                                    Text(
                                        "No hay paquetes almacenados en la base de datos",
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

