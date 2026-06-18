package com.example.pizzapp.view.orders

import com.example.pizzapp.data.model.Order
import org.junit.Assert.assertEquals
import org.junit.Test
class OrderCreationLogicTest {

    @Test
    fun orderMapping_SoloBebida_SetsCorrectPizzaType() {
        val pizzaTypeFromUI = "Ninguna (Solo Bebida)"
        val finalPizzaType = if (pizzaTypeFromUI == "Ninguna (Solo Bebida)") {
            "SOLO BEBESTIBLE"
        } else {
            pizzaTypeFromUI
        }

        assertEquals("SOLO BEBESTIBLE", finalPizzaType)
    }

    @Test
    fun ingredientsLogic_BuildsCorrectString_WithPizzaAndDrink() {
        val selectedIngredients = "Extra Queso"
        val selectedDrink = "Coca-Cola"
        val selectedSize = "Litro"
        val finalIngredients = buildString {
            append(selectedIngredients)
            if (selectedDrink.isNotBlank() && selectedDrink != "Ninguna") {
                if (isNotEmpty()) append(", ")
                append("Bebida: $selectedDrink ($selectedSize)")
            }
        }

        val expected = "Extra Queso, Bebida: Coca-Cola (Litro)"
        assertEquals(expected, finalIngredients)
    }

    @Test
    fun ingredientsLogic_BuildsCorrectString_OnlyDrink() {
        val selectedIngredients = ""
        val selectedDrink = "Agua Mineral"
        val selectedDetail = "Sin Gas"

        val finalIngredients = buildString {
            append(selectedIngredients)
            if (selectedDrink.isNotBlank() && selectedDrink != "Ninguna") {
                if (isNotEmpty()) append(", ")
                append("Bebida: $selectedDrink ($selectedDetail)")
            }
        }

        val expected = "Bebida: Agua Mineral (Sin Gas)"
        assertEquals(expected, finalIngredients)
    }
}
