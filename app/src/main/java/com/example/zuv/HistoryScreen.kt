package com.example.zuv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zuv.ui.theme.ZUVTheme

data class RideHistory(
    val id: Int,
    val date: String,
    val origin: String,
    val destination: String,
    val fare: String
)

val sampleHistory = listOf(
    RideHistory(1, "2024년 7월 28일", "강남역", "판교역", "5,000원"),
    RideHistory(2, "2024년 7월 27일", "홍대입구역", "서울역", "3,500원"),
    RideHistory(3, "2024년 7월 25일", "삼성역", "잠실역", "2,000원")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("이용 기록") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleHistory) { ride ->
                HistoryItem(ride)
            }
        }
    }
}

@Composable
fun HistoryItem(ride: RideHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(ride.date, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("출발:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(ride.origin, modifier = Modifier.weight(3f))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("도착:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(ride.destination, modifier = Modifier.weight(3f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("요금: ${ride.fare}", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    ZUVTheme {
        HistoryScreen()
    }
}