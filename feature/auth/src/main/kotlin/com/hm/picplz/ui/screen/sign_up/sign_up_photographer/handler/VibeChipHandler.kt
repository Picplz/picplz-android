package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler

import com.hm.picplz.common.model.ChipItem
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.AddVibeChip
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.DeleteVibeChip
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetEditingChipId
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateSelectedVibeChipList
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateVibeChip
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState

class VibeChipHandler {
    fun handleIntent(
        intent: SignUpPhotographerIntent,
        currentState: SignUpPhotographerState,
    ): SignUpPhotographerState? {
        return when (intent) {
            is SetEditingChipId -> {
                currentState.copy(editingChipId = intent.chipId)
            }

            is AddVibeChip -> {
                val maxId =
                    currentState.vibeChipList
                        .maxByOrNull { it.id.toIntOrNull() ?: 0 }?.id?.toIntOrNull() ?: 0
                val newId = (maxId + 1).toString()

                val newChip = ChipItem(id = newId, label = intent.label, isEditable = true)
                currentState.copy(vibeChipList = currentState.vibeChipList + newChip)
            }

            is DeleteVibeChip -> {
                currentState.copy(
                    vibeChipList = currentState.vibeChipList.filter { it.id != intent.chipId },
                )
            }

            is UpdateVibeChip -> {
                currentState.copy(
                    vibeChipList =
                        currentState.vibeChipList.map { chip ->
                            if (chip.id == intent.chipId) {
                                chip.copy(label = intent.label)
                            } else {
                                chip
                            }
                        },
                )
            }

            is UpdateSelectedVibeChipList -> {
                val updateSelectedChipList =
                    if (currentState.selectedVibeChipList.any { it.id == intent.chipId }) {
                        currentState.selectedVibeChipList.filter { it.id != intent.chipId }
                    } else {
                        currentState.selectedVibeChipList +
                            ChipItem(
                                id = intent.chipId,
                                label = intent.label,
                            )
                    }
                currentState.copy(selectedVibeChipList = updateSelectedChipList)
            }

            else -> null
        }
    }
}
