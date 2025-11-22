package com.example.zuv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.zuv.ui.theme.ZUVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 앱이 시작될 때 LocationManager를 초기화합니다.
        LocationManager.initialize(this)
        
        setContent {
            ZUVTheme {
                AppNavigation()
            }
        }
    }
}