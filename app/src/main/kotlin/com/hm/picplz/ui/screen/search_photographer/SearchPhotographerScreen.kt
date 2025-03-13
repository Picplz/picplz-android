package com.hm.picplz.ui.screen.search_photographer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.ui.screen.common.CommonBottomSheetScaffold
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SearchPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonBottomSheetScaffold
import com.hm.picplz.ui.screen.search_photographer.composable.AddressMarker
import com.hm.picplz.ui.screen.search_photographer.composable.PhotographerListSheet
import com.hm.picplz.ui.screen.search_photographer.composable.PhotographerProfile
import com.hm.picplz.ui.screen.search_photographer.composable.PhotographerSheet
import com.hm.picplz.ui.screen.search_photographer.composable.RefetchButton
import com.hm.picplz.utils.LocationUtil.getDistance
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale", "UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val context = LocalContext.current
    val currentState = viewModel.state.collectAsState().value

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewModel.handleIntent(SearchPhotographerIntent.GetCurrentLocation)
            }

            else -> {
                Toast.makeText(
                    context,
                    "위치 권한이 필요합니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(SearchPhotographerIntent.GetCurrentLocation)
    }

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true,
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(currentState.selectedPhotographerId) {
        if (currentState.selectedPhotographerId != null) {
            val selectedOffset = currentState.randomOffsets[currentState.selectedPhotographerId]
            if (selectedOffset != null) {
                viewModel.handleIntent(
                    SearchPhotographerIntent.CenterSelectedPhotographer(
                        selectedOffset
                    )
                )
            }
            scaffoldState.bottomSheetState.expand()
        } else {
            viewModel.handleIntent(SearchPhotographerIntent.CenterSelectedPhotographer(Offset.Zero))
            scaffoldState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
            viewModel.handleIntent(SearchPhotographerIntent.SetSelectedPhotographerId(null))
        }
    }

    Scaffold(bottomBar = { BottomNavigationBar(navController = mainNavController) }) {
        CommonBottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            sheetContent = {
                if (currentState.selectedPhotographerId === null) {
                    PhotographerListSheet(mainNavController = mainNavController)
                } else {
                    PhotographerSheet(mainNavController = mainNavController)
                }
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = currentState.sheetPeekHeight,
            sheetMaxHeight = currentState.sheetMaxHeight
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MainThemeColor.White),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MainThemeColor.Gray1)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                                viewModel.handleIntent(
                                    SearchPhotographerIntent.SetSelectedPhotographerId(
                                        null
                                    )
                                )
                            }
                        },
                ) {
                    if (currentState.isFetchingGPS && currentState.userLocation == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MainThemeColor.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "위치 정보 로딩",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MainThemeColor.Black
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 5.dp, end = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AddressMarker(
                                address = currentState.address
                            )
                            RefetchButton(onClick = {
                                viewModel.handleIntent(SearchPhotographerIntent.RefetchNearbyPhotographers)
                            }
                            )
                        }

                        val boxOffset by animateOffsetAsState(
                            targetValue = currentState.centerOffset ?: Offset.Zero,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "boxAnimation"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(boxOffset.x.dp, boxOffset.y.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.multicircle),
                                contentDescription = "범위 지정 이미지",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .scale(1.5f)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.center_char),
                                contentDescription = "작가 탐색 중앙 캐릭터"
                            )
                            val entirePhotographer =
                                currentState.nearbyPhotographers.active + currentState.nearbyPhotographers.inactive
                            // val userLocation = currentState.userLocation
                            val dummyUserLocation = LatLng.from(37.402960, 127.115587)
                            entirePhotographer.forEach { (id, name, photographerLocation, profileImageUri, isActive) ->
                                val offset = currentState.randomOffsets[id] ?: return@forEach
                                val isSelected = id == currentState.selectedPhotographerId
                                val distanceInMeters = photographerLocation?.let { location ->
                                    getDistance(dummyUserLocation, location) * 1000
                                }
                                PhotographerProfile(
                                    name = name,
                                    profileImageUri = profileImageUri,
                                    isActive = isActive,
                                    isSelected = isSelected,
                                    offset = offset,
                                    distance = distanceInMeters,
                                    onClick = {
                                        scope.launch {
                                            scaffoldState.bottomSheetState.partialExpand()
                                            viewModel.handleIntent(
                                                SearchPhotographerIntent.SetSelectedPhotographerId(
                                                    id
                                                )
                                            )
                                            viewModel.handleIntent(
                                                SearchPhotographerIntent.CenterSelectedPhotographer(
                                                    offset
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest { sideEffect ->
                when (sideEffect) {
                    is SearchPhotographerSideEffect.NavigateToPrev -> {
                        mainNavController.popBackStack()
                    }
                }
                is SearchPhotographerSideEffect.RequestLocationPermission -> {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        }
    }
}