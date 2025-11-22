package com.example.zuv

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zuv.ui.theme.ZUVTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerServiceScreen(onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("고객센터") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            ServiceMenuItem(
                icon = Icons.Default.HelpOutline,
                text = "자주 묻는 질문 (FAQ)",
                onClick = { /* TODO */ }
            )
            HorizontalDivider()
            ServiceMenuItem(
                icon = Icons.Default.QuestionAnswer,
                text = "문의하기",
                onClick = { /* TODO */ }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun ServiceMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerServiceScreenPreview() {
    ZUVTheme {
        CustomerServiceScreen()
    }
}