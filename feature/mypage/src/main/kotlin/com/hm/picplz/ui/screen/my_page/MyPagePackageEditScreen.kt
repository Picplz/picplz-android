package com.hm.picplz.ui.screen.my_page

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonBackgroundTextField
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonDestructiveConfirmDialog
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import java.io.ByteArrayOutputStream

@Composable
fun MyPagePackageEditRoute(
    photographerId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPagePackageEditViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            uri?.let {
                val imageBytes = readPackageImageBytes(context, it)
                if (imageBytes != null) {
                    viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageImage(it.toString()))
                    viewModel.handleIntent(
                        MyPagePackageEditIntent.UploadPackageImage(
                            imageBytes = imageBytes,
                            filename = "package_${System.currentTimeMillis()}.jpg",
                        ),
                    )
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.package_edit_image_too_large),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

    LaunchedEffect(photographerId) {
        viewModel.handleIntent(MyPagePackageEditIntent.LoadPhotographer(photographerId))
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                MyPagePackageEditSideEffect.NavigateBack -> onNavigateBack()
                MyPagePackageEditSideEffect.LaunchImagePicker -> imagePickerLauncher.launch("image/*")
                is MyPagePackageEditSideEffect.ShowToast -> {
                    Toast.makeText(context, context.getString(effect.messageResId), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MyPagePackageEditScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPagePackageEditScreen(
    state: MyPagePackageEditState,
    onIntent: (MyPagePackageEditIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val clearFocusThen =
        remember(focusManager) {
            { action: () -> Unit ->
                focusManager.clearFocus()
                action()
            }
        }

    BackHandler {
        onIntent(MyPagePackageEditIntent.NavigateBack)
    }

    if (state.pendingDeletePackageId != null) {
        CommonDestructiveConfirmDialog(
            title = stringResource(R.string.package_edit_delete_dialog_title),
            description = stringResource(R.string.package_edit_delete_dialog_description),
            cancelText = stringResource(R.string.package_edit_dialog_cancel),
            confirmText = stringResource(R.string.package_edit_delete_dialog_confirm),
            onDismissRequest = { onIntent(MyPagePackageEditIntent.DismissDeleteDialog) },
            onConfirm = { onIntent(MyPagePackageEditIntent.ConfirmDeletePackage) },
        )
    }

    if (state.showUnsavedBackDialog) {
        CommonDestructiveConfirmDialog(
            title = stringResource(R.string.package_edit_unsaved_dialog_title),
            description = stringResource(R.string.package_edit_unsaved_dialog_description),
            cancelText = stringResource(R.string.package_edit_dialog_cancel),
            confirmText = stringResource(R.string.package_edit_unsaved_dialog_confirm),
            onDismissRequest = { onIntent(MyPagePackageEditIntent.DismissUnsavedBackDialog) },
            onConfirm = { onIntent(MyPagePackageEditIntent.ConfirmDiscardChanges) },
        )
    }

    var selectedMenuItem by remember { mutableStateOf<MyPagePackageItem?>(null) }
    selectedMenuItem?.let { item ->
        CommonModalBottomSheet(
            onDismissRequest = { selectedMenuItem = null },
            sheetMaxHeight = 160.dp,
            expandToMaxHeight = false,
        ) {
            PackageActionSheet(
                onEdit = {
                    selectedMenuItem = null
                    onIntent(MyPagePackageEditIntent.ClickEditPackage(item.id))
                },
                onDelete = {
                    selectedMenuItem = null
                    onIntent(MyPagePackageEditIntent.RequestDeletePackage(item.id))
                },
            )
        }
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(R.string.package_edit_title),
                onClickBack = { onIntent(MyPagePackageEditIntent.NavigateBack) },
            )
        },
        bottomBar = {
            if (state.editMode != MyPagePackageEditMode.List) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MainThemeColor.White)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                ) {
                    CommonBottomButton(
                        text = stringResource(R.string.package_edit_save),
                        onClick = { clearFocusThen { onIntent(MyPagePackageEditIntent.SavePackage) } },
                        enabled = state.isSaveEnabled,
                    )
                }
            }
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        if (state.editMode == MyPagePackageEditMode.List) {
            LazyColumn(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MainThemeColor.White)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { Spacer(modifier = Modifier.height(3.dp)) }
                item {
                    Text(
                        text = stringResource(R.string.package_edit_list_heading),
                        style = MainThemeFont.TitleSmall,
                        color = MainThemeColor.Black,
                    )
                }
                if (state.packages.size < MAX_PACKAGE_COUNT) {
                    item {
                        AddPackageCard(onClick = { onIntent(MyPagePackageEditIntent.ClickAddPackage) })
                    }
                }
                items(state.packages, key = { it.id }) { item ->
                    PackageCard(
                        item = item,
                        onMenuClick = { selectedMenuItem = item },
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MainThemeColor.White)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                        .padding(horizontal = 16.dp),
            ) {
                item { Spacer(modifier = Modifier.height(3.dp)) }
                item {
                    PackageForm(
                        state = state,
                        onIntent = onIntent,
                        clearFocusThen = clearFocusThen,
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun PackageCard(
    item: MyPagePackageItem,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val overflowContentDescription = stringResource(R.string.package_edit_overflow_content_description)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(5.dp))
                .padding(horizontal = 22.dp, vertical = 23.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.name,
                    style = MainThemeFont.TitleSmall,
                    color = MainThemeColor.Black,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_package_edit_kebab),
                    contentDescription = overflowContentDescription,
                    tint = MainThemeColor.Black,
                    modifier =
                        Modifier
                            .clickable(onClick = onMenuClick)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .size(20.dp),
                )
            }
            if (item.imageUri.isNotBlank()) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = stringResource(R.string.package_edit_package_image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(PACKAGE_IMAGE_ASPECT_RATIO)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MainThemeColor.Gray1),
                )
            }
            Text(
                text = stringResource(R.string.package_edit_price, item.price),
                style = MainThemeFont.Title,
                color = MainThemeColor.Black,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PackageMetadataRow(
                label = stringResource(R.string.package_edit_shooting_time_label),
                value =
                    stringResource(
                        R.string.package_edit_shooting_time_value,
                        packageDurationLabel(item.durationMinutes),
                    ),
                valueStyle = MainThemeFont.BodyBold,
            )
            PackageMetadataRow(
                label = stringResource(R.string.package_edit_extra_guide_label),
                value = item.description.ifBlank { stringResource(R.string.package_edit_empty_extra_guide) },
            )
        }
    }
}

@Composable
private fun PackageActionSheet(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(R.string.package_edit_menu_edit),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onEdit)
                    .padding(vertical = 18.dp),
            style = MainThemeFont.Body,
            color = MainThemeColor.Black,
        )
        HorizontalDivider(color = MainThemeColor.Gray2)
        Text(
            text = stringResource(R.string.package_edit_menu_delete),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDelete)
                    .padding(vertical = 18.dp),
            style = MainThemeFont.Body,
            color = MainThemeColor.Red,
        )
    }
}

