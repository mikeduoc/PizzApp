package com.example.pizzapp.view.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizzapp.data.model.Order
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(navController: NavController, viewModel: OrderViewModel = viewModel()) {
    val auth = FirebaseAuth.getInstance()
    val isGuest = auth.currentUser == null
    val userEmail = auth.currentUser?.email ?: "Invitado"

    var clientName by remember { mutableStateOf("") }
    var pizzaType by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var drinkType by remember { mutableStateOf("") }
    var drinkDetail by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("EFECTIVO") }
    var isEvent by remember { mutableStateOf(false) }
    var eventDate by remember { mutableStateOf("") }

    val pizzaOptions = listOf(
        "Pizza Napolitana", "Pizza Peperoni", "Pizza Vegetariana", 
        "Pizza Carnivora", "Pizza Chilena", "Pizza Margarita", 
        "Pizza Española", "Pizza Vegana", "Personalizada", "Ninguna (Solo Bebida)"
    )
    
    val ingredientOptions = listOf(
        "Ninguno", "Tomate", "Aceituna", "Pimenton", "Choclo", "Jamon", 
        "Salame", "Peperoni", "Pollo", "Albahaca", "Cebolla", "Carne", 
        "Oregano", "Merken", "Extra Queso"
    )

    val drinkOptions = listOf("Ninguna", "Coca-Cola", "Fanta", "Sprite", "Agua Mineral")
    val drinkSizes = listOf("Lata 350ml", "Litro", "1.5 Litros")
    val waterOptions = listOf("Con Gas", "Sin Gas")

    var pizzaExpanded by remember { mutableStateOf(false) }
    var ingredientExpanded by remember { mutableStateOf(false) }
    var drinkExpanded by remember { mutableStateOf(false) }
    var drinkDetailExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text("NUEVO ", fontWeight = FontWeight.Light, color = Color.White)
                        Text("PEDIDO", fontWeight = FontWeight.Black, color = Color(0xFFD32F2F))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = clientName, 
                onValueChange = { clientName = it }, 
                label = { Text("Nombre del Cliente") }, 
                modifier = Modifier.fillMaxWidth().testTag("clientNameField"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color(0xFFFFC107)
                )
            )

            // Selector de Pizza
            ExposedDropdownMenuBox(expanded = pizzaExpanded, onExpandedChange = { pizzaExpanded = !pizzaExpanded }) {
                OutlinedTextField(
                    value = pizzaType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Pizza") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pizzaExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().testTag("pizzaSelector"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
                ExposedDropdownMenu(
                    expanded = pizzaExpanded, 
                    onDismissRequest = { pizzaExpanded = false },
                    modifier = Modifier.background(Color(0xFF1E1E1E))
                ) {
                    pizzaOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) }, 
                            onClick = { pizzaType = option; pizzaExpanded = false }
                        )
                    }
                }
            }

            // Selector de Ingrediente Adicional (solo si hay pizza)
            if (pizzaType != "Ninguna (Solo Bebida)" && pizzaType.isNotBlank()) {
                ExposedDropdownMenuBox(expanded = ingredientExpanded, onExpandedChange = { ingredientExpanded = !ingredientExpanded }) {
                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ingrediente Adicional") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ingredientExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedLabelColor = Color(0xFFFFC107)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = ingredientExpanded, 
                        onDismissRequest = { ingredientExpanded = false },
                        modifier = Modifier.background(Color(0xFF1E1E1E))
                    ) {
                        ingredientOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) }, 
                                onClick = { ingredients = option; ingredientExpanded = false }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color.DarkGray)

            // SECCIÓN DE BEBESTIBLES
            Text("BEBESTIBLES", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            
            ExposedDropdownMenuBox(expanded = drinkExpanded, onExpandedChange = { drinkExpanded = !drinkExpanded }) {
                OutlinedTextField(
                    value = drinkType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Bebestible") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = drinkExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
                ExposedDropdownMenu(
                    expanded = drinkExpanded, 
                    onDismissRequest = { drinkExpanded = false },
                    modifier = Modifier.background(Color(0xFF1E1E1E))
                ) {
                    drinkOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) }, 
                            onClick = { 
                                drinkType = option
                                drinkDetail = "" // Resetear detalle al cambiar bebida
                                drinkExpanded = false 
                            }
                        )
                    }
                }
            }

            if (drinkType.isNotBlank() && drinkType != "Ninguna") {
                val detailLabel = if (drinkType == "Agua Mineral") "Tipo de Agua" else "Tamaño"
                val options = if (drinkType == "Agua Mineral") waterOptions else drinkSizes

                ExposedDropdownMenuBox(expanded = drinkDetailExpanded, onExpandedChange = { drinkDetailExpanded = !drinkDetailExpanded }) {
                    OutlinedTextField(
                        value = drinkDetail,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(detailLabel) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = drinkDetailExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedLabelColor = Color(0xFFFFC107)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = drinkDetailExpanded, 
                        onDismissRequest = { drinkDetailExpanded = false },
                        modifier = Modifier.background(Color(0xFF1E1E1E))
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) }, 
                                onClick = { drinkDetail = option; drinkDetailExpanded = false }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color.DarkGray)

            if (!isGuest) {
                OutlinedTextField(
                    value = price, 
                    onValueChange = { price = it }, 
                    label = { Text("Precio Total") }, 
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
            }

            Text("MÉTODO DE PAGO", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = paymentMethod == "EFECTIVO", 
                    onClick = { paymentMethod = "EFECTIVO" }, 
                    label = { Text("EFECTIVO") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = paymentMethod == "TARJETA", 
                    onClick = { paymentMethod = "TARJETA" }, 
                    label = { Text("TARJETA") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    )
                )
            }

            HorizontalDivider(color = Color.DarkGray)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isEvent, 
                    onCheckedChange = { isEvent = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF388E3C))
                )
                Text("Pedido para Evento", color = Color.White)
            }

            if (isEvent) {
                OutlinedTextField(
                    value = eventDate, 
                    onValueChange = { eventDate = it }, 
                    label = { Text("Fecha Evento (Ej: 30/04)") }, 
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFC107)
                    )
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp).testTag("confirmOrderButton"),
                onClick = {
                    val finalIngredients = buildString {
                        append(ingredients)
                        if (drinkType.isNotBlank() && drinkType != "Ninguna") {
                            if (isNotEmpty()) append(", ")
                            append("Bebida: $drinkType ($drinkDetail)")
                        }
                    }
                    
                    val order = Order(
                        pizzaType = if (pizzaType == "Ninguna (Solo Bebida)") "SOLO BEBESTIBLE" else pizzaType,
                        ingredients = finalIngredients,
                        totalPrice = if (isGuest) 0.0 else (price.toDoubleOrNull() ?: 0.0),
                        paymentMethod = paymentMethod,
                        isEventOrder = isEvent,
                        eventDate = if (isEvent) eventDate else null,
                        createdBy = userEmail,
                        clientName = clientName
                    )
                    viewModel.addOrder(order)
                    navController.popBackStack()
                },
                enabled = (pizzaType.isNotBlank() || drinkType.isNotBlank()) && (isGuest || price.isNotBlank()),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CONFIRMAR PEDIDO", fontWeight = FontWeight.Black)
            }
        }
    }
}
