package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.NavigationRoute
import com.hm.picplz.navigation.model.SignUpAddDevice
import com.hm.picplz.navigation.model.SignUpCareerPeriod
import com.hm.picplz.navigation.model.SignUpDetailExperience
import com.hm.picplz.navigation.model.SignUpDevice
import com.hm.picplz.navigation.model.SignUpExperience
import com.hm.picplz.navigation.model.SignUpMainLocation
import com.hm.picplz.navigation.model.SignUpPhotographyVibe
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpAddDeviceScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpCareerPeriodScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpDetailExpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpDeviceScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpExperienceScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpMainLocationScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpPhotographyVibeScreen

@Composable
fun SignUpPhotographerNavHost(
    mainNavController: NavHostController,
    signUpPhotographerNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    startAt: String? = null,
) {
    val startDestination: NavigationRoute =
        when (startAt) {
            "device" -> SignUpDevice
            "vibe" -> SignUpPhotographyVibe
            "career" -> SignUpCareerPeriod
            "experience" -> SignUpExperience
            "detail-experience" -> SignUpDetailExperience
            else -> SignUpMainLocation
        }

    NavHost(
        navController = signUpPhotographerNavController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<SignUpExperience> {
            SignUpExperienceScreen(
                mainNavController = mainNavController,
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpDetailExperience> {
            SignUpDetailExpScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpPhotographyVibe> {
            SignUpPhotographyVibeScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpCareerPeriod> {
            SignUpCareerPeriodScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpMainLocation> {
            SignUpMainLocationScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpDevice> {
            SignUpDeviceScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
            )
        }
        composable<SignUpAddDevice> { backStackEntry ->
            val route = backStackEntry.toRoute<SignUpAddDevice>()
            val category =
                when (route.category.lowercase()) {
                    "camera" -> DeviceCategory.CAMERA
                    "phone" -> DeviceCategory.PHONE
                    else -> DeviceCategory.PHONE
                }
            SignUpAddDeviceScreen(
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
                category = category,
            )
        }
    }
}
