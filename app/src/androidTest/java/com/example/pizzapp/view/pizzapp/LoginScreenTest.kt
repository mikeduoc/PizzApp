package com.example.pizzapp.view.pizzapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pizzapp.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginTest_IngresarCredencialesYPresionarBoton() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaLogin(navController = navController)
            }
        }

        composeTestRule
            .onNodeWithText("Correo Electrónico")
            .performTextInput("test@pizzarella.com")

        composeTestRule
            .onNodeWithText("Contraseña")
            .performTextInput("123456")

        composeTestRule
            .onNodeWithText("INICIAR SESIÓN", ignoreCase = true)
            .performClick()

        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun loginTest_CamposVacios_VerificarNoNavegacion() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaLogin(navController = navController)
            }
        }

        composeTestRule
            .onNodeWithText("INICIAR SESIÓN", ignoreCase = true)
            .performClick()
        composeTestRule.onNodeWithText("Correo Electrónico").assertExists()
    }
}