@Composable
private fun PackageMetadataRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = MainThemeFont.Body,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = label,
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
        )
        Text(
            text = value,
            style = valueStyle,
            color = MainThemeColor.Black,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AddPackageCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AddPackagePlusIcon()
            Text(
                text = stringResource(R.string.package_edit_add_package),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AddPackagePlusIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(48.dp)) {
        val strokeWidth = 2.dp.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val lineInset = 1.dp.toPx()
        drawLine(
            color = Color(0xFF94A9B8),
            start = Offset(centerX, lineInset),
            end = Offset(centerX, size.height - lineInset),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color(0xFF94A9B8),
            start = Offset(lineInset, centerY),
            end = Offset(size.width - lineInset, centerY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun PackageForm(
    state: MyPagePackageEditState,
    onIntent: (MyPagePackageEditIntent) -> Unit,
    clearFocusThen: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PackageNameFieldGroup(
            label = stringResource(R.string.package_edit_name_label),
            requiredMark = stringResource(R.string.package_edit_required_mark),
            value = state.draft.name,
            onValueChange = { onIntent(MyPagePackageEditIntent.ChangePackageName(it)) },
            placeholder = stringResource(R.string.package_edit_name_placeholder),
            modifier = Modifier.padding(bottom = 8.dp),
        )
        BannerImageSection(
            imageUri = state.draft.imageUri,
            isUploading = state.isUploadingImage,
            onClick = { clearFocusThen { onIntent(MyPagePackageEditIntent.ClickPackageImage) } },
        )
        PackageDurationSection(
            selectedDurationMinutes = state.draft.durationMinutes,
            onOptionClick = { option -> clearFocusThen { onIntent(MyPagePackageEditIntent.SelectDuration(option)) } },
        )
        PackageDescriptionSection(
            value = state.draft.description,
            onValueChange = { onIntent(MyPagePackageEditIntent.ChangeDescription(it)) },
            placeholder = stringResource(R.string.package_edit_description_placeholder),
            counter = stringResource(R.string.package_edit_description_counter, state.draft.description.length),
        )
    }
}

@Composable
private fun PackageDurationSection(
    selectedDurationMinutes: Int?,
    onOptionClick: (MyPagePackageDurationOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.package_edit_duration_label),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Black,
            )
            Text(
                text = stringResource(R.string.package_edit_required_mark),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Red,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MyPagePackageDurationOption.entries.forEach { option ->
                DurationOptionButton(
                    option = option,
                    selected = selectedDurationMinutes == option.minutes,
                    onClick = { onOptionClick(option) },
                )
            }
        }
    }
}

@Composable
private fun PackageNameFieldGroup(
    label: String,
    requiredMark: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Black,
            )
            Text(
                text = requiredMark,
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Red,
            )
        }
        PackageNameTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
        )
    }
}

