package com.example.calculator.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calculator.domain.TokensCollection
import com.example.calculator.presentation.events.EventsCalculateDora
import com.example.calculator.presentation.models.TextFieldExpressionState
import com.example.calculator.presentation.models.ViewModelCalculateDora
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay

val baseColor: Color = Color(57, 129, 145)
val textStyle: TextStyle = TextStyle(
    fontSize = 45.sp,
    textAlign = TextAlign.End,
    color = Color.White,
)

val textStyleLabel: TextStyle = TextStyle(
    fontSize = 30.sp,
    textAlign = TextAlign.End,
    color = Color.Gray
)
val textStyleButtonInput: TextStyle =
    TextStyle(
        fontSize = 30.sp,
    )
val colorButtonInputGlobalToken = Color(15, 15, 15)
val colorButtonInputOperatorNum = Color(24, 26, 25)

@Composable
fun TemlateScreen(contentScreen: @Composable BoxScope.() -> Unit) {

    Scaffold(
        topBar = { TopAppBarCalculate(nameScreen = "CALCULATER") },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            contentScreen()
        }
    }
}

@Composable
fun ContentScreenCalculateDora(
    modifier: Modifier,
    viewModel: ViewModelCalculateDora = hiltViewModel()
) {

    val orientation = LocalConfiguration.current.orientation
    val stateExpression = viewModel.stateExpression
    val stateCalculateExpression by viewModel.stateCalculatedExpression

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (orientation == Configuration.ORIENTATION_PORTRAIT) 40.dp else 10.dp)
    ) {
        Column {
            if (stateCalculateExpression.result.isEmpty()) {
                TextFieldExpression(
                    modifier = Modifier.fillMaxWidth(),
                    state = stateExpression,
                    onEvent = viewModel::onEvent
                )
            } else {
                TextExpression(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    expression = stateCalculateExpression.result,
                    textStyle = textStyle
                )
                val returnExpression = remember {
                    { viewModel.onEvent(EventsCalculateDora.ReturnExpression) }
                }
                TextExpression(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .clickable(
                            interactionSource = null,
                            indication = ripple(),
                            onClick = returnExpression
                        ),
                    expression = stateExpression.value.textFieldValue.text,
                    textStyle = textStyleLabel
                )
            }
        }
        GridTokens(modifier = Modifier, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldExpression(
    modifier: Modifier,
    state: State<TextFieldExpressionState>,
    onEvent: (EventsCalculateDora) -> Unit
) {

    val interation = remember {
        MutableInteractionSource()
    }

    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = state.value) {
        focusRequester.requestFocus()
    }
    InterceptPlatformTextInput(interceptor = { _, _ ->
        awaitCancellation()
    }) {
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .then(modifier),
            value = state.value.textFieldValue,
            onValueChange = { onEvent(EventsCalculateDora.OnChangeTextFieldExpression(it)) },
            interactionSource = interation,
            textStyle = textStyle,
            singleLine = true,
            cursorBrush = SolidColor(baseColor),
        )
    }

}


@Composable
fun TextExpression(modifier: Modifier, expression: String, textStyle: TextStyle) {
    Text(
        text = expression,
        modifier = modifier,
        textAlign = TextAlign.End,
        style = textStyle, maxLines = 1
    )
}


@Composable
fun GridTokens(modifier: Modifier, onEvent: (EventsCalculateDora) -> Unit) {
    val orientation = LocalConfiguration.current.orientation
    BoxWithConstraints(
        modifier = Modifier
            .then(modifier)
    ) {
        val boxConstraint = this
        val arrangement = Arrangement.spacedBy(5.dp)
        val withItem = { countItems: Int ->
            (boxConstraint.maxWidth / countItems) - (arrangement.spacing - 1.dp)
        }
        val getHeightOrientationButtonInput = remember {
            { height: Dp ->
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    height
                } else {
                    height - 10.dp
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            verticalArrangement = arrangement,
            horizontalArrangement = arrangement,
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(horizontalArrangement = arrangement, userScrollEnabled = false) {
                    items(TokensCollection.globalTokens) {
                        ButtonInput(
                            modifier = Modifier
                                .width(width = withItem(TokensCollection.globalTokens.size))
                                .height(getHeightOrientationButtonInput(70.dp)),
                            color = colorButtonInputGlobalToken,
                            onClick = { onEvent(EventsCalculateDora.OnChangeTextExpression(it)) }
                        ) {
                            Text(text = it.define, color = it.color, style = textStyleButtonInput)
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(horizontalArrangement = arrangement, userScrollEnabled = false) {
                    items(TokensCollection.arithmetic) {
                        ButtonInput(
                            modifier = Modifier
                                .width(withItem(TokensCollection.arithmetic.size))
                                .height(getHeightOrientationButtonInput(70.dp)),
                            color = colorButtonInputOperatorNum,
                            onClick = { onEvent(EventsCalculateDora.OnChangeTextExpression(it)) }
                        ) {
                            Text(
                                text = it.define,
                                color = it.color,
                                style = textStyleButtonInput.copy(fontSize = 35.sp)
                            )
                        }
                    }
                }
            }
            items(TokensCollection.numbers) {
                ButtonInput(
                    modifier = Modifier
                        .width(withItem(TokensCollection.numbers.size))
                        .height(getHeightOrientationButtonInput(60.dp)),
                    color = colorButtonInputOperatorNum, onClick = {
                        onEvent(EventsCalculateDora.OnChangeTextExpression(it))
                    }
                ) {
                    Text(text = it.define, color = it.color, style = textStyleButtonInput)
                }
            }
        }
    }

}

@Composable
fun ButtonInput(
    modifier: Modifier,
    color: Color,
    onClick: () -> Unit,
    text: @Composable () -> Unit
) {
    val focus = LocalFocusManager.current

    Button(
        onClick = { focus.clearFocus();onClick() },
        shape = RoundedCornerShape(5.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        text()
    }
}

@Composable
fun d() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCalculate(nameScreen: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = nameScreen,
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}

