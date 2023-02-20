package com.example.statecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.statecodelab.ui.theme.ComposePathwayTheme
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePathwayTheme {
                CalculateTipApp(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun CalculateTipApp(
    modifier: Modifier = Modifier
) {
    var amountInput by remember {
        mutableStateOf("")
    }

    var tipInput by remember {
        mutableStateOf("")
    }

    var isChecked by remember {
        mutableStateOf(false)
    }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount, tipPercent)

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.header),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(12.dp))

        EditTextField(
            amount = amountInput,
            placeholder = R.string.bill_amount,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        ) {
            amountInput = it
        }
        Spacer(modifier = Modifier.height(12.dp))

        EditTextField(
            amount = tipInput,
            placeholder = R.string.how_was_the_service,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        ) {
            tipInput = it
        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundTipRow(isChecked = isChecked, onCheckedChange = {
            isChecked = it
        })

        Spacer(modifier = Modifier.height(32.dp))

        TipTextField(
            tip = if (isChecked) tip.toDouble().roundToInt().toString() else tip,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun EditTextField(
    amount: String,
    modifier: Modifier = Modifier,
    @StringRes placeholder: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = amount,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(placeholder))
        },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun TipTextField(
    tip: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(
            R.string.tip_amount,
            tip
        ),
        modifier = modifier,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun RoundTipRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

fun calculateTip(
    amount: Double?,
    tipPercent: Double = 15.0
): String {
    amount?.let {
        val tip = tipPercent / 100 * amount
        return NumberFormat.getInstance().format(tip)
    }
    return ""
}


