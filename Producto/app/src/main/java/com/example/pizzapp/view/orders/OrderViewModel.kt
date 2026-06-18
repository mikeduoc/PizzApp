package com.example.pizzapp.view.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import com.example.pizzapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface OrdersUiState {
    object Loading : OrdersUiState
    data class Success(val orders: List<Order>) : OrdersUiState
    data class Error(val message: String) : OrdersUiState
}

class OrderViewModel(private val repository: OrderRepository = OrderRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            try {
                repository.getOrders().collect { orders ->
                    _uiState.value = OrdersUiState.Success(orders)
                }
            } catch (e: Exception) {
                _uiState.value = OrdersUiState.Error(e.message ?: "Error al cargar pedidos")
            }
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.saveOrder(order)
            } catch (e: Exception) {
                _uiState.value = OrdersUiState.Error(e.message ?: "Error al guardar el pedido")
            }
        }
    }

    fun updateState(orderId: String, newState: OrderState) {
        viewModelScope.launch {
            repository.updateOrderState(orderId, newState)
        }
    }

    fun updatePrice(orderId: String, newPrice: Double) {
        viewModelScope.launch {
            repository.updateOrderPrice(orderId, newPrice)
        }
    }

    fun cancel(orderId: String) {
        viewModelScope.launch {
            repository.cancelOrder(orderId)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAllOrders()
        }
    }
}
