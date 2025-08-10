package com.choa.musinsai.api.controller

import com.choa.musinsai.core.crawler.recentlyviewed.RecentlyViewedBrandResponse
import com.choa.musinsai.core.service.RecentlyViewedService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recently-viewed")
class RecentlyViewedController(
    private val recentlyViewedService: RecentlyViewedService
) {
    
    companion object {
        private val logger = LoggerFactory.getLogger(RecentlyViewedController::class.java)
    }
    
    @GetMapping("/brands")
    suspend fun getRecentlyViewedBrands(
        @CookieValue cookies: Map<String, String>?
    ): ResponseEntity<RecentlyViewedBrandResponse> {
        logger.info("최근 본 브랜드 조회 API 호출")
        
        return try {
            val response = recentlyViewedService.getRecentlyViewedBrands(cookies)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("최근 본 브랜드 조회 실패", e)
            ResponseEntity.internalServerError().build()
        }
    }
}