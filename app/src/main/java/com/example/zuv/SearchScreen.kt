package com.example.zuv

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zuv.network.SearchItem
import com.example.zuv.network.SearchRetrofitClient
import com.example.zuv.ui.theme.ZUVTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchItem>>(emptyList()) }

    // --- 네이버 개발자 센터 "검색" API 키 ---
    val clientId = "O0cu85XKiYwxBqJoPRuZ"
    val clientSecret = "szlLeuA1nk"

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            searchResults = emptyList()
            return@LaunchedEffect
        }
        delay(500)

        try {
            // "검색" API 호출
            val response = SearchRetrofitClient.instance.searchLocation(
                clientId = clientId,
                clientSecret = clientSecret,
                query = searchQuery
            )
            searchResults = response.items
        } catch (e: Exception) {
            Log.e("SearchScreen", "Search API Call Failed: ${e.message}")
            searchResults = emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("목적지 검색") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("어디로 갈까요?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(searchResults) { result ->
                    ListItem(
                        headlineContent = { Text(result.formattedTitle) },
                        supportingContent = { Text(result.roadAddress) },
                        modifier = Modifier.clickable {
                            // TODO: SearchItem을 MainScreen으로 전달하여 지도/경로 표시하기
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_search_item", result) // 다른 키 사용
                            navController.popBackStack()
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    ZUVTheme {
        SearchScreen(navController = rememberNavController())
    }
}