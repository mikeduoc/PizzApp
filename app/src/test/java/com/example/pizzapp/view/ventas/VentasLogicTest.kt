package com.example.pizzapp.view.ventas

import com.example.pizzapp.data.model.Order
import com.example.pizzapp.data.model.OrderState
import org.junit.Assert.assertEquals
import org.junit.Test
class VentasLogicTest {

    @Test
    fun calculateSales_Logic_IsCorrect() {
        val orders = listOf(
            Order(totalPrice = 10000.0, paymentMethod = "EFECTIVO", state = OrderState.DELIVERED),
            Order(totalPrice = 5000.0, paymentMethod = "TARJETA", state = OrderState.DELIVERED),
            Order(totalPrice = 2000.0, paymentMethod = "EFECTIVO", state = OrderState.IN_PROCESS),
            Order(totalPrice = 3000.0, paymentMethod = "EFECTIVO", state = OrderState.DELIVERED)
        )

        val deliveredOrders = orders.filter { it.state == OrderState.DELIVERED }
        
        val total = deliveredOrders.sumOf { it.totalPrice }
        val cash = deliveredOrders.filter { it.paymentMethod == "EFECTIVO" }.sumOf { it.totalPrice }
        val card = deliveredOrders.filter { it.paymentMethod == "TARJETA" }.sumOf { it.totalPrice }
        val count = deliveredOrders.size

        assertEquals(18000.0, total, 0.1)
        assertEquals(13000.0, cash, 0.1)
        assertEquals(5000.0, card, 0.1)
        assertEquals(3, count)
    }
}
