package com.example.pizzapp.view.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzapp.data.model.Insumo
import com.example.pizzapp.data.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface InventoryUiState {
    object Loading : InventoryUiState
    data class Success(val items: List<Insumo>) : InventoryUiState
    data class Error(val message: String) : InventoryUiState
}

class InventoryViewModel(private val repository: InventoryRepository = InventoryRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow<InventoryUiState>(InventoryUiState.Loading)
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        fetchInventory()
    }

    private fun fetchInventory() {
        viewModelScope.launch {
            try {
                repository.getInventory().collect { items ->
                    _uiState.value = InventoryUiState.Success(items)
                }
            } catch (e: Exception) {
                _uiState.value = InventoryUiState.Error(e.message ?: "Error al cargar inventario")
            }
        }
    }

    fun saveItem(insumo: Insumo) {
        viewModelScope.launch {
            try {
                repository.saveInsumo(insumo)
            } catch (e: Exception) {
                _uiState.value = InventoryUiState.Error(e.message ?: "Error al guardar insumo")
            }
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteInsumo(id)
            } catch (e: Exception) {
                _uiState.value = InventoryUiState.Error(e.message ?: "Error al eliminar insumo")
            }
        }
    }

    fun addStock(id: String, currentStock: Double, amountToAdd: Double) {
        viewModelScope.launch {
            repository.updateStock(id, currentStock + amountToAdd)
        }
    }

    fun removeStock(id: String, currentStock: Double, amountToRemove: Double) {
        viewModelScope.launch {
            val newStock = (currentStock - amountToRemove).coerceAtLeast(0.0)
            repository.updateStock(id, newStock)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAllInventory()
        }
    }
}
