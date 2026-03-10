package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.detail_reservation.model.RefundReason
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReservationCancelDialog(
    status: ReservationStatus,
    refundReason: RefundReason?,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonButtonModal(
        modifier = modifier,
        onDismissRequest = onDismiss,
        cancelText = stringResource(R.string.reservation_cancel_dialog_button_no),
        confirmText = stringResource(R.string.reservation_cancel_dialog_button_yes),
        onCancel = onCancel,
        onConfirm = onConfirm,
    ) {
        ReservationCancelDialogContent(
            status = status,
            refundReason = refundReason,
        )
    }
}

@Composable
private fun ReservationCancelDialogContent(
    status: ReservationStatus,
    refundReason: RefundReason?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.reservation_cancel_dialog_title),
            style = pretendardTypography.titleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (status == ReservationStatus.RESERVED && refundReason != null) {
            Text(
                text = getPartialRefundAnnotatedText(refundPercent = refundReason.percent),
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = getDescriptionText(status),
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun getDescriptionText(status: ReservationStatus): String =
    when (status) {
        ReservationStatus.WAITING_APPROVAL -> {
            stringResource(R.string.reservation_cancel_dialog_desc_waiting_approval)
        }

        ReservationStatus.WAITING_PAYMENT -> {
            stringResource(R.string.reservation_cancel_dialog_desc_full_refund)
        }

        ReservationStatus.RESERVED -> {
            stringResource(R.string.reservation_cancel_dialog_desc_partial_refund, 90)
        }

        ReservationStatus.COMPLETED -> {
            ""
        }
    }

@Composable
private fun getPartialRefundAnnotatedText(refundPercent: Int) =
    buildAnnotatedString {
        append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_prefix))
        withStyle(style = SpanStyle(color = MainThemeColor.Red)) {
            append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_highlight, refundPercent))
        }
        append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_suffix))
    }

@Preview
@Composable
private fun ReservationCancelDialogWaitingApprovalPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.WAITING_APPROVAL,
        refundReason = null,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
    )
}

@Preview
@Composable
private fun ReservationCancelDialogFullRefundPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.WAITING_PAYMENT,
        refundReason = null,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
    )
}

@Preview
@Composable
private fun ReservationCancelDialogPartialRefundPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.RESERVED,
        refundReason = RefundReason.Before3Days,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
    )
}
