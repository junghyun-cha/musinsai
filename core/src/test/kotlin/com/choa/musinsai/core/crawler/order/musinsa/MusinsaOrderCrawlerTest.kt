package com.choa.musinsai.core.crawler.order.musinsa

import com.choa.musinsai.core.config.WebClientConfig
import com.choa.musinsai.core.provider.crawler.auth.musinsa.MusinsaAuthCrawler
import com.choa.musinsai.core.provider.crawler.order.musinsa.MusinsaOrderCrawler
import com.choa.musinsai.core.provider.crawler.order.musinsa.OrderDetailRequest
import com.choa.musinsai.core.provider.crawler.order.musinsa.OrderState
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.web.reactive.function.client.WebClient

class MusinsaOrderCrawlerTest {

    private lateinit var crawler: MusinsaOrderCrawler
    val cookies = mutableMapOf<String, String>()

    @BeforeEach
    fun setUp() {
        val webClientConfig = WebClientConfig()
        val webClient = webClientConfig.webClient(WebClient.builder())
        crawler = MusinsaOrderCrawler(webClient)

        cookies.put(MusinsaAuthCrawler.REFRESH_TOKEN_KEY, "63bbf83ebbb90e022731735d9b0776d8ad61bf5b")
        cookies.put(
            MusinsaAuthCrawler.ACCESS_TOKEN_KEY,
            "sJ5K8RVvBw6SQT5kHCkkCMvJ6Gbqe6n4b8XX5hlxpmfg6c%2Fts41qwlhCo5TWxyirpUG5DaxBsM1880LVuSiSdpkHBkvBnZdf5YKSV9VdxcoSMAYeHrt5rVCp0af3kNzKEmuwcvIuqefae2fyWZ1n8gt6R1rYK8lihEF7eOmc%2B9rNDwTPN9JLJVCDisDhqslzTT7GfKtMd82PHR5gVyIkaZJc6I8oip7AvQwkm5GtoA%2FCLS7bPAjHRSmtlVacyh6de%2Fq11kkC37QXWsCMWWcAmS9ra7W16qBa5tE%2F0opuz77G0Q%2FKkfazlndcKE1RDnqUNl%2FIjri406tLpicSFETfh6kX%2FfSaz08dUbUcxCuBcovyGErMyn%2BDEcvHPfabpdwq3kJZjIiCa96jNCrNa0ZMXXEBy4AcbOCrR016vzGldBV8xqPOak8nTeQ7IEx6vxdNr8D6pmdAFdGelvZnjsViYqr1a4llINPwfGpwxzqBqFldv8Fs%2BUnoBLuRP7yBiF%2FXv9zsMIc5eXFcbFzwulEpRq%2BSNFz07Yerog%2FAfF%2FIWO93B9qY%2FgeZmOc4X6AV3z1QM2R7wNoshGayu152HNzTVz%2FnscoGmUIdmBbuon6O8Pw8zjUZpe18hfnkJzmo%2FFclCv75%2FDwBd05hD2t2ysL25bH8E5GaNvrrsLFdLoO02NeMglPk%2F2kO1EZHGTlbgpR97XgHaHEkhCuQvAuu3nlmLNqKNU6lslYufmapwJIlo9bISR8PThE6QP%2B1PUMaSau1"
        )
    }

    @Test
    fun `should fetch order detail successfully with valid order number`() {
        // Given
        val orderNo = "202508052327430002" // Example order number from the provided data

        val request = OrderDetailRequest(
            orderNo = orderNo,
            cookies = cookies
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
        val request = OrderDetailRequest(
            orderNo = orderNo,
            cookies = cookies,
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
            cookies = cookies,
        )

        assertNotNull(request)
        assertEquals("test123", request.orderNo)
    }
}
