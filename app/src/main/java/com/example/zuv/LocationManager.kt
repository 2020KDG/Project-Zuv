package com.example.zuv

import android.app.Activity
import com.naver.maps.map.util.FusedLocationSource

object LocationManager {
    lateinit var locationSource: FusedLocationSource
        private set

    fun initialize(activity: Activity) {
        // 앱이 실행되는 동안 단 한 번만 초기화되도록 보장
        if (!::locationSource.isInitialized) {
            locationSource = FusedLocationSource(activity, 1000)
        }
    }
}