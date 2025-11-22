package com.example.zuv

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zuv.network.*
import com.example.zuv.ui.theme.ZUVTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    onNavigateToHistory: () -> Unit = {},
    onNavigateToPayment: () -> Unit = {},
    onNavigateToCustomerService: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // LocationManager에서 앱 전체에서 공유되는 단일 인스턴스를 가져옴
    val locationSource = remember { LocationManager.locationSource }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.566610, 126.978388), 16.0)
    }
    var trackingMode by remember { mutableStateOf(LocationTrackingMode.None) }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var destination by remember { mutableStateOf<Address?>(null) }
    var route by remember { mutableStateOf<DirectionsResponse?>(null) }
    var routePath by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    val ncpClientId = "d3e26b85p0"
    val ncpClientSecret = "eUFqN19gB6iKLEBgjxEBhCXerYKvuF5zbnlkWgVk"

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions -> if (permissions.values.any { it }) trackingMode = LocationTrackingMode.Follow }
    )

    LaunchedEffect(locationSource) {
        snapshotFlow { locationSource.lastLocation }
            .collect { location ->
                location?.let {
                    currentLocation = LatLng(it)
                }
            }
    }

    val navBackStackEntry = navController.currentBackStackEntry
    val selectedSearchItem = navBackStackEntry?.savedStateHandle?.get<SearchItem>("selected_search_item")

    LaunchedEffect(selectedSearchItem, currentLocation) {
        if (selectedSearchItem == null || currentLocation == null) {
            return@LaunchedEffect
        }

        try {
            if (selectedSearchItem.roadAddress.isBlank()) {
                Log.e("MainScreen", "SearchItem has a blank roadAddress. Aborting.")
                return@LaunchedEffect
            }
            
            val geocodeResponse = RetrofitClient.instance.geocode(
                clientId = ncpClientId,
                clientSecret = ncpClientSecret,
                query = selectedSearchItem.roadAddress
            )
            val preciseAddress = geocodeResponse.addresses.firstOrNull() ?: return@LaunchedEffect
            destination = preciseAddress

            val startLatLng = currentLocation ?: return@LaunchedEffect

            val directionsResponse = RetrofitClient.instance.getDrivingRoute(
                clientId = ncpClientId,
                clientSecret = ncpClientSecret,
                start = "${startLatLng.longitude},${startLatLng.latitude}",
                goal = "${preciseAddress.x},${preciseAddress.y}"
            )
            route = directionsResponse
            
            val path = directionsResponse.route?.trafast?.firstOrNull()?.path
            if (!path.isNullOrEmpty()) {
                routePath = path.map { LatLng(it[1], it[0]) }
                val bounds = LatLngBounds.from(routePath)
                cameraPositionState.animate(CameraUpdate.fitBounds(bounds, 100))
            }
        } catch (e: Exception) {
            Log.e("MainScreen", "API Chain Failed:", e)
        } finally {
            navBackStackEntry.savedStateHandle.remove<SearchItem>("selected_search_item")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White)) {
                        Icon(Icons.Default.Person, contentDescription = "프로필 사진", modifier = Modifier.size(60.dp).align(Alignment.Center), tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("사용자 이름", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                DrawerMenuItem(icon = Icons.Default.History, text = "이용 기록", onClick = onNavigateToHistory)
                DrawerMenuItem(icon = Icons.Default.Payment, text = "결제", onClick = onNavigateToPayment)
                DrawerMenuItem(icon = Icons.Default.HelpOutline, text = "고객센터", onClick = onNavigateToCustomerService)
                DrawerMenuItem(icon = Icons.Default.Settings, text = "설정", onClick = onNavigateToSettings)
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ZUV") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.apply { if (isClosed) open() else close() } } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "메뉴")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                NaverMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    locationSource = locationSource,
                    properties = MapProperties(locationTrackingMode = trackingMode),
                    uiSettings = MapUiSettings(isLocationButtonEnabled = false, isZoomControlEnabled = false),
                    onMapClick = { _, _ -> trackingMode = LocationTrackingMode.None }
                ) {
                    if (routePath.isNotEmpty()) {
                        PolylineOverlay(coords = routePath, width = 5.dp, color = Color.Blue)
                    }
                }
                AnimatedVisibility(visible = destination == null, modifier = Modifier.align(Alignment.BottomCenter)) {
                    LocationSearchCard(
                        onMyLocationClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            if (hasPermission) trackingMode = LocationTrackingMode.Follow
                            else permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                        },
                        onSearchClick = { navController.navigate(AppRoutes.SEARCH_SCREEN) }
                    )
                }
                AnimatedVisibility(visible = destination != null, modifier = Modifier.align(Alignment.BottomCenter)) {
                    RouteInfoCard(
                        destination = destination,
                        route = route,
                        onCancel = { 
                            destination = null
                            route = null
                            routePath = emptyList()
                        },
                        onCallRequest = { /* TODO */ }
                    )
                }
            }
        }
    }
}

// ... 이하 모든 코드는 이전과 동일 ...
@Composable
fun DrawerMenuItem(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = text) },
        label = { Text(text) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun LocationSearchCard(onMyLocationClick: () -> Unit, onSearchClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("어디로 갈까요?", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            SearchTextField("현재 위치", Icons.Default.MyLocation, onMyLocationClick)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            SearchTextField("목적지 검색", Icons.Default.Search, onSearchClick)
        }
    }
}

@Composable
fun RouteInfoCard(
    destination: Address?,
    route: DirectionsResponse?,
    onCancel: () -> Unit,
    onCallRequest: () -> Unit
) {
    val summary = route?.route?.trafast?.firstOrNull()?.summary
    val durationMinutes = summary?.duration?.let { TimeUnit.MILLISECONDS.toMinutes(it.toLong()) }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(destination?.roadAddress ?: "목적지 정보 없음", style = MaterialTheme.typography.titleLarge)
            summary?.let {
                Text("예상 시간: 약 ${durationMinutes}분", style = MaterialTheme.typography.bodyMedium)
                Text("예상 요금: ${it.taxiFare}원", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onCancel, modifier = Modifier.weight(1f)) { Text("취소") }
                Button(onClick = onCallRequest, modifier = Modifier.weight(2f)) { Text("동승 호출하기") }
            }
        }
    }
}

@Composable
fun SearchTextField(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ZUVTheme {
        MainScreen(rememberNavController())
    }
}