@Composable
private fun PackageNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    CommonBackgroundTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
    )
}

@Composable
private fun PackageDescriptionSection(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    counter: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.package_edit_description_label),
            style = MainThemeFont.ButtonDefault,
            color = MainThemeColor.Black,
        )
        PackageDescriptionTextArea(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            counter = counter,
        )
    }
}

@Composable
private fun PackageDescriptionTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    counter: String,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .height(136.dp)
                .background(MainThemeColor.Gray1, RoundedCornerShape(5.dp))
                .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(5.dp)),
        textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
        decorationBox = { innerTextField ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp),
            ) {
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Gray3,
                    )
                }
                innerTextField()
                Text(
                    text = counter,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    style = MainThemeFont.SmallTagCaption,
                    color = MainThemeColor.Gray3,
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationOptionButton(
    option: MyPagePackageDurationOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(20.dp)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DurationRadioIndicator(selected = selected)
            Text(
                text = packageDurationLabel(option.minutes),
                style = MainThemeFont.Body,
                color = if (selected) MainThemeColor.Black else MainThemeColor.Gray3,
            )
        }
        Text(
            text = stringResource(R.string.package_edit_price, option.price),
            modifier = Modifier.weight(1f),
            style = MainThemeFont.Body,
            color = if (selected) MainThemeColor.Black else MainThemeColor.Gray3,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun DurationRadioIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(20.dp)) {
        val strokeWidth = 1.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth / 2

        if (selected) {
            drawCircle(
                color = MainThemeColor.Black,
                radius = radius,
                style = Stroke(width = strokeWidth),
            )
            drawCircle(
                color = MainThemeColor.Black,
                radius = 5.dp.toPx(),
            )
        } else {
            drawCircle(
                color = MainThemeColor.Gray2,
                radius = radius,
            )
            drawCircle(
                color = MainThemeColor.Gray3,
                radius = radius,
                style = Stroke(width = strokeWidth),
            )
        }
    }
}

@Composable
private fun packageDurationLabel(minutes: Int): String =
    stringResource(
        when (minutes) {
            15 -> R.string.package_edit_duration_within_15
            30 -> R.string.package_edit_duration_15_to_30
            60 -> R.string.package_edit_duration_30_to_60
            else -> R.string.package_edit_duration_over_60
        },
    )

@Composable
private fun BannerImageSection(
    imageUri: String,
    isUploading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.package_edit_banner_image_title),
                style = MainThemeFont.ButtonDefault,
                color = MainThemeColor.Black,
            )
            Text(
                text = stringResource(R.string.package_edit_banner_image_description),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
            )
        }
        BannerImageBox(
            imageUri = imageUri,
            isUploading = isUploading,
            onClick = onClick,
        )
    }
}

@Composable
private fun BannerImageBox(
    imageUri: String,
    isUploading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(166.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1)
                .clickable(enabled = !isUploading, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUri.isNotBlank()) {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.package_edit_package_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else if (isUploading) {
            Text(
                text = stringResource(R.string.package_edit_image_uploading),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        } else {
            BannerImageAddContent()
        }
    }
}

@Composable
private fun BannerImageAddContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(R.string.package_edit_image_add),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
        BannerImagePlusIcon()
    }
}

@Composable
private fun BannerImagePlusIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(32.dp)) {
        val strokeWidth = 2.dp.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val lineInset = 1.dp.toPx()
        drawLine(
            color = Color(0xFF94A9B8),
            start = Offset(centerX, lineInset),
            end = Offset(centerX, size.height - lineInset),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color(0xFF94A9B8),
            start = Offset(lineInset, centerY),
            end = Offset(size.width - lineInset, centerY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

private const val MAX_PACKAGE_COUNT = 3
private const val MAX_PACKAGE_IMAGE_BYTES = 5 * 1024 * 1024
private const val PACKAGE_IMAGE_ASPECT_RATIO = 299f / 130f

private fun readPackageImageBytes(
    context: Context,
    uri: Uri,
): ByteArray? =
    context.contentResolver.openInputStream(uri)?.use { stream ->
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var totalBytes = 0
        while (true) {
            val readCount = stream.read(buffer)
            if (readCount == -1) break
            totalBytes += readCount
            if (totalBytes > MAX_PACKAGE_IMAGE_BYTES) return null
            output.write(buffer, 0, readCount)
        }
        output.toByteArray()
    }

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun MyPagePackageEditScreenPreview() {
    PicplzTheme {
        MyPagePackageEditScreen(
            state = MyPagePackageEditState.idle(),
            onIntent = {},
        )
    }
}
