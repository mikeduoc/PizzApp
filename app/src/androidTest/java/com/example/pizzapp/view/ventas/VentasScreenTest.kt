package com.example.pizzapp.view.ventas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pizzapp.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VentasScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun ventasScreen_displaysStaticHeaders() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                VentasScreen(navController = navController)
            }
        }
        composeTestRule.onNodeWithText("Control de Ventas").assertExists()
        composeTestRule.onNodeWithText("Resumen Diario").assertExists()
        composeTestRule.onNodeWithText("Total Vendido").assertExists()
        composeTestRule.onNodeWithText("Efectivo").assertExists()
        composeTestRule.onNodeWithText("Tarjeta").assertExists()
        composeTestRule.onNodeWithText("Pedidos Entregados Hoy").assertExists()
    }
    @Test
    fun salesCard_displaysCorrectAmount() {
        val testTitle = "Venta Test"
        val testAmount = 15500.50
        composeTestRule.setContent {
            AppTheme {
                SalesCard(
                    title = testTitle,
                    amount = testAmount,
                    icon = Icons.Default.AttachMoney,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
        composeTestRule.onNodeWithText(testTitle).assertExists()
        composeTestRule.onNodeWithText("$15500.50").assertExists()
    }
}
