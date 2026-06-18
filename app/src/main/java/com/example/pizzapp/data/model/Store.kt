package com.example.pizzapp.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class Store(
    @DocumentId val id: String = "",
    val name: String = "",
    val category: StoreCategory = StoreCategory.SUPERMERCADO,
    val location: String = "",
    val openingHours: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val imageResId: Int = 0,
    val contact: String = "",
    var isFavorite: Boolean = false
)

@Serializable
enum class StoreCategory(val displayName: String) {
    SUPERMERCADO("SUPERMERCADO"),
    FARMACIA("FARMACIA"),
    MULTI_TIENDA("MULTI TIENDA"),
    COMIDA("COMIDA"),
    BANCO("BANCO")
}