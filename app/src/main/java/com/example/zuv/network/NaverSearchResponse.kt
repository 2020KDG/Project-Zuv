package com.example.zuv.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// 네이버 개발자 센터 "검색" API의 응답을 담는 데이터 클래스
data class NaverSearchResponse(
    val items: List<SearchItem>
)

@Parcelize
data class SearchItem(
    val title: String,          // 장소 이름 (HTML 태그 포함)
    val roadAddress: String,    // 도로명 주소
    @SerializedName("mapx")
    val mapX: String,           // TM128 좌표계 X
    @SerializedName("mapy")
    val mapY: String            // TM128 좌표계 Y
) : Parcelable {
    // HTML 태그를 제거하여 순수한 장소 이름만 반환하는 편의 속성
    val formattedTitle: String
        get() = title.replace(Regex("<.*?>"), "")
}