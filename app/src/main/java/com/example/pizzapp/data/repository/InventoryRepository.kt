package com.example.pizzapp.data.repository

import com.example.pizzapp.data.model.Insumo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InventoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val inventoryCollection = db.collection("inventory")

    fun getInventory(): Flow<List<Insumo>> = callbackFlow {
        val subscription = inventoryCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.toObjects(Insumo::class.java)
                trySend(items)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun saveInsumo(insumo: Insumo) {
        if (insumo.id.isEmpty()) {
            inventoryCollection.add(insumo).await()
        } else {
            inventoryCollection.document(insumo.id).set(insumo).await()
        }
    }

    suspend fun updateStock(id: String, newStock: Double) {
        inventoryCollection.document(id).update("stock", newStock).await()
    }

    suspend fun deleteInsumo(id: String) {
        inventoryCollection.document(id).delete().await()
    }

    suspend fun deleteAllInventory() {
        val snapshot = inventoryCollection.get().await()
        db.runBatch { batch ->
            snapshot.documents.forEach { batch.delete(it.reference) }
        }.await()
    }
}











