package com.choa.musinsai.core.provider.crawler.auth.musinsa

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class MusinsaAuthCrawlerTest {

    @Autowired
    private lateinit var crawler: MusinsaAuthCrawler

    @Test
    fun getAuthInfo() {
        val cookies = mutableMapOf<String, String>()
        cookies.put(MusinsaAuthCrawler.REFRESH_TOKEN_KEY, "63bbf83ebbb90e022731735d9b0776d8ad61bf5b")
        cookies.put(
            MusinsaAuthCrawler.ACCESS_TOKEN_KEY,
            "sJ5K8RVvBw6SQT5kHCkkCMvJ6Gbqe6n4b8XX5hlxpmfg6c%2Fts41qwlhCo5TWxyirpUG5DaxBsM1880LVuSiSdpkHBkvBnZdf5YKSV9VdxcoSMAYeHrt5rVCp0af3kNzKEmuwcvIuqefae2fyWZ1n8gt6R1rYK8lihEF7eOmc%2B9rNDwTPN9JLJVCDisDhqslzTT7GfKtMd82PHR5gVyIkaZJc6I8oip7AvQwkm5GtoA%2FCLS7bPAjHRSmtlVacyh6de%2Fq11kkC37QXWsCMWWcAmS9ra7W16qBa5tE%2F0opuz77G0Q%2FKkfazlndcKE1RDnqUNl%2FIjri406tLpicSFETfh6kX%2FfSaz08dUbUcxCuBcovyGErMyn%2BDEcvHPfabpdwq3kJZjIiCa96jNCrNa0ZMXXEBy4AcbOCrR016vzGldBV8xqPOak8nTeQ7IEx6vxdNr8D6pmdAFdGelvZnjsViYqr1a4llINPwfGpwxzqBqFldv8Fs%2BUnoBLuRP7yBiF%2FXv9zsMIc5eXFcbFzwulEpRq%2BSNFz07Yerog%2FAfF%2FIWO93B9qY%2FgeZmOc4X6AV3z1QM2R7wNoshGayu152HNzTVz%2FnscoGmUIdmBbuon6O8Pw8zjUZpe18hfnkJzmo%2FFclCv75%2FDwBd05hD2t2ysL25bH8E5GaNvrrsLFdLoO02NeMglPk%2F2kO1EZHGTlbgpR97XgHaHEkhCuQvAuu3nlmLNqKNU6lslYufmapwJIlo9bISR8PThE6QP%2B1PUMaSau1"
        )
        val authInfo = crawler.getAuthInfo(cookies)
        println(authInfo)
    }
}
