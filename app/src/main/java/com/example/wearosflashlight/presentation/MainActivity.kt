//https://github.com/android/wear-os-samples/tree/main/ComposeStarter
package com.example.wearosflashlight.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.material.SwipeToDismissBox
//import androidx.wear.compose.material.Icon

import kotlin.random.Random

/**
 * Simple "Hello, World" app meant as a starting point for a new project using Compose for Wear OS.
 *
 * Displays a centered [Text] composable and a list built with [Horologist]
 * (https://github.com/google/horologist).
 *
 * Use the Wear version of Compose Navigation. You can carry
 * over your knowledge from mobile and it supports the swipe-to-dismiss gesture (Wear OS's
 * back action). For more information, go here:
 * https://developer.android.com/reference/kotlin/androidx/wear/compose/navigation/package-summary
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()

//    WearAppTheme {
//        AppScaffold {
    SwipeDismissableNavHost(navController = navController, startDestination = "home") {

        composable("infinitely_repeating") {
            InfinitelyRepeatable(
                onTapAction = {
                    navController.navigate("home")
                }
            )
        }
        // Home Screen
        composable("home") {
            HomeScreen(
                onTapAction = {
                    navController.navigate("white")
                }
            )
        }
        composable("white") {
            UpdateBrightness()
            WhiteScreen(
                onTapAction = {
                    navController.navigate("infinitely_repeating")
                }
            )
        }
    }
}
//    }
//}


@Composable
fun HomeScreen(onTapAction: () -> Unit) {
    val swipeToDismissState = rememberSwipeToDismissBoxState()
    val context = LocalContext.current  // Get the current activity context

    SwipeToDismissBox(
        state = swipeToDismissState,
        onDismissed = {
            // Finish the activity and return to the watch face
            (context as? Activity)?.finish()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onTapAction()
                    })
                },
            contentAlignment = Alignment.Center
        ) {
//        Icon(
//            imageVector = androidx.compose.material.icons.Icons.Default.WbSunny,
//            contentDescription = null,
//            tint = Color.White)
//    }

            Text(
                text = "Tap",
                color = Color.White
            )
        }
    }
}


@Composable
fun WhiteScreen(onTapAction: () -> Unit) {
    val swipeToDismissState = rememberSwipeToDismissBoxState()
    val context = LocalContext.current  // Get the current activity context

    SwipeToDismissBox(
        state = swipeToDismissState,
        onDismissed = {
            // Finish the activity and return to the watch face
            (context as? Activity)?.finish()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onTapAction() // Call the passed function on tap
                    })
                },
            contentAlignment = Alignment.Center
        ) {
        }
    }
}


@Composable
fun Screen(greetingName: String) {

        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center
        ) {
        }
}

@Composable
fun UpdateBrightness() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        setBrightness(context, isFull = true)
        onDispose {
            setBrightness(context, isFull = false)
        }
    }
}

fun setBrightness(context: Context, isFull: Boolean) {
    val activity = context as? Activity ?: return
    val layoutParams: WindowManager.LayoutParams = activity.window.attributes
    layoutParams.screenBrightness =
        if (isFull) 1f else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    activity.window.attributes = layoutParams
}

// https://developer.android.com/develop/ui/compose/animation/quick-guide
@Composable
fun InfinitelyRepeatable(onTapAction: () -> Unit) {
    // [START android_compose_animation_infinitely_repeating]
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val color by infiniteTransition.animateColor(
        initialValue = Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        ),
        targetValue = Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        ),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )
    val swipeToDismissState = rememberSwipeToDismissBoxState()
    val context = LocalContext.current  // Get the current activity context

    SwipeToDismissBox(
        state = swipeToDismissState,
        onDismissed = {
            // Finish the activity and return to the watch face
            (context as? Activity)?.finish()
        }
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    drawRect(color)
                }
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onTapAction() // Call the passed function on tap
                    })
                },
        ) {
            // your composable here
            UpdateBrightness()
            Screen("Android")
        }
        // [END android_compose_animation_infinitely_repeating]
    }
}

