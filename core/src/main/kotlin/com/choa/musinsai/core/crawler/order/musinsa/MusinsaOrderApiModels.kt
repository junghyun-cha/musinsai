package com.choa.musinsai.core.crawler.order.musinsa

import com.fasterxml.jackson.annotation.JsonProperty

// API Response Models
data class MusinsaOrderDetailApiResponse(
    @JsonProperty("result") val result: String,
    @JsonProperty("orderList") val orderList: MusinsaOrderList,
    @JsonProperty("orderInfo") val orderInfo: MusinsaOrderInfo,
    @JsonProperty("refundOrderInfo") val refundOrderInfo: MusinsaRefundOrderInfo,
    @JsonProperty("isReactTarget") val isReactTarget: Boolean
)

data class MusinsaOrderList(
    @JsonProperty("orderNo") val orderNo: String,
    @JsonProperty("orderDate") val orderDate: String,
    @JsonProperty("orderOptionList") val orderOptionList: List<MusinsaOrderOption>,
    @JsonProperty("orderDeleteEnabled") val orderDeleteEnabled: Boolean
)

data class MusinsaOrderOption(
    @JsonProperty("orderState") val orderState: Int,
    @JsonProperty("childOrderState") val childOrderState: Int,
    @JsonProperty("claimState") val claimState: Int,
    @JsonProperty("orderStateText") val orderStateText: String,
    @JsonProperty("orderOptionNo") val orderOptionNo: Long,
    @JsonProperty("brandName") val brandName: String,
    @JsonProperty("brandId") val brandId: String,
    @JsonProperty("shopNo") val shopNo: Int,
    @JsonProperty("goodsNo") val goodsNo: Long,
    @JsonProperty("goodsImage") val goodsImage: String,
    @JsonProperty("goodsName") val goodsName: String,
    @JsonProperty("goodsOptionNo") val goodsOptionNo: Long,
    @JsonProperty("originGoodsOption") val originGoodsOption: String,
    @JsonProperty("goodsOption") val goodsOption: String,
    @JsonProperty("goodsOptionName") val goodsOptionName: String,
    @JsonProperty("quantity") val quantity: Int,
    @JsonProperty("optionAmount") val optionAmount: Long,
    @JsonProperty("receiveAmount") val receiveAmount: String,
    @JsonProperty("orderLogisticsGroupNo") val orderLogisticsGroupNo: Long,
    @JsonProperty("virtualAccountText") val virtualAccountText: String,
    @JsonProperty("detailText") val detailText: String,
    @JsonProperty("deliveryCompanyCode") val deliveryCompanyCode: String?,
    @JsonProperty("deliveryInvoiceNo") val deliveryInvoiceNo: String?,
    @JsonProperty("isExchangeOption") val isExchangeOption: Boolean,
    @JsonProperty("isDigitalTicket") val isDigitalTicket: Boolean,
    @JsonProperty("isDigitalVoucher") val isDigitalVoucher: Boolean,
    @JsonProperty("voucherNumber") val voucherNumber: String?,
    @JsonProperty("voucherShareMethod") val voucherShareMethod: String?,
    @JsonProperty("recipientName") val recipientName: String,
    @JsonProperty("voucherLink") val voucherLink: String,
    @JsonProperty("representNoType") val representNoType: String?,
    @JsonProperty("representNo") val representNo: Int,
    @JsonProperty("plusDelivery") val plusDelivery: MusinsaPlusDelivery,
    @JsonProperty("pickup") val pickup: Any?,
    @JsonProperty("buttonStatus") val buttonStatus: MusinsaButtonStatus,
    @JsonProperty("orderReviewContents") val orderReviewContents: Any?,
    @JsonProperty("isCustomOrder") val isCustomOrder: Boolean,
    @JsonProperty("isExchangeDisabled") val isExchangeDisabled: Boolean,
    @JsonProperty("exchangeDisabledCompanyId") val exchangeDisabledCompanyId: String,
    @JsonProperty("isPartnerAdOrderReturnable") val isPartnerAdOrderReturnable: Boolean,
    @JsonProperty("partnerAdName") val partnerAdName: String,
    @JsonProperty("isExperienceGroup") val isExperienceGroup: Boolean,
    @JsonProperty("addOptionList") val addOptionList: List<Any>,
    @JsonProperty("claimGoodsOptionNo") val claimGoodsOptionNo: Long,
    @JsonProperty("claimGoodsOption") val claimGoodsOption: String,
    @JsonProperty("claimGoodsOptionQuantity") val claimGoodsOptionQuantity: Int,
    @JsonProperty("claimOrderNo") val claimOrderNo: String?,
    @JsonProperty("claimOrderOptionNo") val claimOrderOptionNo: Long?,
    @JsonProperty("claimAddOptionList") val claimAddOptionList: List<Any>?,
    @JsonProperty("orderDeleteEnabled") val orderDeleteEnabled: Boolean
)

