package com.example.zuv.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// NCP Geocoding API의 응답을 담기 위한 데이터 클래스
data class GeocodingResponse(
    val status: String,
    val meta: Meta,
    val addresses: List<Address>,
    val errorMessage: String
)

data class Meta(
    val totalCount: Int,
    val page: Int,
    val count: Int
)

@Parcelize
data class Address(
    val roadAddress: String,    // 도로명 주소
    val jibunAddress: String,   // 지번 주소
    val englishAddress: String,
    val x: String,              // 경도 (Longitude)
    val y: String               // 위도 (Latitude)
) : Parcelable