package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler

import com.hm.picplz.data.model.PhotographyExperience
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.CareerPeriod
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetCareerPeriod
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetHasPhotographyExperience
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetMonthValue
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetPhotographyExperience
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetSelectedSelector
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetUserPhotographyExperience
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetUserPhotographyVibe
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetYearValue
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState

class CareerHandler {
    fun handleIntent(
        intent: SignUpPhotographerIntent,
        currentState: SignUpPhotographerState
    ): SignUpPhotographerState? {
        return when (intent) {
            is SetHasPhotographyExperience -> {
                currentState.copy(
                    hasPhotographyExperience = if (currentState.hasPhotographyExperience == intent.hasExperience) {
                        null
                    } else {
                        intent.hasExperience
                    }
                )
            }

            is SetPhotographyExperience -> {
                currentState.copy(selectedPhotographyExperienceId = intent.photographyExperienceId)
            }

            is SetUserPhotographyExperience -> {
                val experience = when (currentState.selectedPhotographyExperienceId) {
                    "1" -> PhotographyExperience.PHOTO_MAJOR
                    "2" -> PhotographyExperience.INCOME_GENERATION
                    "3" -> PhotographyExperience.SNS_OPERATION
                    else -> null
                }

                experience?.let { newExperience ->
                    currentState.copy(
                        userInfo = currentState.userInfo.copy(
                            photographyExperience = newExperience
                        )
                    )
                } ?: currentState
            }

            is SetUserPhotographyVibe -> {
                currentState.copy(
                    userInfo = currentState.userInfo.copy(
                        photographyVibes = currentState.selectedVibeChipList.map { chip -> chip.label }
                    )
                )
            }

            is SetYearValue -> {
                currentState.copy(yearValue = intent.year)
            }

            is SetMonthValue -> {
                currentState.copy(monthValue = intent.month)
            }

            is SetCareerPeriod -> {
                currentState.copy(
                    careerPeriod = if (currentState.yearValue != null && currentState.monthValue != null) {
                        CareerPeriod(years = currentState.yearValue, months = currentState.monthValue)
                    } else null
                )
            }

            is SetSelectedSelector -> {
                currentState.copy(selectedSelector = intent.selectedSelector)
            }

            else -> null
        }
    }
}
