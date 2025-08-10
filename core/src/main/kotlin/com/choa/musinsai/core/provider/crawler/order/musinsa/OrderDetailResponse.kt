package com.choa.musinsai.core.provider.crawler.order.musinsa

import java.time.LocalDateTime

data class OrderDetailResponse(
    val orderNo: String,
    val orderDate: LocalDateTime,
    val orderState: OrderState,
    val orderItems: List<OrderItem>,
    val paymentInfo: PaymentInfo,
    val deliveryInfo: DeliveryInfo,
    val priceInfo: PriceInfo,
    val userInfo: UserInfo,
    val isOrderDeleteEnabled: Boolean = false
)

data class OrderItem(
    val orderOptionNo: Long,
    val goodsNo: Long,
    val goodsName: String,
    val goodsImage: String,
    val brandName: String,
    val brandId: String,
    val goodsOption: String,
    val quantity: Int,
    val receiveAmount: Long,
    val orderState: OrderState,
    val deliveryCompanyCode: String? = null,
    val deliveryInvoiceNo: String? = null,
    val buttonStatus: OrderButtonStatus,
    val isExchangeOption: Boolean = false,
    val isDigitalTicket: Boolean = false,
    val isDigitalVoucher: Boolean = false
)

data class PaymentInfo(
    val payKind: String,
    val payKindName: String,
    val cardName: String? = null,
    val cardQuota: String? = null,
    val totalAmount: Long,
    val receiptPageUrl: String? = null
)

data class DeliveryInfo(
    val recipientName: String,
    val recipientPhone: String,
    val recipientMobile: String,
    val zipCode: String,
    val address1: String,
    val address2: String,
    val deliveryMessage: String? = null
)

data class PriceInfo(
    val totalGoodsAmount: Long,
    val totalBenefitAmount: Long,
    val totalSaleAmount: Long,
    val totalDcAmount: Long,
    val couponAmount: Long,
    val pointAmount: Long,
    val usePointAmount: Long,
    val addPoint: Long,
    val receiveAmount: Long
)

data class UserInfo(
    val userId: String,
    val userName: String,
    val userPhone: String,
    val userEmail: String,
    val groupLevel: Int,
    val groupName: String
)

data class OrderButtonStatus(
    val orderCancelEnabled: Boolean = false,
    val cancelRequestEnabled: Boolean = false,
    val returnRequestEnabled: Boolean = false,
    val exchangeRequestEnabled: Boolean = false,
    val deliverySearchEnabled: Boolean = false,
    val reorderEnabled: Boolean = false,
    val orderConfirmEnabled: Boolean = false,
    val reviewWriteEnabled: Boolean = false
)

enum class OrderState(val code: Int, val text: String) {
    ORDER_COMPLETE(10, "주문 완료"),
    PAYMENT_COMPLETE(20, "결제 완료"),
    PREPARING_SHIPMENT(30, "배송 준비중"),
    SHIPPING(40, "배송중"),
    DELIVERED(45, "배송 완료"),
    PURCHASE_CONFIRMED(50, "구매 확정"),
    CANCEL_REQUESTED(60, "취소 요청"),
    CANCELLED(70, "취소 완료"),
    RETURN_REQUESTED(80, "반품 요청"),
    RETURNED(90, "반품 완료"),
    EXCHANGE_REQUESTED(100, "교환 요청"),
    EXCHANGED(110, "교환 완료");

    companion object {
        fun fromCode(code: Int): OrderState {
            return values().find { it.code == code } 
                ?: throw IllegalArgumentException("Unknown order state code: $code")
        }
    }
}