data class MusinsaPlusDelivery(
    @JsonProperty("isPlusDelivery") val isPlusDelivery: Boolean,
    @JsonProperty("plusDeliveryText") val plusDeliveryText: String
)

data class MusinsaButtonStatus(
    @JsonProperty("nonPaymentOrderCancelEnabled") val nonPaymentOrderCancelEnabled: Boolean,
    @JsonProperty("orderCancelEnabled") val orderCancelEnabled: Boolean,
    @JsonProperty("optionExchangeEnabled") val optionExchangeEnabled: Boolean,
    @JsonProperty("cancelRequestEnabled") val cancelRequestEnabled: Boolean,
    @JsonProperty("shippingProcessingCancelRequestEnabled") val shippingProcessingCancelRequestEnabled: Boolean,
    @JsonProperty("digitalTicketCancelEnabled") val digitalTicketCancelEnabled: Boolean,
    @JsonProperty("returnRequestEnabled") val returnRequestEnabled: Boolean,
    @JsonProperty("exchangeRequestEnabled") val exchangeRequestEnabled: Boolean,
    @JsonProperty("deliverySearchEnabled") val deliverySearchEnabled: Boolean,
    @JsonProperty("reorderEnabled") val reorderEnabled: Boolean,
    @JsonProperty("orderConfirmEnabled") val orderConfirmEnabled: Boolean,
    @JsonProperty("reviewWriteEnabled") val reviewWriteEnabled: Boolean,
    @JsonProperty("styleRecommendEnabled") val styleRecommendEnabled: Boolean,
    @JsonProperty("barcodeScanEnabled") val barcodeScanEnabled: Boolean,
    @JsonProperty("accountSearchEnabled") val accountSearchEnabled: Boolean,
    @JsonProperty("lockerOpenEnabled") val lockerOpenEnabled: Boolean,
    @JsonProperty("pickupCancelRequestEnabled") val pickupCancelRequestEnabled: Boolean,
    @JsonProperty("cancelDetailEnabled") val cancelDetailEnabled: Boolean,
    @JsonProperty("refundDetailEnabled") val refundDetailEnabled: Boolean,
    @JsonProperty("exchangeDetailEnabled") val exchangeDetailEnabled: Boolean,
    @JsonProperty("inquiryRequestEnabled") val inquiryRequestEnabled: Boolean,
    @JsonProperty("digitalVoucherCancelEnabled") val digitalVoucherCancelEnabled: Boolean,
    @JsonProperty("digitalVoucherShareEnabled") val digitalVoucherShareEnabled: Boolean,
    @JsonProperty("digitalVoucherKakaoShareEnabled") val digitalVoucherKakaoShareEnabled: Boolean,
    @JsonProperty("digitalVoucherMmsShareEnabled") val digitalVoucherMmsShareEnabled: Boolean
)

