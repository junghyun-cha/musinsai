package com.choa.musinsai.core.crawler.order

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// Request DTOs
data class OrderHistoryRequest(
    val size: Int = 15,
    val searchText: String? = null,
    val startDate: String,
    val endDate: String
)

// Response DTOs
@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderHistoryResponse(
    val data: List<Order>? = null,
    val meta: OrderMeta? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(
    val rootOrderNo: String? = null,
    val orderNo: String? = null,
    val orderDate: String? = null,
    val isOnline: Boolean? = null,
    val orderOptionList: List<OrderOption>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderOption(
    val orderState: Int? = null,
    val childOrderState: Int? = null,
    val claimState: Int? = null,
    val orderStateText: String? = null,
    val orderOptionNo: Long? = null,
    val orderNo: String? = null,
    val brandName: String? = null,
    val brandId: String? = null,
    val shopNo: Int? = null,
    val shopName: String? = null,
    val goodsNo: Long? = null,
    val goodsImage: String? = null,
    val pdpEnabled: Boolean? = null,
    val goodsName: String? = null,
    val goodsOptionNo: Long? = null,
    val originGoodsOption: String? = null,
    val goodsOption: String? = null,
    val quantity: Int? = null,
    val optionAmount: Int? = null,
    val receiveAmount: String? = null,
    val orderLogisticsGroupNo: Long? = null,
    val virtualAccountText: String? = null,
    val detailText: String? = null,
    val deliveryCompanyCode: String? = null,
    val deliveryInvoiceNo: String? = null,
    val isExchangeOption: Boolean? = null,
    val isDigitalTicket: Boolean? = null,
    val isDigitalVoucher: Boolean? = null,
    val representNoType: String? = null,
    val representNo: Long? = null,
    val plusDelivery: PlusDelivery? = null,
    val pickup: Any? = null,
    val buttonStatus: ButtonStatus? = null,
    val orderReviewContents: OrderReviewContents? = null,
    val addOptionList: List<Any>? = null,
    val voucherNumber: String? = null,
    val voucherShareMethod: String? = null,
    val voucherStatus: String? = null,
    val recipientName: String? = null,
    val recipientMobile: String? = null,
    val voucherLink: String? = null,
    val isCustomOrder: Boolean? = null,
    val isExchangeDisabled: Boolean? = null,
    val exchangeDisabledCompanyId: String? = null,
    val isPartnerAdOrderReturnable: Boolean? = null,
    val partnerAdName: String? = null,
    val isExperienceGroup: Boolean? = null,
    val accessibleProductDetail: Boolean? = null,
    val returnMasterNo: Long? = null,
    val returnDeliveryNo: String? = null,
    val returnDeliveryCompanyCode: String? = null,
    val returnDeliveryCompanyName: String? = null,
    val claimGoodsOptionNo: Long? = null,
    val claimGoodsOption: String? = null,
    val claimGoodsOptionQuantity: Int? = null,
    val claimOrderNo: String? = null,
    val claimOrderOptionNo: Long? = null,
    val claimAddOptionList: List<Any>? = null,
    val childOrderReviewContents: OrderReviewContents? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlusDelivery(
    val isPlusDelivery: Boolean? = null,
    val plusDeliveryText: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ButtonStatus(
    val nonPaymentOrderCancelEnabled: Boolean? = null,
    val orderCancelEnabled: Boolean? = null,
    val optionExchangeEnabled: Boolean? = null,
    val shippingProcessingCancelRequestEnabled: Boolean? = null,
    val digitalTicketCancelEnabled: Boolean? = null,
    val returnRequestEnabled: Boolean? = null,
    val exchangeRequestEnabled: Boolean? = null,
    val deliverySearchEnabled: Boolean? = null,
    val orderConfirmEnabled: Boolean? = null,
    val reviewWriteEnabled: Boolean? = null,
    val styleRecommendEnabled: Boolean? = null,
    val barcodeScanEnabled: Boolean? = null,
    val lockerOpenEnabled: Boolean? = null,
    val pickupCancelRequestEnabled: Boolean? = null,
    val inquiryRequestEnabled: Boolean? = null,
    val digitalVoucherCancelEnabled: Boolean? = null,
    val digitalVoucherShareEnabled: Boolean? = null,
    val digitalVoucherKakaoShareEnabled: Boolean? = null,
    val digitalVoucherMmsShareEnabled: Boolean? = null,
    val cancelRequestEnabled: Boolean? = null,
    val reorderEnabled: Boolean? = null,
    val cancelDetailEnabled: Boolean? = null,
    val exchangeDetailEnabled: Boolean? = null,
    val searchAccountEnabled: Boolean? = null,
    val cancelWithdrawEnabled: Boolean? = null,
    val shippingProcessingCancelWithdrawEnabled: Boolean? = null,
    val returnDetailEnabled: Boolean? = null,
    val registerRecoveryInvoiceEnabled: Boolean? = null,
    val modifyRecoveryInvoiceEnabled: Boolean? = null,
    val searchReturnDeliveryEnabled: Boolean? = null,
    val searchExchangeDeliveryEnabled: Boolean? = null,
    val collectionLocationChangeEnabled: Boolean? = null,
    val childOptionExchangeEnabled: Boolean? = null,
    val childReturnRequestEnabled: Boolean? = null,
    val childExchangeRequestEnabled: Boolean? = null,
    val childDeliverySearchEnabled: Boolean? = null,
    val childOrderConfirmEnabled: Boolean? = null,
    val childReviewWriteEnabled: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderReviewContents(
    val bubbleText: String? = null,
    val reviewDetailList: List<ReviewDetail>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewDetail(
    val reviewType: String? = null,
    val reviewTypeName: String? = null,
    val reviewDescription: String? = null,
    val reviewWriteLink: String? = null,
    val wrote: Boolean? = null,
    val activated: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderMeta(
    val result: String? = null,
    val errorCode: String? = null,
    val message: String? = null,
    val onlineOffset: String? = null,
    val offlineOffset: String? = null
)