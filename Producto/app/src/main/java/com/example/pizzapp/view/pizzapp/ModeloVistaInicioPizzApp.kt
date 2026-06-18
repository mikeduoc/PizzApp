package com.example.pizzapp.view.pizzapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzapp.data.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class EstadoUIInicioPizzApp(
    val stores: List<Store> = emptyList(),
    val searchQuery: String = "",
    val isGuest: Boolean = false // Add isGuest to the UI state
)

class ModeloVistaInicioPizzApp(

    private val isGuest: Boolean // Pass isGuest as a constructor parameter
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _estadoUI = MutableStateFlow(EstadoUIInicioPizzApp(isGuest = isGuest))
    val estadoUI = _estadoUI.asStateFlow()

    init {

    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}