package com.example.pizzapp.view.orders

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(
    navController: NavController,
    viewModel: OrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val isAdmin = auth.currentUser != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text("PEDIDOS DE ", fontWeight = FontWeight.Light, color = Color.White)
                        Text("EVENTO", fontWeight = FontWeight.Black, color = Color(0xFFFFC107))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is OrdersUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFD32F2F))
                is OrdersUiState.Success -> {
                    val eventOrders = state.orders.filter { it.isEventOrder }
                    if (eventOrders.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(Icons.Default.EventBusy, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.DarkGray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No hay pedidos marcados como evento", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(eventOrders) { order ->
                                EventOrderCard(order, viewModel, isAdmin)
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
fun EventOrderCard(order: Order, viewModel: OrderViewModel, isAdmin: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.pizzaType.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    if (order.clientName.isNotBlank()) {
                        Text(
                            text = "CLIENTE: ${order.clientName}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFFFC107)
                        )
                    }
                }
                Surface(
                    color = Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "$${order.totalPrice.toInt()}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Mostrar Ingredientes y Bebestibles
            if (order.ingredients.isNotBlank()) {
                val parts = order.ingredients.split(", Bebida: ")
                val ingredientsOnly = parts[0]
                val drinkOnly = if (parts.size > 1) parts[1] else null

                if (ingredientsOnly.isNotBlank() && ingredientsOnly != "Ninguno") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconWithTint(Icons.Default.LocalPizza, contentDescription = null, size = 14.dp, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ADICIONALES: $ingredientsOnly", fontSize = 11.sp, color = Color.LightGray)
                    }
                }

                if (drinkOnly != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconWithTint(Icons.Default.LocalDrink, contentDescription = null, size = 14.dp, tint = Color(0xFF2196F3))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "BEBESTIBLE: ${drinkOnly.uppercase()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBBDEFB))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            if (order.eventDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconWithTint(Icons.Default.Event, contentDescription = null, size = 14.dp, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "FECHA PROGRAMADA: ${order.eventDate}", fontSize = 11.sp, color = Color.Gray)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = getStatusColor(order.state).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, getStatusColor(order.state))
                ) {
                    Text(
                        text = translateStatus(order.state),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(order.state)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "MÉTODO: ${order.paymentMethod}", fontSize = 10.sp, color = Color.DarkGray)
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
                        modifier = Modifier.size(40.dp).background(Color.DarkGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun IconWithTint(imageVector: ImageVector, contentDescription: String?, size: Dp, tint: Color) {
    Icon(imageVector, contentDescription, modifier = Modifier.size(size), tint = tint)
}
