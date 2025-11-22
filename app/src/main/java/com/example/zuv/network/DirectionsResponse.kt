package com.example.zuv.network

import com.google.gson.annotations.SerializedName

// 네이버 Directions API의 응답을 담기 위한 데이터 클래스
data class DirectionsResponse(
    val code: Int,
    val message: String,
    val route: Route?
)

data class Route(
    @SerializedName("trafast") // "trafast"는 "실시간 빠른 길" 옵션을 의미
    val trafast: List<TrafficSummary>?
)

data class TrafficSummary(
    val summary: Summary,
    val path: List<List<Double>> // [[경도, 위도], [경도, 위도], ...] 형태의 좌표 리스트
)

data class Summary(
    val distance: Int,      // 총 거리 (미터)
    val duration: Int,      // 예상 시간 (밀리초)
    val taxiFare: Int,      // 예상 택시 요금
    val start: Location,
    val goal: Location
)

data class Location(
    val location: List<Double>
)