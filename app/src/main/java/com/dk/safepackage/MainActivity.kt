package com.dk.safepackage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dk.safepackage.navigation.Navigation
import com.dk.safepackage.ui.theme.SafePackageTheme
import com.dk.safepackage.viewmodel.PackageViewModel
import com.dk.safepackage.viewmodel.PackageViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)

        val packageDao = database.packageDao()
        val packageViewModel = ViewModelProvider(
            this,
            PackageViewModelFactory(packageDao)
        )[PackageViewModel::class.java]

        setContent {
            SafePackageTheme {
                Navigation(packageViewModel)
            }
        }
    }
}


