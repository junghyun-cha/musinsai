package com.choa.musinsai.core.crawler.order.musinsa

import com.choa.musinsai.core.crawler.order.*
import com.choa.musinsai.core.exception.CrawlerException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.component1
import kotlin.collections.component2

@Component
class MusinsaOrderCrawler(
    private val webClient: WebClient
) : OrderCrawler {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    override suspend fun getOrderDetail(request: OrderDetailRequest): OrderDetailResponse {
        logger.info("Fetching order detail for orderNo: ${request.orderNo}")

        return try {
            val response = fetchOrderDetail(request.orderNo, request)
            mapToOrderDetailResponse(response)
        } catch (e: WebClientResponseException) {
            logger.error("Failed to fetch order detail: ${e.statusCode} - ${e.responseBodyAsString}")
            throw CrawlerException("Failed to fetch order detail: ${e.message}")
        } catch (e: Exception) {
            logger.error("Unexpected error while fetching order detail", e)
            throw CrawlerException("Failed to fetch order detail: ${e.message}")
        }
    }

    private suspend fun fetchOrderDetail(orderNo: String, request: OrderDetailRequest): MusinsaOrderDetailApiResponse {
        val url = "https://www.musinsa.com/order-service/my/order/get_order_view/$orderNo"

        val requestSpec = webClient.get()
            .uri(url)
            .header("accept", "application/json, text/plain, */*")
            .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
            .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36")
            .header("referer", "https://www.musinsa.com/")
            .apply {
                // 쿠키가 있으면 추가
                request.cookies?.forEach { (name, value) ->
                    cookie(name, value)
                }
            }

        val responseBody = requestSpec
            .retrieve()
            .bodyToMono(String::class.java)
            .onErrorResume { error ->
                logger.error("Error fetching order detail", error)
                Mono.error(error)
            }
            .awaitFirstOrNull()

        if (responseBody.isNullOrEmpty()) {
            throw CrawlerException("Empty response from order detail API")
        }

        return objectMapper.readValue(responseBody)
    }

    private fun mapToOrderDetailResponse(apiResponse: MusinsaOrderDetailApiResponse): OrderDetailResponse {
        val orderList = apiResponse.orderList
        val orderInfo = apiResponse.orderInfo

        // Parse order date
        val orderDate = parseOrderDate(orderInfo.ordDate)

        // Map order items
        val orderItems = orderList.orderOptionList.map { option ->
            OrderItem(
                orderOptionNo = option.orderOptionNo,
                goodsNo = option.goodsNo,
                goodsName = option.goodsName,
                goodsImage = normalizeImageUrl(option.goodsImage),
                brandName = option.brandName,
                brandId = option.brandId,
                goodsOption = option.goodsOption,
                quantity = option.quantity,
                receiveAmount = option.receiveAmount.toLongOrNull() ?: 0L,
                orderState = OrderState.fromCode(option.orderState),
                deliveryCompanyCode = option.deliveryCompanyCode,
                deliveryInvoiceNo = option.deliveryInvoiceNo,
                buttonStatus = mapButtonStatus(option.buttonStatus),
                isExchangeOption = option.isExchangeOption,
                isDigitalTicket = option.isDigitalTicket,
                isDigitalVoucher = option.isDigitalVoucher
            )
        }

        // Map payment info
        val paymentInfo = PaymentInfo(
            payKind = orderInfo.payKind,
            payKindName = orderInfo.payKindName,
            cardName = orderInfo.cardName.takeIf { it.isNotEmpty() },
            cardQuota = orderInfo.cardQuota.takeIf { it.isNotEmpty() },
            totalAmount = orderInfo.recvAmt.toLongOrNull() ?: 0L,
            receiptPageUrl = orderInfo.receiptPageUrl.takeIf { it.isNotEmpty() }
        )

        // Map delivery info
        val deliveryInfo = DeliveryInfo(
            recipientName = orderInfo.rNm,
            recipientPhone = orderInfo.rPhone,
            recipientMobile = orderInfo.rMobile,
            zipCode = orderInfo.rZipcode,
            address1 = orderInfo.rAddr1,
            address2 = orderInfo.rAddr2,
            deliveryMessage = orderInfo.dlvMsg.takeIf { it.isNotEmpty() }
        )

        // Map price info
        val priceInfo = PriceInfo(
            totalGoodsAmount = orderInfo.totalGoodsAmt.toLongOrNull() ?: 0L,
            totalBenefitAmount = orderInfo.totalBenefitAmt.toLongOrNull() ?: 0L,
            totalSaleAmount = orderInfo.totalSaleAmt.toLongOrNull() ?: 0L,
            totalDcAmount = orderInfo.dcAmt.toLongOrNull() ?: 0L,
            couponAmount = orderInfo.couponAmt.toLongOrNull() ?: 0L,
            pointAmount = orderInfo.pointAmt.toLongOrNull() ?: 0L,
            usePointAmount = orderInfo.usePointAmt.toLongOrNull() ?: 0L,
            addPoint = orderInfo.addPoint.toLongOrNull() ?: 0L,
            receiveAmount = orderInfo.recvAmt.toLongOrNull() ?: 0L
        )

        // Map user info
        val userInfo = UserInfo(
            userId = orderInfo.userId,
            userName = orderInfo.userNm,
            userPhone = orderInfo.phone,
            userEmail = orderInfo.email,
            groupLevel = orderInfo.groupLevel.toIntOrNull() ?: 0,
            groupName = orderInfo.group.groupName
        )

        // Determine overall order state
        val orderState = if (orderItems.isNotEmpty()) {
            orderItems.first().orderState
        } else {
            OrderState.fromCode(orderInfo.ordState.toIntOrNull() ?: 0)
        }

        return OrderDetailResponse(
            orderNo = orderList.orderNo,
            orderDate = orderDate,
            orderState = orderState,
            orderItems = orderItems,
            paymentInfo = paymentInfo,
            deliveryInfo = deliveryInfo,
            priceInfo = priceInfo,
            userInfo = userInfo,
            isOrderDeleteEnabled = orderList.orderDeleteEnabled
        )
    }

    private fun mapButtonStatus(status: MusinsaButtonStatus): OrderButtonStatus {
        return OrderButtonStatus(
            orderCancelEnabled = status.orderCancelEnabled,
            cancelRequestEnabled = status.cancelRequestEnabled,
            returnRequestEnabled = status.returnRequestEnabled,
            exchangeRequestEnabled = status.exchangeRequestEnabled,
            deliverySearchEnabled = status.deliverySearchEnabled,
            reorderEnabled = status.reorderEnabled,
            orderConfirmEnabled = status.orderConfirmEnabled,
            reviewWriteEnabled = status.reviewWriteEnabled
        )
    }

    private fun parseOrderDate(dateString: String): LocalDateTime {
        return try {
            // Format: "2025-04-01 08:45:42"
            LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            logger.warn("Failed to parse order date: $dateString", e)
            LocalDateTime.now()
        }
    }

    private fun normalizeImageUrl(imageUrl: String): String {
        return when {
            imageUrl.startsWith("//") -> "https:$imageUrl"
            imageUrl.startsWith("http") -> imageUrl
            else -> "https://image.msscdn.net$imageUrl"
        }
    }
}
