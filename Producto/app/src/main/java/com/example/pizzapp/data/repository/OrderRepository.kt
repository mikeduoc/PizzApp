package com.example.pizzapp.data.repository

import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val ordersCollection = db.collection("orders")

    fun getOrders(): Flow<List<Order>> = callbackFlow {
        val subscription = ordersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val orders = snapshot.toObjects(Order::class.java)
                trySend(orders)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun saveOrder(order: Order) {
        ordersCollection.add(order).await()
    }

    suspend fun updateOrderState(orderId: String, newState: OrderState) {
        ordersCollection.document(orderId).update("state", newState).await()
    }

    suspend fun updateOrderPrice(orderId: String, newPrice: Double) {
        ordersCollection.document(orderId).update("totalPrice", newPrice).await()
    }

    suspend fun cancelOrder(orderId: String) {
        ordersCollection.document(orderId).update("state", OrderState.CANCELLED).await()
    }

    suspend fun deleteAllOrders() {
        val snapshot = ordersCollection.get().await()
        db.runBatch { batch ->
            snapshot.documents.forEach { batch.delete(it.reference) }
        }.await()
    }
}
