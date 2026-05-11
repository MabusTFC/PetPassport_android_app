package com.example.petpassport_android_app.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petpassport_android_app.presentation.details.Card.ErrorCard
import com.example.petpassport_android_app.presentation.details.Card.TextFieldCard
import com.example.petpassport_android_app.presentation.details.Card.TopBarCard
import com.example.petpassport_android_app.R
import java.io.IOException

@Composable
fun RegisterEntryContent(
    state: LoginScreenModel.State,
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    var isPrivacyAccepted by remember { mutableStateOf(false) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    val privacyPolicyText = remember(context) {
        try {
            context.assets.open(PRIVACY_POLICY_ASSET).bufferedReader().use { it.readText() }
        } catch (_: IOException) {
            "Файл политики обработки персональных данных не найден."
        }
    }

    if (showPrivacyPolicy) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicy = false },
            title = { Text("Персональные данные") },
            text = {
                PrivacyPolicyContent(
                    markdown = privacyPolicyText,
                    modifier = Modifier
                        .height(360.dp)
                        .verticalScroll(rememberScrollState()),
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicy = false }) {
                    Text("Закрыть")
                }
            },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NewBgColor)
            .statusBarsPadding()
    ) {
        // Верхний логотип (фон)
        Image(
            painter = painterResource(id = R.drawable.top_bar_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя "пружина", чтобы опустить заголовок
            Spacer(modifier = Modifier.weight(0.3f))

            // Заголовок экрана
            Text(
                text = "Регистрация",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = NewPrimaryDark
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Белая карточка с формой
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp), // Внутренний отступ карточки
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldCard(value = login, onValueChange = onLoginChange, text = "Логин")
                    TextFieldCard(value = password, onValueChange = onPasswordChange, text = "Пароль")

                    Spacer(modifier = Modifier.height(8.dp))

                    PrivacyConsentCheckbox(
                        checked = isPrivacyAccepted,
                        onCheckedChange = { isPrivacyAccepted = it },
                        onPolicyClick = { showPrivacyPolicy = true },
                    )

                    if (state is LoginScreenModel.State.Error) {
                        ErrorCard(message = state.message)
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                        shape = RoundedCornerShape(12.dp),
                        enabled = state !is LoginScreenModel.State.Loading && isPrivacyAccepted
                    ) {
                        if (state is LoginScreenModel.State.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Создать аккаунт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            // Нижняя "пружина"
            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}

@Composable
private fun PrivacyPolicyContent(
    markdown: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        markdown
            .lines()
            .map { it.trim() }
            .filterNot { it.isBlank() || it == "---" || it.matches(Regex("""\|[-\s|]+\|""")) }
            .forEach { line ->
                when {
                    line.startsWith("### ") -> Text(
                        text = line.removePrefix("### ").cleanMarkdownInline(),
                        color = NewPrimaryDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    line.startsWith("## ") -> Text(
                        text = line.removePrefix("## ").cleanMarkdownInline(),
                        color = NewPrimaryDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    line.startsWith("# ") -> Text(
                        text = line.removePrefix("# ").cleanMarkdownInline(),
                        color = NewPrimaryDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    line.startsWith("- ") -> EmailAwarePolicyText(
                        text = "• ${line.removePrefix("- ").cleanNonBoldMarkdownInline()}",
                    )
                    line.startsWith("|") && line.endsWith("|") -> {
                        val cells = line
                            .trim('|')
                            .split("|")
                            .map { it.trim().cleanNonBoldMarkdownInline() }
                        if (cells.size >= 2) {
                            EmailAwarePolicyText(
                                text = "${cells[0]}: ${cells.drop(1).joinToString(" ")}",
                            )
                        }
                    }
                    else -> EmailAwarePolicyText(
                        text = line.cleanNonBoldMarkdownInline(),
                    )
                }
            }
    }
}

@Composable
private fun EmailAwarePolicyText(text: String) {
    val uriHandler = LocalUriHandler.current
    val emailRegex = remember { Regex("""[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}""") }
    val annotatedText = buildAnnotatedString {
        var index = 0
        var isBold = false
        while (index < text.length) {
            val markerIndex = text.indexOf("**", startIndex = index)
            if (markerIndex == -1) {
                appendPolicySegment(text.substring(index), isBold, emailRegex)
                index = text.length
            } else {
                appendPolicySegment(text.substring(index, markerIndex), isBold, emailRegex)
                isBold = !isBold
                index = markerIndex + 2
            }
        }
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(
            color = Color(0xFF2B2B2B),
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        onClick = { offset ->
            annotatedText
                .getStringAnnotations(EMAIL_TAG, offset, offset)
                .firstOrNull()
                ?.let { annotation ->
                    try {
                        uriHandler.openUri(annotation.item)
                    } catch (_: Exception) {
                    }
                }
        },
    )
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendPolicySegment(
    segment: String,
    isBold: Boolean,
    emailRegex: Regex,
) {
    var cursor = 0
    emailRegex.findAll(segment).forEach { match ->
        appendStyledPolicyText(segment.substring(cursor, match.range.first), isBold)
        val email = match.value
        pushStringAnnotation(tag = EMAIL_TAG, annotation = "mailto:$email")
        withStyle(
            SpanStyle(
                color = NewPrimaryDark,
                fontWeight = FontWeight.Bold,
            ),
        ) {
            append(email)
        }
        pop()
        cursor = match.range.last + 1
    }
    appendStyledPolicyText(segment.substring(cursor), isBold)
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendStyledPolicyText(
    text: String,
    isBold: Boolean,
) {
    if (text.isEmpty()) return
    if (isBold) {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text)
        }
    } else {
        append(text)
    }
}

private fun String.cleanMarkdownInline(): String =
    replace("**", "")
        .replace("__", "")
        .replace("`", "")
        .replace(Regex("""\[(.+?)]\(.+?\)"""), "$1")
        .trim()

private fun String.cleanNonBoldMarkdownInline(): String =
    replace("__", "")
        .replace("`", "")
        .replace(Regex("""\[(.+?)]\(.+?\)"""), "$1")
        .trim()

@Composable
private fun PrivacyConsentCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onPolicyClick: () -> Unit,
) {
    val annotatedText = buildAnnotatedString {
        append("Я соглашаюсь с обработкой ")
        pushStringAnnotation(tag = PRIVACY_POLICY_TAG, annotation = PRIVACY_POLICY_TAG)
        withStyle(
            SpanStyle(
                color = NewPrimaryDark,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("персональных данных")
        }
        pop()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        ClickableText(
            text = annotatedText,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = Color(0xFF4A378B),
                fontSize = 14.sp,
                lineHeight = 18.sp,
            ),
            onClick = { offset ->
                annotatedText
                    .getStringAnnotations(PRIVACY_POLICY_TAG, offset, offset)
                    .firstOrNull()
                    ?.let { onPolicyClick() }
            },
        )
    }
}

private const val PRIVACY_POLICY_ASSET = "privacy-policy.md"
private const val PRIVACY_POLICY_TAG = "privacy_policy"
private const val EMAIL_TAG = "email"

@Preview(showBackground = true)
@Composable
fun RegisterEntryPreview() {
    RegisterEntryContent(
        state = LoginScreenModel.State.Idle,
        login = "",
        onLoginChange = {},
        password = "",
        onPasswordChange = {},
        onBack = {},
        onSubmit = {}
    )
}
