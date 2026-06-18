package com.example.pizzapp.view.orders

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pizzapp.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateOrderScreenTest {
    @get:Rule

    val composeTestRule = createComposeRule()
    @Test
    fun createOrder_InitialState_ButtonIsDisabled() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                CreateOrderScreen(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("confirmOrderButton").assertIsNotEnabled()
    }

    @Test
    fun createOrder_SelectionSequence_EnablesButton() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                CreateOrderScreen(navController = navController)
            }
        }

        composeTestRule.onNodeWithTag("clientNameField").performTextInput("Cliente de Prueba")
        composeTestRule.onNodeWithTag("pizzaSelector").performClick()
        composeTestRule.onNodeWithText("Pizza Napolitana").performClick()
        composeTestRule.onNodeWithTag("confirmOrderButton").assertIsEnabled()
    }

    @Test
    fun createOrder_BebestiblesSelection_ShowsDetail() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                CreateOrderScreen(navController = navController)
            }
        }

        composeTestRule.onNodeWithText("Tamaño").assertDoesNotExist()
        composeTestRule.onNodeWithText("Tipo de Agua").assertDoesNotExist()

        composeTestRule.onNodeWithText("Seleccionar Bebestible").performClick()
        composeTestRule.onNodeWithText("Coca-Cola").performClick()

        composeTestRule.onNodeWithText("Tamaño").assertExists()
    }
}
