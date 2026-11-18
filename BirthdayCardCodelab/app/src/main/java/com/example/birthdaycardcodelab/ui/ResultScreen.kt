package com.example.birthdaycardcodelab.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.birthdaycardcodelab.JapanTravelBadgeScreen
import com.example.birthdaycardcodelab.R

@Composable
fun ResultScreen(
    screen: Int,
    value: Int,
    onBack: () -> Unit,
    onNavigate: (screen: Int, value: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (screen) {
        1 -> TravelScreen(onBack, modifier)
        2 -> IncrementScreen(value, onBack, modifier)
        3 -> UpdateScreen(value, onBack, modifier)
        4 -> SelectorScreen(value, onBack, onNavigate, modifier)
        5 -> FinalResultScreen(value, onBack, modifier)
        else -> DiceGameScreen(value, onBack, onNavigate, modifier)
    }
}

@Composable
private fun ResultLayout(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        content()
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(0.6f)) {
            Text(text = stringResource(R.string.back))
        }
    }
}

@Composable
private fun TravelScreen(onBack: () -> Unit, modifier: Modifier) {
    var temperature by rememberSaveable { mutableIntStateOf(18) }

    JapanTravelBadgeScreen(
        temperature = temperature,
        onRefresh = { temperature = (5..35).random() },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun IncrementScreen(value: Int, onBack: () -> Unit, modifier: Modifier) {
    var current by rememberSaveable { mutableIntStateOf(value) }

    ResultLayout(stringResource(R.string.die_result, 2), onBack, modifier) {
        DiceImage(current)
        Text(text = stringResource(R.string.current_value, current))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { current = (current + 1) % 6 + 1 }) {
            Text(text = stringResource(R.string.increment_by_two))
        }
    }
}

@Composable
private fun UpdateScreen(value: Int, onBack: () -> Unit, modifier: Modifier) {
    var current by rememberSaveable { mutableIntStateOf(value) }
    var input by rememberSaveable { mutableStateOf("") }

    ResultLayout(stringResource(R.string.die_result, 3), onBack, modifier) {
        DiceImage(current)
        Text(text = stringResource(R.string.current_value, current))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = input,
            onValueChange = { text ->
                input = text.take(1)
                input.toIntOrNull()?.takeIf { it in 1..6 }?.let { current = it }
            },
            label = { Text(text = stringResource(R.string.update_die)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}

@Composable
private fun SelectorScreen(
    value: Int,
    onBack: () -> Unit,
    onNavigate: (Int, Int) -> Unit,
    modifier: Modifier
) {
    ResultLayout(stringResource(R.string.die_result, 4), onBack, modifier) {
        Text(text = stringResource(R.string.select_screen))
        Spacer(modifier = Modifier.height(8.dp))
        (1..6).forEach { target ->
            Button(
                onClick = { onNavigate(target, value) },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(text = stringResource(R.string.go_to_screen, target))
            }
        }
    }
}

@Composable
private fun FinalResultScreen(value: Int, onBack: () -> Unit, modifier: Modifier) {
    ResultLayout(stringResource(R.string.die_result, 5), onBack, modifier) {
        DiceImage(value)
        Text(text = stringResource(R.string.result_value, value))
    }
}

@Composable
private fun DiceGameScreen(
    value: Int,
    onBack: () -> Unit,
    onNavigate: (Int, Int) -> Unit,
    modifier: Modifier
) {
    var secondDie by rememberSaveable { mutableStateOf<Int?>(null) }

    ResultLayout(stringResource(R.string.die_result_game, 6), onBack, modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.current_die))
                DiceImage(value, size = 72.dp)
            }
            secondDie?.let { second ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.second_die))
                    DiceImage(second, size = 72.dp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { secondDie = (1..6).random() }) {
            Text(text = stringResource(R.string.roll_second_die))
        }

        secondDie?.let { second ->
            val won = second >= value
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (won) {
                    stringResource(R.string.you_won, second, value)
                } else {
                    stringResource(R.string.you_lost, second, value)
                },
                style = MaterialTheme.typography.titleMedium
            )
            if (won) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onNavigate(second, second) }) {
                    Text(text = stringResource(R.string.go_to_result))
                }
            }
        }
    }
}