data class MusinsaOrderInfo(
    @JsonProperty("ord_state") val ordState: String,
    @JsonProperty("qty") val qty: String,
    @JsonProperty("opt_amt") val optAmt: String,
    @JsonProperty("addopt_amt") val addoptAmt: String,
    @JsonProperty("clm_state") val clmState: String?,
    @JsonProperty("ord_date") val ordDate: String,
    @JsonProperty("recv_amt") val recvAmt: String,
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("delete_yn") val deleteYn: String,
    @JsonProperty("point_type") val pointType: String,
    @JsonProperty("dc_type") val dcType: String,
    @JsonProperty("sale_amt") val saleAmt: String,
    @JsonProperty("normal_price") val normalPrice: String,
    @JsonProperty("dc_amt") val dcAmt: String,
    @JsonProperty("group_dc_amt") val groupDcAmt: String,
    @JsonProperty("coupon_amt") val couponAmt: String,
    @JsonProperty("cart_dc_amt") val cartDcAmt: String,
    @JsonProperty("cart_coupon_amt") val cartCouponAmt: String,
    @JsonProperty("point_amt") val pointAmt: String,
    @JsonProperty("pre_point_amt") val prePointAmt: String,
    @JsonProperty("use_point_amt") val usePointAmt: String,
    @JsonProperty("add_point") val addPoint: String,
    @JsonProperty("add_member_point") val addMemberPoint: String,
    @JsonProperty("add_purchase_point") val addPurchasePoint: String,
    @JsonProperty("total_goods_amt") val totalGoodsAmt: String,
    @JsonProperty("total_benefit_amt") val totalBenefitAmt: String,
    @JsonProperty("total_sale_amt") val totalSaleAmt: String,
    @JsonProperty("total_goods_sale_amt") val totalGoodsSaleAmt: String,
    @JsonProperty("sale_total_amt") val saleTotalAmt: String,
    @JsonProperty("total_dc_percent") val totalDcPercent: String,
    @JsonProperty("promotion_discount_amt") val promotionDiscountAmt: String,
    @JsonProperty("promotion_discount_code") val promotionDiscountCode: String,
    @JsonProperty("without_recv_amt_promotion_discount_amt") val withoutRecvAmtPromotionDiscountAmt: String,
    @JsonProperty("order_sum_qty") val orderSumQty: String,
    @JsonProperty("claim_sum_qty") val claimSumQty: String?,
    @JsonProperty("all_refuned") val allRefuned: String,
    @JsonProperty("all_order_cancel") val allOrderCancel: String,
    @JsonProperty("r_addr1") val rAddr1: String,
    @JsonProperty("r_addr2") val rAddr2: String,
    @JsonProperty("dlv_msg") val dlvMsg: String,
    @JsonProperty("r_zipcode") val rZipcode: String,
    @JsonProperty("goods_opt_kind_cd") val goodsOptKindCd: String,
    @JsonProperty("goods_form_type") val goodsFormType: String,
    @JsonProperty("pay_kind") val payKind: String,
    @JsonProperty("tno") val tno: String,
    @JsonProperty("payment_key") val paymentKey: String,
    @JsonProperty("pay_kind_name") val payKindName: String,
    @JsonProperty("card_quota") val cardQuota: String,
    @JsonProperty("card_name") val cardName: String,
    @JsonProperty("bank_code") val bankCode: String,
    @JsonProperty("nointf") val nointf: String,
    @JsonProperty("group_level") val groupLevel: String,
    @JsonProperty("shop_nm") val shopNm: String,
    @JsonProperty("shop_address1") val shopAddress1: String?,
    @JsonProperty("shop_address2") val shopAddress2: String?,
    @JsonProperty("phone") val phone: String,
    @JsonProperty("operation_hours") val operationHours: String?,
    @JsonProperty("map_img_url") val mapImgUrl: String,
    @JsonProperty("m_map_url") val mMapUrl: String?,
    @JsonProperty("total_sale_total_amt") val totalSaleTotalAmt: String,
    @JsonProperty("order_category") val orderCategory: String?,
    @JsonProperty("share_method") val shareMethod: String?,
    @JsonProperty("voucher_status") val voucherStatus: String?,
    @JsonProperty("musinsa_money") val musinsaMoney: MusinsaMoney,
    @JsonProperty("user_nm") val userNm: String,
    @JsonProperty("mobile") val mobile: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("r_nm") val rNm: String,
    @JsonProperty("r_phone") val rPhone: String,
    @JsonProperty("r_mobile") val rMobile: String,
    @JsonProperty("isZeroPay") val isZeroPay: Boolean,
    @JsonProperty("addDeliveryInfo") val addDeliveryInfo: MusinsaAddDeliveryInfo,
    @JsonProperty("receiptPageUrl") val receiptPageUrl: String,
    @JsonProperty("pay_info") val payInfo: String,
    @JsonProperty("ord_no") val ordNo: String,
    @JsonProperty("group") val group: MusinsaGroup,
    @JsonProperty("isExperienceGroup") val isExperienceGroup: Boolean
)

data class MusinsaMoney(
    @JsonProperty("basic") val basic: Int,
    @JsonProperty("promotion") val promotion: Int,
    @JsonProperty("first") val first: Int,
    @JsonProperty("total") val total: Int
)

data class MusinsaAddDeliveryInfo(
    @JsonProperty("total_amt") val totalAmt: Int,
    @JsonProperty("list") val list: List<Any>
)

data class MusinsaGroup(
    @JsonProperty("groupNo") val groupNo: Int,
    @JsonProperty("groupName") val groupName: String,
    @JsonProperty("maximumUsePointRatio") val maximumUsePointRatio: Int,
    @JsonProperty("levelSavePointRatio") val levelSavePointRatio: Int,
    @JsonProperty("levelPrePointRatio") val levelPrePointRatio: Int,
    @JsonProperty("discountRatio") val discountRatio: Int,
    @JsonProperty("pointRatio") val pointRatio: Int,
    @JsonProperty("pointLimitAmount") val pointLimitAmount: Int,
    @JsonProperty("isPointSave") val isPointSave: Boolean,
    @JsonProperty("isPointUse") val isPointUse: Boolean,
    @JsonProperty("isCouponUse") val isCouponUse: Boolean,
    @JsonProperty("orderGroupLabel") val orderGroupLabel: String,
    @JsonProperty("groupPoint") val groupPoint: Int
)

data class MusinsaRefundOrderInfo(
    @JsonProperty("musinsa_money") val musinsaMoney: MusinsaMoney,
    @JsonProperty("ord_date") val ordDate: String,
    @JsonProperty("ord_no") val ordNo: String,
    @JsonProperty("group") val group: MusinsaRefundGroup
)

data class MusinsaRefundGroup(
    @JsonProperty("orderGroupLabel") val orderGroupLabel: String,
    @JsonProperty("groupPoint") val groupPoint: Int,
    @JsonProperty("levelSavePointRatio") val levelSavePointRatio: String? = null
)