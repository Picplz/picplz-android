package com.hm.picplz.ui.screen.common.common_chip

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.SetCalculatedWidth
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.SetChipMode
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.SetIsEditing
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.SetTextFieldWidth
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.SetValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CommonChipViewModel : ViewModel() {
    private val _state = MutableStateFlow(CommonChipState.idle())
    val state: StateFlow<CommonChipState> = _state

    fun handleIntent(intent: CommonChipIntent) {
        when (intent) {
            is SetValue -> {
                _state.update { it.copy(value = intent.value) }
            }
            is SetChipMode -> {
                _state.update { it.copy(chipMode = intent.newChipMode) }
            }
            is SetTextFieldWidth -> {
                _state.update { it.copy(textFieldWidth = intent.newWidth) }
            }
            is SetCalculatedWidth -> {
                _state.update { it.copy(calculatedWidth = intent.newWidth) }
            }
            is SetIsEditing -> {
                _state.update { it.copy(isEditing = intent.isEditing) }
            }
        }
    }
}
