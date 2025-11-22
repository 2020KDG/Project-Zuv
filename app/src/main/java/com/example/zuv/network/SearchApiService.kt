package com.example.zuv.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 네이버 개발자 센터 "검색" API 인터페이스
interface SearchApiService {

    @GET("v1/search/local.json")
    suspend fun searchLocation(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 10
    ): NaverSearchResponse
}