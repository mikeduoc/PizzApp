package com.example.pizzapp.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @DocumentId val id: String = "",
    val pizzaType: String = "",
    val ingredients: String = "",
    val totalPrice: Double = 0.0,
    val paymentMethod: String = "EFECTIVO",
    val state: OrderState = OrderState.IN_PROCESS,
    val timestamp: Long = System.currentTimeMillis(),
    
    @get:PropertyName("isEventOrder")
    @set:PropertyName("isEventOrder")
    var isEventOrder: Boolean = false,

    val eventDate: String? = null,
    val createdBy: String = "Invitado",
    val clientName: String = ""
)

enum class OrderState { IN_PROCESS, READY, DELIVERED, CANCELLED }
