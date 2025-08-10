package com.choa.musinsai.core.crawler.order.musinsa

import com.choa.musinsai.core.config.WebClientConfig
import com.choa.musinsai.core.crawler.order.OrderDetailRequest
import com.choa.musinsai.core.crawler.order.OrderState
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.web.reactive.function.client.WebClient

class MusinsaOrderCrawlerTest {

    private lateinit var crawler: MusinsaOrderCrawler
    
    @BeforeEach
    fun setUp() {
        val webClientConfig = WebClientConfig()
        val webClient = webClientConfig.webClient(WebClient.builder())
        crawler = MusinsaOrderCrawler(webClient)
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "MUSINSA_ACCESS_TOKEN", matches = ".+")
    fun `should fetch order detail successfully with valid order number`() {
        // Given
        val orderNo = "202504010845250002" // Example order number from the provided data
        val accessToken = System.getenv("MUSINSA_ACCESS_TOKEN")
        val request = OrderDetailRequest(
            orderNo = orderNo,
            accessToken = accessToken
        )

        // When
        val response = runBlocking {
            crawler.getOrderDetail(request)
        }

        // Then
        assertNotNull(response)
        assertEquals(orderNo, response.orderNo)
        assertNotNull(response.orderDate)
        assertNotNull(response.orderState)
        assertTrue(response.orderItems.isNotEmpty())
        
        // Verify first order item
        val firstItem = response.orderItems.first()
        assertNotNull(firstItem.goodsName)
        assertNotNull(firstItem.brandName)
        assertTrue(firstItem.quantity > 0)
        assertTrue(firstItem.receiveAmount > 0)
        
        // Verify payment info
        assertNotNull(response.paymentInfo)
        assertNotNull(response.paymentInfo.payKind)
        assertNotNull(response.paymentInfo.payKindName)
        
        // Verify delivery info
        assertNotNull(response.deliveryInfo)
        assertNotNull(response.deliveryInfo.recipientName)
        assertNotNull(response.deliveryInfo.zipCode)
        assertNotNull(response.deliveryInfo.address1)
        
        // Verify price info
        assertNotNull(response.priceInfo)
        assertTrue(response.priceInfo.totalGoodsAmount > 0)
        assertTrue(response.priceInfo.receiveAmount > 0)
        
        // Verify user info
        assertNotNull(response.userInfo)
        assertNotNull(response.userInfo.userId)
        assertNotNull(response.userInfo.userName)
        assertNotNull(response.userInfo.groupName)
    }

    @Test
    fun `should map order state correctly`() {
        // Test order state mapping
        assertEquals(10, OrderState.ORDER_COMPLETE.code)
        assertEquals("주문 완료", OrderState.ORDER_COMPLETE.text)
        
        assertEquals(50, OrderState.PURCHASE_CONFIRMED.code)
        assertEquals("구매 확정", OrderState.PURCHASE_CONFIRMED.text)
        
        // Test fromCode function
        assertEquals(OrderState.PURCHASE_CONFIRMED, OrderState.fromCode(50))
        assertEquals(OrderState.DELIVERED, OrderState.fromCode(45))
    }

    @Test
    fun `should throw exception for invalid order state code`() {
        assertThrows(IllegalArgumentException::class.java) {
            OrderState.fromCode(999)
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "MUSINSA_ACCESS_TOKEN", matches = ".+")
    fun `should handle multiple order items`() {
        // Given
        val orderNo = "202504010845250002" // This order has multiple items based on provided data
        val accessToken = System.getenv("MUSINSA_ACCESS_TOKEN")
        val request = OrderDetailRequest(
            orderNo = orderNo,
            accessToken = accessToken
        )

        // When
        val response = runBlocking {
            crawler.getOrderDetail(request)
        }

        // Then
        assertTrue(response.orderItems.size >= 2, "Should have at least 2 items")
        
        // Verify each item has required fields
        response.orderItems.forEach { item ->
            assertNotNull(item.orderOptionNo)
            assertNotNull(item.goodsNo)
            assertNotNull(item.goodsName)
            assertNotNull(item.brandName)
            assertNotNull(item.goodsOption)
            assertTrue(item.quantity > 0)
            assertNotNull(item.orderState)
            assertNotNull(item.buttonStatus)
        }
    }

    @Test
    fun `should handle missing optional fields gracefully`() {
        // This test would require mocking the WebClient response
        // For now, we'll just verify the data classes can handle null values
        
        val request = OrderDetailRequest(
            orderNo = "test123",
            accessToken = null // Optional field
        )
        
        assertNotNull(request)
        assertEquals("test123", request.orderNo)
        assertNull(request.accessToken)
    }
}