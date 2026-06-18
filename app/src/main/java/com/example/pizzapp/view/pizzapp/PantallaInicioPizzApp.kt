package com.example.pizzapp.view.pizzapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizzapp.view.core.navegation.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioPizzApp(navController: NavController, isGuest: Boolean = false) {
    val auth = FirebaseAuth.getInstance()
    val userEmail = if (isGuest) "Invitado" else (auth.currentUser?.email ?: "Usuario")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text("PIZZA", fontWeight = FontWeight.Black, color = Color.White)
                        Text("RELLA", fontWeight = FontWeight.Light, color = Color(0xFFD32F2F))
                        Text(" PANEL", fontWeight = FontWeight.Light, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.align(Alignment.Bottom))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F0F0F)),
                actions = {
                    IconButton(onClick = { 
                        auth.signOut()
                        navController.navigate(Login) { popUpTo(0) }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión", tint = Color(0xFFD32F2F))
                    }
                }
            )
        },
        containerColor = Color(0xFF0F0F0F) // Fondo Negro Pizzarella
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta de Usuario Premium
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Bienvenido a Pizzarella,", fontSize = 12.sp, color = Color.Gray)
                        Text(text = userEmail.substringBefore("@"), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                    }
                }
            }

            Text(
                text = "OPERACIONES DISPONIBLES",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )

            // Cuadrícula de Menú
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MenuButton(
                        title = "NUESTRO MENÚ",
                        icon = Icons.Default.RestaurantMenu,
                        colorAccent = Color(0xFFD32F2F), // Rojo
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(MenuPizzas) }
                    )
                    MenuButton(
                        title = "PEDIDOS",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        colorAccent = Color(0xFFFFC107), // Amarillo
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(OrdersList(isGuest = isGuest)) }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!isGuest) {
                        MenuButton(
                            title = "INVENTARIO",
                            icon = Icons.Default.Inventory,
                            colorAccent = Color(0xFF388E3C), // Verde
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(InventoryList) }
                        )
                    } else {
                        LockedMenuButton(title = "INVENTARIO", modifier = Modifier.weight(1f))
                    }

                    if (!isGuest) {
                        MenuButton(
                            title = "VENTAS",
                            icon = Icons.Default.QueryStats,
                            colorAccent = Color(0xFF2196F3), // Azul (Stats)
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Ventas) }
                        )
                    } else {
                        LockedMenuButton(title = "VENTAS", modifier = Modifier.weight(1f))
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MenuButton(
                        title = "EVENTOS",
                        icon = Icons.Default.Event,
                        colorAccent = Color(0xFFFFC107), // Amarillo
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Evento) }
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Para mantener el tamaño
                }
            }
        }
    }
}

@Composable
fun MenuButton(title: String, icon: ImageVector, colorAccent: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(130.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(36.dp), tint = colorAccent)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White)
        }
    }
}

@Composable
fun LockedMenuButton(title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(130.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = "ADMIN ONLY", style = MaterialTheme.typography.labelSmall, color = Color(0xFFD32F2F))
        }
    }
}
