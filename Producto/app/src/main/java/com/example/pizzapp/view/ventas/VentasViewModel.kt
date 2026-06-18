package com.example.pizzapp.view.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import com.example.pizzapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class SalesSummary(
    val totalDaily: Double = 0.0,
    val totalCash: Double = 0.0,
    val totalCard: Double = 0.0,
    val orderCount: Int = 0
)

class VentasViewModel(private val repository: OrderRepository = OrderRepository()) : ViewModel() {

    private val _summary = MutableStateFlow(SalesSummary())
    val summary: StateFlow<SalesSummary> = _summary.asStateFlow()

    init {
        calculateSales()
    }

    private fun calculateSales() {
        viewModelScope.launch {
            repository.getOrders().collect { orders ->
                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val deliveredToday = orders.filter { 
                    it.state == OrderState.DELIVERED && it.timestamp >= today 
                }

                val total = deliveredToday.sumOf { it.totalPrice }
                val cash = deliveredToday.filter { it.paymentMethod == "EFECTIVO" }.sumOf { it.totalPrice }
                val card = deliveredToday.filter { it.paymentMethod == "TARJETA" }.sumOf { it.totalPrice }

                _summary.value = SalesSummary(
                    totalDaily = total,
                    totalCash = cash,
                    totalCard = card,
                    orderCount = deliveredToday.size
                )
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAllOrders()
        }
    }
}
