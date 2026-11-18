package com.example.birthdaycardcodelab.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.birthdaycardcodelab.R

@DrawableRes
fun diceDrawable(value: Int): Int = when (value) {
    1 -> R.drawable.dice_1
    2 -> R.drawable.dice_2
    3 -> R.drawable.dice_3
    4 -> R.drawable.dice_4
    5 -> R.drawable.dice_5
    else -> R.drawable.dice_6
}

@Composable
fun DiceImage(value: Int, modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Image(
        painter = painterResource(diceDrawable(value)),
        contentDescription = value.toString(),
        modifier = modifier.size(size)
    )
}
