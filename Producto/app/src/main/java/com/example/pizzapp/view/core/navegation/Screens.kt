package com.example.pizzapp.view.core.navegation

import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
data class Home(val isGuest: Boolean)

@Serializable
object Login

@Serializable
object Registro

@Serializable
data class OrdersList(val isGuest: Boolean)

@Serializable
object CreateOrder

@Serializable
object InventoryList

@Serializable
object Ventas

@Serializable
object MenuPizzas

@Serializable
object Evento
