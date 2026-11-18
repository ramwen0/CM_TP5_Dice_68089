package com.example.birthdaycardcodelab.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.birthdaycardcodelab.R
import com.example.birthdaycardcodelab.ShakeEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.roundToInt

private const val FLICK_MIN_SPEED = 1200f
private val DiceSize = 96.dp

@Composable
fun MainScreen(
    onGoToResult: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val diceSizePx = with(LocalDensity.current) { DiceSize.toPx() }

    var roll by rememberSaveable { mutableIntStateOf(1) }
    var areaSize by remember { mutableStateOf(IntSize.Zero) }
    var rollButtonSize by remember { mutableStateOf(IntSize.Zero) }
    var diceOffset by remember { mutableStateOf(Offset.Zero) }
    var rollButtonOffset by remember { mutableStateOf(Offset.Zero) }
    var placed by remember { mutableStateOf(false) }
    var throwJob by remember { mutableStateOf<Job?>(null) }

    val diceMaxX = (areaSize.width - diceSizePx).coerceAtLeast(0f)
    val diceMaxY = (areaSize.height - diceSizePx).coerceAtLeast(0f)
    val buttonMaxX = (areaSize.width - rollButtonSize.width).coerceAtLeast(0).toFloat()
    val buttonMaxY = (areaSize.height - rollButtonSize.height).coerceAtLeast(0).toFloat()

    ShakeEffect(onShake = { roll = (1..6).random() })

    LaunchedEffect(areaSize, rollButtonSize) {
        if (!placed && areaSize != IntSize.Zero && rollButtonSize != IntSize.Zero) {
            diceOffset = Offset(diceMaxX / 2, areaSize.height * 0.25f)
            rollButtonOffset = Offset(buttonMaxX / 2, areaSize.height * 0.5f)
            placed = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { areaSize = it }
            .pointerInput(diceMaxX, diceMaxY) {
                val tracker = VelocityTracker()
                detectDragGestures(
                    onDragStart = { tracker.resetTracking() },
                    onDragEnd = {
                        val velocity = tracker.calculateVelocity()
                        if (hypot(velocity.x, velocity.y) > FLICK_MIN_SPEED) {
                            roll = (1..6).random()
                            throwJob?.cancel()
                            throwJob = scope.launch {
                                val animatable = Animatable(diceOffset, Offset.VectorConverter)
                                animatable.updateBounds(Offset.Zero, Offset(diceMaxX, diceMaxY))
                                animatable.animateDecay(
                                    Offset(velocity.x, velocity.y),
                                    exponentialDecay()
                                ) {
                                    diceOffset = value
                                }
                            }
                        }
                    }
                ) { change, _ ->
                    change.consume()
                    tracker.addPosition(change.uptimeMillis, change.position)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.main_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.current_roll, roll),
                style = MaterialTheme.typography.titleMedium
            )
        }

        DiceImage(
            value = roll,
            size = DiceSize,
            modifier = Modifier.offset { IntOffset(diceOffset.x.roundToInt(), diceOffset.y.roundToInt()) }
        )

        Button(
            onClick = { roll = (1..6).random() },
            modifier = Modifier
                .offset { IntOffset(rollButtonOffset.x.roundToInt(), rollButtonOffset.y.roundToInt()) }
                .onSizeChanged { rollButtonSize = it }
                .pointerInput(buttonMaxX, buttonMaxY) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        rollButtonOffset = Offset(
                            (rollButtonOffset.x + dragAmount.x).coerceIn(0f, buttonMaxX),
                            (rollButtonOffset.y + dragAmount.y).coerceIn(0f, buttonMaxY)
                        )
                    }
                }
        ) {
            Text(text = stringResource(R.string.roll))
        }

        Button(
            onClick = { onGoToResult(roll) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text(text = stringResource(R.string.go_to_result))
        }
    }
}
