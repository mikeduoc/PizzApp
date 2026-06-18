package com.example.pizzapp.view.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizzapp.R
import com.example.pizzapp.data.model.Order
import com.example.pizzapp.view.core.navegation.OrdersList
import com.google.firebase.auth.FirebaseAuth

data class DefaultPizza(
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPizzasScreen(navController: NavController, viewModel: OrderViewModel = viewModel()) {
    val auth = FirebaseAuth.getInstance()
    val isGuest = auth.currentUser == null
    val userEmail = auth.currentUser?.email ?: "Invitado"

    val pizzaMenu = listOf(
        DefaultPizza("Pizza Napolitana Familiar", "Tomate, mozzarella, jamón y orégano", 14000.0, R.drawable.napolitana),
        DefaultPizza("Pizza Peperoni Familiar", "Doble pepperoni y extra queso", 15000.0, R.drawable.pepperoni),
        DefaultPizza("Pizza Vegetariana Familiar", "Pimentón, choclo, cebolla y aceitunas", 13500.0, R.drawable.vegetariana),
        DefaultPizza("Pizza Carnivora Familiar", "Carne, jamón, salame y pepperoni", 18000.0, R.drawable.carnivora),
        DefaultPizza("Pizza Chilena Familiar", "Carne, cebolla, pimentón y merken", 16000.0, R.drawable.chilena),
        DefaultPizza("Pizza Margarita Familiar", "Tomate, albahaca y mozzarella", 12000.0, R.drawable.margarita),
        DefaultPizza("Pizza Española Familiar", "Salame, pimentón y aceitunas", 15000.0, R.drawable.espanola),
        DefaultPizza("Pizza Vegana Familiar", "Queso vegano y vegetales de la estación", 16000.0, R.drawable.vegana),
        DefaultPizza("Bebida en Lata", "Variedad de bebidas", 1500.0, R.drawable.bebida_lata),
        DefaultPizza("Bebida de Litro", "Algunas bebidas de litro", 2700.0, R.drawable.bebida_litro_variedad),
        DefaultPizza("Bebida de Litro y Medio", "Un sabor refrescante en litro y medio", 3000.0, R.drawable.bebida_litroymedio),
        DefaultPizza("Agua Mineral 600ml", "Agua gasificada en un formato perfecto", 1000.0, R.drawable.agua_mineral_600_ml)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text("NUESTRAS ", fontWeight = FontWeight.Light, color = Color.White)
                        Text("PIZZAS", fontWeight = FontWeight.Black, color = Color(0xFFD32F2F))
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pizzaMenu) { pizza ->
                PizzaMenuItemCard(pizza) {
                    val order = Order(
                        pizzaType = pizza.name,
                        ingredients = "Pedido desde menú",
                        totalPrice = pizza.price,
                        paymentMethod = "EFECTIVO",
                        createdBy = userEmail,
                        clientName = "Cliente Menú"
                    )
                    viewModel.addOrder(order)
                    // Navegamos a la lista de pedidos SIN borrar el historial
                    navController.navigate(OrdersList(isGuest = isGuest))
                }
            }
        }
    }
}

@Composable
fun PizzaMenuItemCard(pizza: DefaultPizza, onOrderClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = pizza.imageRes),
                    contentDescription = pizza.name,
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.align(Alignment.BottomStart),
                    color = Color(0xFFFFC107),
                    shape = RoundedCornerShape(topEnd = 8.dp)
                ) {
                    Text(
                        text = "$${pizza.price.toInt()}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = pizza.name.uppercase(), fontWeight = FontWeight.Black, fontSize = 12.sp, maxLines = 1, color = Color.White)
                Text(text = pizza.description, color = Color.Gray, fontSize = 10.sp, maxLines = 2, minLines = 2, lineHeight = 12.sp)
                
                Button(
                    onClick = onOrderClick,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("PEDIR", fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
