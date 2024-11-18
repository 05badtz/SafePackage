package com.dk.safepackage.navigation

import com.dk.safepackage.R

sealed class Router(
    val icon: Int,
    val title: String,
    val router:String
){
    object HomeScreen:Router(R.drawable.round_home_24,"Inicio","HomeScreen")
    object RegisterScreen:Router(R.drawable.rounded_add_2_24,"Registro","RegisterScreen")
    object ListScreen:Router(R.drawable.round_format_list_bulleted_24,"Listado","ListScreen")
    object DetailScreen:Router(R.drawable.round_info_24,"Detalles","DetailScreen")
}