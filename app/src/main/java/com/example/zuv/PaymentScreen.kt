package com.example.zuv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zuv.ui.theme.ZUVTheme

data class PaymentMethod(
    val id: Int,
    val cardType: String,
    val last4Digits: String
)

val samplePaymentMethods = listOf(
    PaymentMethod(1, "Visa", "1234"),
    PaymentMethod(2, "MasterCard", "5678")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("결제") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Add new card */ }) {
                Icon(Icons.Default.Add, contentDescription = "결제 수단 추가")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(samplePaymentMethods) { method ->
                PaymentItem(method)
            }
        }
    }
}

@Composable
fun PaymentItem(method: PaymentMethod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CreditCard,
                contentDescription = "카드 아이콘",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(method.cardType, fontWeight = FontWeight.Bold)
                Text("**** **** **** ${method.last4Digits}")
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    ZUVTheme {
        PaymentScreen()
    }
}