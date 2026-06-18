package com.example.pizzapp.view.core.navegation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pizzapp.view.pizzapp.PantallaInicio
import com.example.pizzapp.view.pizzapp.PantallaInicioPizzApp
import com.example.pizzapp.view.pizzapp.PantallaLogin
import com.example.pizzapp.view.pizzapp.PantallaRegistro
import com.example.pizzapp.view.orders.OrdersListScreen
import com.example.pizzapp.view.orders.CreateOrderScreen
import com.example.pizzapp.view.orders.MenuPizzasScreen
import com.example.pizzapp.view.orders.EventoScreen
import com.example.pizzapp.view.inventory.InventoryScreen
import com.example.pizzapp.view.ventas.VentasScreen

@Composable
fun NavigationWrapper(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Welcome){
        composable<Welcome> {
            PantallaInicio(
                onUnlocked = {
                    navController.navigate(Login) {
                        popUpTo(Welcome) { inclusive = true }
                    }
                }
            )
        }
        composable<Login> {
            PantallaLogin(navController = navController)
        }
        composable<Registro> {
            PantallaRegistro(navController = navController)
        }
        composable<Home> { backStackEntry ->
            val home: Home = backStackEntry.toRoute()
            PantallaInicioPizzApp(navController = navController, isGuest = home.isGuest)
        }
        composable<OrdersList> { backStackEntry ->
            val ordersList: OrdersList = backStackEntry.toRoute()
            OrdersListScreen(navController = navController, isGuest = ordersList.isGuest)
        }
        composable<CreateOrder> {
            CreateOrderScreen(navController = navController)
        }
        composable<InventoryList> {
            InventoryScreen(navController = navController)
        }
        composable<Ventas> {
            VentasScreen(navController = navController)
        }
        composable<MenuPizzas> {
            MenuPizzasScreen(navController = navController)
        }
        composable<Evento> {
            EventoScreen(navController = navController)
        }
    }
}
