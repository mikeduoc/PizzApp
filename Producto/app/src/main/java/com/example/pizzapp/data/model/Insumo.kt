package com.example.pizzapp.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class Insumo(
    @DocumentId val id: String = "",
    val name: String = "",
    val stock: Double = 0.0,
    val unit: String = "kg",
    val minStock: Double = 1.0
)
