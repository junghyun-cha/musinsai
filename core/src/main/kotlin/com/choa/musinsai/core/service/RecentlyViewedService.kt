package com.choa.musinsai.core.service

import com.choa.musinsai.core.crawler.recentlyviewed.RecentlyViewedBrandRequest
import com.choa.musinsai.core.crawler.recentlyviewed.RecentlyViewedBrandResponse
import com.choa.musinsai.core.crawler.recentlyviewed.RecentlyViewedCrawler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RecentlyViewedService(
    private val recentlyViewedCrawler: RecentlyViewedCrawler
) {
    
    companion object {
        private val logger = LoggerFactory.getLogger(RecentlyViewedService::class.java)
    }
    
    suspend fun getRecentlyViewedBrands(cookies: Map<String, String>? = null): RecentlyViewedBrandResponse {
        logger.info("최근 본 브랜드 조회 서비스 시작")
        
        val request = RecentlyViewedBrandRequest(cookies = cookies)
        
        return recentlyViewedCrawler.getRecentlyViewedBrands(request).also {
            logger.info("최근 본 브랜드 조회 완료: ${it.total}개 브랜드")
        }
    }
}