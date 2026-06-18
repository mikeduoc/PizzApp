package com.example.pizzapp.view.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizzapp.data.model.Insumo
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController, viewModel: InventoryViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val isAdmin = auth.currentUser != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text("GESTIÓN DE ", fontWeight = FontWeight.Light, color = Color.White)
                        Text("INSUMOS", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = { viewModel.clearAll() }) {
                            Icon(Icons.Default.DeleteForever, contentDescription = "Limpiar Todo", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF388E3C), // Verde Pizzarella
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        },
        containerColor = Color(0xFF0F0F0F) // Fondo Negro Pizzarella
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is InventoryUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
                is InventoryUiState.Success -> {
                    if (state.items.isEmpty()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                            Text("No hay insumos registrados", color = Color.Gray)
                            if (isAdmin) {
                                Button(
                                    onClick = { seedInventory(viewModel) }, 
                                    modifier = Modifier.padding(top = 16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Cargar Inventario Inicial")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(state.items.sortedBy { it.name }) { item ->
                                InsumoCard(item, viewModel)
                            }
                        }
                    }
                }
                is InventoryUiState.Error -> Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            }
        }

        if (showAddDialog) {
            AddInsumoDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, unit, min ->
                    viewModel.saveItem(Insumo(name = name, unit = unit, minStock = min.toDoubleOrNull() ?: 1.0))
                    showAddDialog = false
                }
            )
        }
    }
}

fun seedInventory(viewModel: InventoryViewModel) {
    val baseInventory = listOf(
        Insumo(name = "Tomate", stock = 10.0, unit = "kg", minStock = 3.0),
        Insumo(name = "Aceituna", stock = 5.0, unit = "kg", minStock = 1.5),
        Insumo(name = "Pimenton", stock = 4.0, unit = "kg", minStock = 1.0),
        Insumo(name = "Choclo", stock = 6.0, unit = "kg", minStock = 2.0),
        Insumo(name = "Jamon", stock = 8.0, unit = "kg", minStock = 2.0),
        Insumo(name = "Salame", stock = 5.0, unit = "kg", minStock = 1.0),
        Insumo(name = "Peperoni", stock = 12.0, unit = "kg", minStock = 3.0),
        Insumo(name = "Pollo", stock = 15.0, unit = "kg", minStock = 5.0),
        Insumo(name = "Albahaca", stock = 2.0, unit = "kg", minStock = 0.5),
        Insumo(name = "Cebolla", stock = 20.0, unit = "kg", minStock = 5.0),
        Insumo(name = "Carne", stock = 25.0, unit = "kg", minStock = 8.0),
        Insumo(name = "Oregano", stock = 1.0, unit = "kg", minStock = 0.2),
        Insumo(name = "Merken", stock = 1.0, unit = "kg", minStock = 0.2),
        Insumo(name = "Cajas de pizza", stock = 200.0, unit = "uds", minStock = 50.0),
        Insumo(name = "Platos para pizza", stock = 150.0, unit = "uds", minStock = 30.0),
        Insumo(name = "Bebidas", stock = 100.0, unit = "uds", minStock = 20.0),
        Insumo(name = "Aguas", stock = 60.0, unit = "uds", minStock = 15.0),
        Insumo(name = "Bolsas", stock = 500.0, unit = "uds", minStock = 100.0),
        Insumo(name = "Te", stock = 2.0, unit = "kg", minStock = 0.5),
        Insumo(name = "Cafe", stock = 3.0, unit = "kg", minStock = 1.0),
        Insumo(name = "Vasos", stock = 1000.0, unit = "uds", minStock = 200.0),
        Insumo(name = "Masas de pizza", stock = 50.0, unit = "uds", minStock = 15.0)
    )
    baseInventory.forEach { viewModel.saveItem(it) }
}

@Composable
fun InsumoCard(item: Insumo, viewModel: InventoryViewModel) {
    val isLow = item.stock <= item.minStock
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(
                1.dp, 
                if (isLow) Color(0xFFD32F2F) else Color.Transparent, 
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color.White)
                Text(text = "CANTIDAD: ${item.stock} ${item.unit}", fontSize = 12.sp, color = Color.Gray)
                if (isLow) {
                    Text(
                        text = "STOCK CRÍTICO", 
                        color = Color(0xFFD32F2F), 
                        style = MaterialTheme.typography.labelSmall, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Botones de control con color Amarillo Pizzarella
            IconButton(onClick = { viewModel.removeStock(item.id, item.stock, 1.0) }) {
                Icon(Icons.Default.RemoveCircle, contentDescription = "Restar", tint = Color(0xFFFFC107))
            }
            
            Text(
                text = "${item.stock.toInt()}", 
                color = Color.White, 
                fontWeight = FontWeight.Bold, 
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            IconButton(onClick = { viewModel.addStock(item.id, item.stock, 1.0) }) {
                Icon(Icons.Default.AddCircle, contentDescription = "Sumar", tint = Color(0xFFFFC107))
            }
            
            IconButton(onClick = { viewModel.deleteItem(item.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.DarkGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun AddInsumoDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var min by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color.White,
        textContentColor = Color.Gray,
        title = { Text("NUEVO INSUMO", fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name, 
                    onValueChange = { name = it }, 
                    label = { Text("Nombre del Insumo") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
                OutlinedTextField(
                    value = unit, 
                    onValueChange = { unit = it }, 
                    label = { Text("Unidad (kg, uds)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
                OutlinedTextField(
                    value = min, 
                    onValueChange = { min = it }, 
                    label = { Text("Mínimo para alerta") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, unit, min) }, 
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) { Text("GUARDAR", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCELAR", color = Color.Gray) }
        }
    )
}
