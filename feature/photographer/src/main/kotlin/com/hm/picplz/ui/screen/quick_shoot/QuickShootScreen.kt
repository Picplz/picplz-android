package com.hm.picplz.ui.screen.quick_shoot

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.ui.navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.AddressMarker
import com.hm.picplz.ui.screen.common.CommonBottomSheetScaffold
import com.hm.picplz.ui.screen.common.CommonGrayDragHandle
import com.hm.picplz.ui.screen.common.RefetchButton
import com.hm.picplz.ui.screen.quick_shoot.composable.PhotographerListSheet
import com.hm.picplz.ui.screen.quick_shoot.composable.PhotographerProfile
import com.hm.picplz.ui.screen.quick_shoot.composable.PhotographerSheet
import com.hm.picplz.ui.screen.quick_shoot.composable.QuickShootBottomButton
import com.hm.picplz.ui.screen.quick_shoot.composable.QuickShootSortBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale", "UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun QuickShootScreen(
    modifier: Modifier = Modifier,
    viewModel: QuickShootViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val context = LocalContext.current
    val currentState = viewModel.state.collectAsState().value

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    viewModel.handleIntent(QuickShootIntent.SetLocationPermissionGranted(true))
                    viewModel.handleIntent(QuickShootIntent.GetCurrentLocation)
                }

                else -> {
                    viewModel.handleIntent(QuickShootIntent.SetLocationPermissionGranted(false))
                    Toast.makeText(
                        context,
                        "위치 권한이 필요합니다",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

    LaunchedEffect(Unit) {
        val hasLocationPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED

        viewModel.handleIntent(QuickShootIntent.SetLocationPermissionGranted(hasLocationPermission))

        if (hasLocationPermission) {
            viewModel.handleIntent(QuickShootIntent.GetCurrentLocation)
        }
    }

    LaunchedEffect(currentState.userLocation) {
        val userLocation = currentState.userLocation
        if (userLocation != null && currentState.locationPermissionGranted) {
            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            viewModel.handleIntent(QuickShootIntent.GetAddress(userLocation))
        }
    }

    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true,
        )

    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
        )

    val modalSheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    val selectedPhotographer =
        currentState.selectedPhotographerId?.let { selectedId ->
            val allPhotographers = currentState.nearbyPhotographers.active + currentState.nearbyPhotographers.inactive
            allPhotographers.find { it.id == selectedId }
        }

    val isPermissionDenied = !currentState.locationPermissionGranted && currentState.hasRequestedPermission
    val isFirstEntry = !currentState.locationPermissionGranted && !currentState.hasRequestedPermission

    Scaffold(
        bottomBar = {
            if (currentState.locationPermissionGranted || isPermissionDenied) {
                BottomNavigationBar(navController = mainNavController)
            }
        },
    ) {
        if (isFirstEntry) {
            QuickShootLocationPermissionRationale(
                onNextClick = {
                    viewModel.handleIntent(QuickShootIntent.RequestLocationPermission())
                },
            )
        } else if (isPermissionDenied) {
            QuickShootPermissionDeniedContent(currentState = currentState)
        } else if (currentState.locationPermissionGranted) {
            CommonBottomSheetScaffold(
                modifier = Modifier.fillMaxSize(),
                sheetContent = {
                    PhotographerListSheet(
                        photographers = currentState.nearbyPhotographers,
                        selectedSortType = currentState.selectedSortType,
                        onSortClick = {
                            viewModel.handleIntent(QuickShootIntent.ToggleSortSheet(true))
                        },
                        onPhotographerClick = { id ->
                            mainNavController.navigate(DetailPhotographer(id.toInt()))
                        },
                    )
                },
                scaffoldState = scaffoldState,
                sheetPeekHeight = 76.dp,
                sheetMaxHeight = (LocalConfiguration.current.screenHeightDp * 0.9f).dp,
                navigationBarPadding = true,
            ) {
                Column(
                    modifier =
                        modifier
                            .fillMaxSize()
                            .background(MainThemeColor.White),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(color = MainThemeColor.Gray1)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.partialExpand()
                                        viewModel.handleIntent(
                                            QuickShootIntent.SetSelectedPhotographerId(
                                                null,
                                            ),
                                        )
                                        viewModel.handleIntent(QuickShootIntent.CenterSelectedPhotographer(Offset.Zero))
                                    }
                                },
                    ) {
                        if (currentState.isFetchingGPS && currentState.userLocation == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator(
                                        color = MainThemeColor.Black,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "위치 정보 로딩",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MainThemeColor.Black,
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier =
                                    modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, start = 5.dp, end = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AddressMarker(
                                    address = currentState.address,
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RefetchButton(
                                        onClick = {
                                            viewModel.handleIntent(QuickShootIntent.RefetchNearbyPhotographers)
                                        },
                                    )
                                }
                            }

                            val entirePhotographers =
                                currentState.nearbyPhotographers.active + currentState.nearbyPhotographers.inactive
                            val isEmpty =
                                entirePhotographers.isEmpty() &&
                                    !currentState.isSearchingPhotographer &&
                                    currentState.userLocation != null

                            if (isEmpty) {
                                QuickShootEmptyState()
                            } else {
                                val boxOffset by animateOffsetAsState(
                                    targetValue = currentState.centerOffset ?: Offset.Zero,
                                    animationSpec =
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow,
                                        ),
                                    label = "boxAnimation",
                                )

                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .offset(boxOffset.x.dp, boxOffset.y.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.multicircle),
                                        contentDescription = "범위 지정 이미지",
                                        contentScale = ContentScale.FillWidth,
                                        modifier =
                                            Modifier
                                                .scale(1.5f),
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.center_char),
                                        contentDescription = "작가 탐색 중앙 캐릭터",
                                    )
                                    currentState.nearbyPhotographers.active.forEach { photographer ->
                                        val offset =
                                            currentState.randomOffsets[photographer.id]
                                                ?: return@forEach
                                        val isSelected = photographer.id == currentState.selectedPhotographerId
                                        PhotographerProfile(
                                            name = photographer.name,
                                            profileImageUri = photographer.profileImageUri,
                                            isActive = photographer.isActive,
                                            isSelected = isSelected,
                                            offset = offset,
                                            distance = photographer.distance,
                                            onClick = {
                                                viewModel.handleIntent(
                                                    QuickShootIntent.SetSelectedPhotographerId(
                                                        photographer.id,
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (selectedPhotographer != null) {
                val sheetNestedScrollConnection =
                    remember {
                        object : NestedScrollConnection {
                            override fun onPostScroll(
                                consumed: Offset,
                                available: Offset,
                                source: NestedScrollSource,
                            ): Offset = available.copy(x = 0f, y = available.y.coerceAtLeast(0f))

                            override suspend fun onPostFling(
                                consumed: Velocity,
                                available: Velocity,
                            ): Velocity = available.copy(x = 0f, y = available.y.coerceAtLeast(0f))
                        }
                    }
                ModalBottomSheet(
                    onDismissRequest = {
                        viewModel.handleIntent(QuickShootIntent.SetSelectedPhotographerId(null))
                        viewModel.handleIntent(QuickShootIntent.CenterSelectedPhotographer(Offset.Zero))
                    },
                    sheetState = modalSheetState,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    containerColor = MainThemeColor.White,
                    scrimColor = MainThemeColor.Black.copy(alpha = 0.4f),
                    dragHandle = { CommonGrayDragHandle() },
                    modifier = Modifier.wrapContentHeight().nestedScroll(sheetNestedScrollConnection),
                ) {
                    PhotographerSheet(
                        photographer = selectedPhotographer,
                        onNavigateToDetail = { id ->
                            mainNavController.navigate(DetailPhotographer(id.toInt()))
                        },
                    )
                }
            }

            QuickShootSortBottomSheet(
                visible = currentState.showSortSheet,
                selectedSortType = currentState.selectedSortType,
                onDismiss = {
                    viewModel.handleIntent(QuickShootIntent.ToggleSortSheet(false))
                },
                onSelect = { sortType ->
                    viewModel.handleIntent(QuickShootIntent.SelectSortType(sortType))
                },
            )
        }
        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest { sideEffect ->
                when (sideEffect) {
                    is QuickShootSideEffect.NavigateToPrev -> {
                        mainNavController.popBackStack()
                    }
                    is QuickShootSideEffect.RequestLocationPermission -> {
                        launcher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickShootLocationPermissionRationale(onNextClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MainThemeColor.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 162.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_location_permission),
                contentDescription = "위치 권한 안내 아이콘",
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "앱 서비스 이용을 위해",
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = MainThemeColor.Green120)) {
                            append("위치 접근 권한")
                        }
                        withStyle(SpanStyle(color = MainThemeColor.Black)) {
                            append("이 필요해요")
                        }
                    },
                style = MainThemeFont.Title,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "더 나은 서비스 제공을 위해 권한을 요청드려요.\n동의하지 않아도 서비스를 이용할 수 있지만,\n다수 기능의 이용이 제한될 수 있어요.",
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }

        QuickShootBottomButton(
            text = "다음",
            onClick = onNextClick,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp, end = 20.dp, bottom = 47.dp),
            enabled = true,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickShootPermissionDeniedContent(currentState: QuickShootState) {
    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState =
                rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded,
                    skipHiddenState = true,
                ),
        )

    CommonBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            PhotographerListSheet(
                photographers = FilteredPhotographers(),
                selectedSortType = currentState.selectedSortType,
                onSortClick = {},
                onPhotographerClick = {},
                emptyContent = { LocationPermissionDeniedEmptyState() },
            )
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 76.dp,
        sheetMaxHeight = (LocalConfiguration.current.screenHeightDp * 0.9f).dp,
        navigationBarPadding = true,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MainThemeColor.Gray1),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 5.dp, end = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AddressMarker(address = "-")
                RefetchButton(
                    enabled = false,
                    onClick = {},
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LocationPermissionDeniedEmptyState()
            }
        }
    }
}

@Composable
private fun LocationPermissionDeniedEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "위치 접근 권한이",
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "꺼져 있어요",
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.empty_character),
            contentDescription = "위치 권한 꺼짐 캐릭터",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "설정 → 앱 → [픽플즈]에서",
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "위치 권한을 활성화 해주세요",
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun QuickShootEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.multicircle),
            contentDescription = "범위 표시",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.scale(1.5f),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "주변에 바로 촬영 중인",
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "작가가 없습니다",
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.no_photographer),
                contentDescription = "주변 작가 없음 캐릭터",
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "다른 지역으로",
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "이동해 보는 건 어때요?",
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}
