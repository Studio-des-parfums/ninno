package com.ninno.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ninno.app.ui.LocalPartnerLogoUrl
import com.ninno.app.ui.theme.NinnoTheme

private object Routes {
    const val HOME = "home"
    const val SECONDARY = "secondary"
    const val THIRD = "third"
    const val FOURTH = "fourth"
    const val SORTING_TOP = "sorting_top"
    const val FIFTH = "fifth"
    const val SORTING_HEART = "sorting_heart"
    const val SEVENTH = "seventh"
    const val SORTING_BASE = "sorting_base"
    const val FINAL = "final"
}

@Composable
fun App(versionName: String) {
    CoilSetup.ensureInitialized()
    NinnoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val formulaViewModel: PerfumeFormulaViewModel = viewModel { PerfumeFormulaViewModel() }
            val appContentViewModel: AppContentViewModel = viewModel { AppContentViewModel() }

            val content by appContentViewModel.content.collectAsState()
            val isPreloading by appContentViewModel.isPreloading.collectAsState()
            val backgroundUrl = content?.backgroundUrl
            val backgroundOtherUrl = content?.backgroundOtherUrl
            val logoUrl = content?.logoUrl
            val displayTopNotes = content?.topNotes ?: topNotes.toDisplayNotes()
            val displayHeartNotes = content?.heartNotes ?: heartNotes.toDisplayNotes()
            val displayBaseNotes = content?.baseNotes ?: baseNotes.toDisplayNotes()

            val navController = rememberNavController()
            // Logo partenaire (en haut à droite), lu par ScreenBackground sur tous les écrans
            CompositionLocalProvider(LocalPartnerLogoUrl provides content?.partnerLogoUrl) {
            NavHost(navController = navController, startDestination = Routes.HOME) {
                composable(Routes.HOME) {
                    HomeScreen(
                        onNextClick = { navController.navigate(Routes.SECONDARY) },
                        backgroundUrl = backgroundUrl,
                        isLoading = isPreloading,
                        versionName = versionName
                    )
                }
                composable(Routes.SECONDARY) {
                    SecondaryScreen(
                        onNextClick = { navController.navigate(Routes.THIRD) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.THIRD) {
                    ThirdScreen(
                        onNextClick = { navController.navigate(Routes.FOURTH) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.FOURTH) {
                    FourthScreen(
                        onNextClick = { navController.navigate(Routes.SORTING_TOP) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.SORTING_TOP) {
                    SortingScreen(
                        title = "Note de tête",
                        notes = displayTopNotes,
                        onSelectionChanged = { liked, disliked ->
                            formulaViewModel.recordChoice(NoteCategory.TOP, liked, disliked)
                        },
                        onNextClick = { navController.navigate(Routes.FIFTH) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.FIFTH) {
                    FifthScreen(
                        onNextClick = { navController.navigate(Routes.SORTING_HEART) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.SORTING_HEART) {
                    SortingScreen(
                        title = "Note de cœur",
                        notes = displayHeartNotes,
                        onSelectionChanged = { liked, disliked ->
                            formulaViewModel.recordChoice(NoteCategory.HEART, liked, disliked)
                        },
                        onNextClick = { navController.navigate(Routes.SEVENTH) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.SEVENTH) {
                    SeventhScreen(
                        onNextClick = { navController.navigate(Routes.SORTING_BASE) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.SORTING_BASE) {
                    SortingScreen(
                        title = "Note de fond",
                        notes = displayBaseNotes,
                        onSelectionChanged = { liked, disliked ->
                            formulaViewModel.recordChoice(NoteCategory.BASE, liked, disliked)
                        },
                        onNextClick = { navController.navigate(Routes.FINAL) },
                        onBackClick = { navController.popBackStack() },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
                composable(Routes.FINAL) {
                    FinalScreen(
                        formulaViewModel = formulaViewModel,
                        onHomeClick = {
                            formulaViewModel.reset()
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        },
                        backgroundUrl = backgroundOtherUrl,
                        logoUrl = logoUrl
                    )
                }
            }
            }
        }
    }
}
