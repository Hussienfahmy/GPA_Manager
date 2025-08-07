package com.hussienFahmy.myGpaManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hussienFahmy.myGpaManager.navigation.AppBottomNav
import com.hussienFahmy.myGpaManager.navigation.AppDestinationsNavHost
import com.hussienFahmy.myGpaManager.ui.theme.GPAManagerTheme
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.utils.startDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel.googleAuthUiClient)

        setContent {
            GPAManagerTheme {
                val localFocusManager = LocalFocusManager.current
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route
                val isSingedIn by viewModel.isSignedIn.collectAsState()

                LaunchedEffect(key1 = isSingedIn) {
                    if (isSingedIn == false) {
                        navController.navigate(NavGraphs.onBoarding.startDestination.route) {
                            popUpTo(NavGraphs.root.startDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                localFocusManager.clearFocus()
                            })
                        },
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    bottomBar = {
                        if (
                            currentDestination !in NavGraphs.onBoarding.destinations.map { it.route }
                        ) {
                            AppBottomNav(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppDestinationsNavHost(
                            navController = navController,
                            snackBarHostState = snackBarHostState,
                            googleAuthUiClient = viewModel.googleAuthUiClient,
                        )
                    }
                }
            }
        }
    }
}