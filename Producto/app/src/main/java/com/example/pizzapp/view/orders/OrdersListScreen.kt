package com.example.pizzapp.view.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import com.example.pizzapp.view.core.navegation.CreateOrder
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    navController: NavController, 
    isGuest: Boolean,
    viewModel: OrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val isAdmin = !isGuest || auth.currentUser != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text("LISTADO DE ", fontWeight = FontWeight.Light, color = Color.White)
                        Text("PEDIDOS", fontWeight = FontWeight.Black, color = Color(0xFFD32F2F))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = { viewModel.clearAll() }) {
                            Icon(Icons.Default.DeleteForever, contentDescription = "Limpiar Todo", tint = Color(0xFFD32F2F))
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(CreateOrder) },
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Pedido")
            }
        },
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is OrdersUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFD32F2F))
                is OrdersUiState.Success -> {
                    val normalOrders = state.orders.filter { !it.isEventOrder }
                    if (normalOrders.isEmpty()) {
                        Text("No hay pedidos normales registrados", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(normalOrders) { order ->
                                OrderCard(order, viewModel, isAdmin)
                            }
                        }
                    }
                }
                is OrdersUiState.Error -> Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, viewModel: OrderViewModel, isAdmin: Boolean) {
    var showEditPriceDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (order.state == OrderState.CANCELLED) Color(0xFF331515) else Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = order.pizzaType.uppercase(), fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    if (order.clientName.isNotBlank()) {
                        Text(text = "CLIENTE: ${order.clientName}", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFC107))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${order.totalPrice.toInt()}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                    if (isAdmin) {
                        IconButton(onClick = { showEditPriceDialog = true }, modifier = Modifier.size(30.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Precio", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = order.ingredients, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.DarkGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "CREADO POR: ${order.createdBy.substringBefore("@")}", style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Surface(
                    color = getStatusColor(order.state).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "ESTADO: ${translateStatus(order.state)}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = getStatusColor(order.state),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            if (isAdmin && order.state != OrderState.DELIVERED && order.state != OrderState.CANCELLED) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.updateState(order.id, OrderState.READY) },
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("LISTO", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = { viewModel.updateState(order.id, OrderState.DELIVERED) },
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("ENTREGAR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    IconButton(
                        onClick = { viewModel.cancel(order.id) },
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.Red)
                    }
                }
            }
        }
    }

    if (isAdmin && showEditPriceDialog) {
        EditPriceDialog(
            currentPrice = order.totalPrice.toInt().toString(),
            onDismiss = { showEditPriceDialog = false },
            onConfirm = { newPrice ->
                viewModel.updatePrice(order.id, newPrice.toDoubleOrNull() ?: 0.0)
                showEditPriceDialog = false
            }
        )
    }
}

@Composable
fun EditPriceDialog(currentPrice: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newPrice by remember { mutableStateOf(currentPrice) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color.White,
        title = { Text("EDITAR PRECIO", fontWeight = FontWeight.Black) },
        text = {
            OutlinedTextField(
                value = newPrice,
                onValueChange = { newPrice = it },
                label = { Text("Nuevo Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color(0xFFFFC107)
                )
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(newPrice) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))) {
                Text("ACTUALIZAR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color.Gray)
            }
        }
    )
}

fun getStatusColor(state: OrderState): Color = when (state) {
    OrderState.IN_PROCESS -> Color(0xFFFFA000) // Naranja/Ambar
    OrderState.READY -> Color(0xFF388E3C)      // Verde
    OrderState.DELIVERED -> Color(0xFF1976D2)  // Azul
    OrderState.CANCELLED -> Color(0xFFD32F2F)  // Rojo
}

fun translateStatus(state: OrderState): String = when (state) {
    OrderState.IN_PROCESS -> "EN PROCESO"
    OrderState.READY -> "LISTO"
    OrderState.DELIVERED -> "ENTREGADO"
    OrderState.CANCELLED -> "CANCELADO"
}
