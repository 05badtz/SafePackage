package com.dk.safepackage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dk.safepackage.entities.PackageEntity
import com.dk.safepackage.navigation.Router
import com.dk.safepackage.ui.theme.PurpleCo

@Composable
fun PackageRow(navController: NavController,packageEntity: PackageEntity){
    Row(
        Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Router.DetailScreen.router}?packageId=${packageEntity.id}")
            },
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier.weight(3f),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                packageEntity.destinatario,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleCo
            )
        }
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                packageEntity.departamento,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleCo
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                if (packageEntity.estado) "E" else "S/E",
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleCo
            )
        }

    }
}