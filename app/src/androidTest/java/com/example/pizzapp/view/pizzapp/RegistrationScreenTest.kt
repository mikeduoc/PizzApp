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
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState_buttonDisabled() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaRegistro(navController = navController)
            }
        }
        composeTestRule
            .onNodeWithTag("registerButton")
            .assertIsNotEnabled()
    }

    @Test
    fun validInput_enablesButton() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaRegistro(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("nameField")
            .performTextInput("Juan Perez")

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput("juan@test.com")

        composeTestRule.onNodeWithTag("passwordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("confirmPasswordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("termsCheckbox")
            .performClick()

        composeTestRule
            .onNodeWithTag("registerButton")
            .assertIsEnabled()
    }

    @Test
    fun emptyFields_noLoadingShown() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaRegistro(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("termsCheckbox")
            .performClick()

        composeTestRule.onNodeWithTag("registerButton")
            .performClick()

        composeTestRule.onNodeWithTag("loading_indicator")
            .assertDoesNotExist()
    }

    @Test
    fun passwordMismatch_noLoadingShown() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaRegistro(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("nameField")
            .performTextInput("Juan")

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput("juan@test.com")

        composeTestRule.onNodeWithTag("passwordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("confirmPasswordField")
            .performTextInput("654321")

        composeTestRule.onNodeWithTag("termsCheckbox")
            .performClick()

        composeTestRule.onNodeWithTag("registerButton")
            .performClick()

        composeTestRule.onNodeWithTag("loading_indicator")
            .assertDoesNotExist()
    }

    @Test
    fun validFlow_buttonClick_doesNotCrash() {
        composeTestRule.setContent {
            AppTheme {
                val navController = rememberNavController()
                PantallaRegistro(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("nameField")
            .performTextInput("Juan")

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput("juan@test.com")

        composeTestRule.onNodeWithTag("passwordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("confirmPasswordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("termsCheckbox")
            .performClick()

        composeTestRule.onNodeWithTag("registerButton")
            .performClick()

        composeTestRule.onNodeWithTag("registerButton")
            .assertExists()
    }
}