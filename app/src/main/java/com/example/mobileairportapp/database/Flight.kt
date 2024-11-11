package com.example.mobileairportapp.database

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Flight(
    val id: Int,
    val departure_iata: String,
    val departure_name: String,
    val destination_iata: String,
    val destination_name: String,
    var isFavorite: MutableState<Boolean> = mutableStateOf(false)
)