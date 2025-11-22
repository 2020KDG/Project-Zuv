package com.example.zuv.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverApiService {

    // 주소 -> 좌표 변환
    @GET("map-geocode/v2/geocode")
    suspend fun geocode(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("query") query: String,
        @Query("count") count: Int = 10 // 검색 결과 개수 (기본 10개)
    ): GeocodingResponse

    // 경로 탐색 (자동차)
    @GET("map-direction/v1/driving")
    suspend fun getDrivingRoute(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
        @Query("option") option: String = "trafast"
    ): DirectionsResponse
}