package com.example.metermorphosis

import android.os.Bundle
import android.text.LoginFilter
import android.window.SurfaceSyncGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info // Заглушка для фото
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter


// --- 1. ЦВЕТА ИЗ МАКЕТА ---
val ColorPrimary = Color(0xFF6750A4)
val ColorSecondary = Color(0xFF625B71)
val ColorOutlineVariant = Color(0xFFCAC4D0)
val ColorPrimaryContainer = Color(0xFFEADDFF)
val ColorBackground = Color(0xFFFFFFFF)
val ColorOnSurface = Color(0xFFFFFFFF)
val ColorOnSurfaceVariant = Color(0xFF48454F)
val ColorOnPrimaryContainer = Color(0xFF4F378A)

data class MeterCardModel(
    val id: Int,
    val title: String,
    val description: String,
    val lastUpdate: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Устанавливаем общий фон приложения
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = ColorBackground
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    MeterMorphosisApp()
                }
            }
        }
    }
}

// --- 2. ЛОГИКА ПЕРЕКЛЮЧЕНИЯ ЭКРАНОВ ---
@Composable
fun MeterMorphosisApp() {
    // Простая переменная состояния: true = экран входа, false = экран регистрации
    var currentScreen by remember { mutableStateOf("Login") }

    when (currentScreen) {
        "Login" -> LoginScreen(
            onNavigateToRegister = {currentScreen = "Register"},
            onLoginSuccess = {currentScreen = "Dashboard"}
        )
        "Register" -> RegistrationScreen(
            onNavigateToLogin = {currentScreen = "Login"}
        )
        "Dashboard" -> DashboardScreen(
            onNavigateBack = {currentScreen = "Login"}
        )
    }
}

// ЭКРАН ВХОДА
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Центрируем всё по горизонтали
    ) {
        // Заголовок и подзаголовок (общие для обоих экранов)
        AppHeader()

        Spacer(modifier = Modifier.weight(1f)) // Пружинка, толкающая контент к центру

        // Центральный блок
        Text(
            text = "Вход",
            fontSize = 36.sp,
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Контейнер с полями ввода (Прямоугольник радиус 19, делится на два поля)
        InputContainer(
            loginValue = loginText,
            onLoginChange = {newText -> loginText = newText},
            passwordValue = passwordText,
            onPasswordChange = {newText -> passwordText = newText},
            isRegistration = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Продолжить"
        Button(
            onClick = {
                // идентификация и аутентификация
                onLoginSuccess()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp), // Высота для красоты
            shape = RoundedCornerShape(104.dp), // Радиус 104
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
        ) {
            Text("Продолжить", color = ColorOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Текст внизу "Нет аккаунта? Зарегистрируйтесь"
        BottomNavigationText(
            mainText = "Нет аккаунта? ",
            actionText = "Зарегистрируйтесь",
            onClick = onNavigateToRegister
        )

        Spacer(modifier = Modifier.weight(1.5f)) // Балансировка отступа снизу
    }
}

// ЭКРАН РЕГИСТРАЦИИ
@Composable
fun RegistrationScreen(onNavigateToLogin: () -> Unit) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var repeatPasswordText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Регистрация",
            fontSize = 36.sp,
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Контейнер с 3 полями
        InputContainer(
            loginValue = loginText,
            onLoginChange = {newText -> loginText = newText},
            passwordValue = passwordText,
            onPasswordChange = {newText -> passwordText = newText},
            repeatPasswordValue = repeatPasswordText,
            onRepeatPasswordChange = {newText -> repeatPasswordText = newText},
            isRegistration = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Логика создания */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(104.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
        ) {
            Text("Создать аккаунт", color = ColorOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavigationText(
            mainText = "Есть аккаунт? ",
            actionText = "Войти",
            onClick = onNavigateToLogin
        )

        Spacer(modifier = Modifier.weight(1.5f))
    }
}

// ЭКРАН ДАШБОРДА
@Composable
fun DashboardScreen(onNavigateBack: () -> Unit) {
    // Данные для примера
    val meters = listOf(
        MeterCardModel(1, "СЧЕТЧИК ВОДЫ", "Кухня, холодная", "01.01.2026"),
        MeterCardModel(2, "ЭЛЕКТРОЭНЕРГИЯ", "Основной щиток", "01.01.2026"),
        MeterCardModel(3, "ГАЗ", "Котельная", "01.01.2026"),
        MeterCardModel(4, "СЧЕТЧИК ВОДЫ", "Ванная, горячая", "01.01.2026"),
        MeterCardModel(5, "счетчик 2", "все плохо", "01.01.2026"),
        MeterCardModel(6, "счетчик 1g-fe", "все очень плохо, жрем 20 литров на 100 км", "01.01.2026")
    )

    // ИСПОЛЬЗУЕМ BOX, ЧТОБЫ НАКЛАДЫВАТЬ СЛОИ (МЕНЮ ПОВЕРХ КОНТЕНТА)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground) // Белый фон всего экрана
    ) {
        // --- СЛОЙ 1: ВЕСЬ КОНТЕНТ (ЗАГОЛОВОК + СПИСОК) ---
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top // Прижимаем всё к верху!
        ) {
            // 1. ЗАГОЛОВОК И КНОПКА "+"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp), // Отступ сверху побольше (под статус бар)
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Выравнивание по вертикали по центру строки
            ) {
                // Текст слева
                Column {
                    Text(
                        text = "Дашборд",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                    Text(
                        text = "Ваши активные счетчики",
                        fontSize = 14.sp,
                        color = ColorSecondary
                    )
                }

                // Кнопка справа
                Button(
                    onClick = { /* Логика добавления */ },
                    shape = androidx.compose.foundation.shape.CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = Color.White
                    )
                }
            }

            // 2. КОНТЕЙНЕР С КАРТОЧКАМИ (ВТОРИЧНЫЙ ЦВЕТ)
            // Surface занимает всё оставшееся место (weight 1f)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f), // <-- ВАЖНО: Растягивает контейнер до низа
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), // Закругляем только верхние углы
                color = ColorPrimaryContainer
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 100.dp // <-- ВАЖНО: Большой отступ снизу, чтобы меню не перекрывало последнюю карточку
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(meters) { meter ->
                        MeterCardFun(meter)
                    }
                }
            }
        }
        // --- СЛОЙ 2: ПАРЯЩЕЕ НИЖНЕЕ МЕНЮ (КАСТОМНОЕ) ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(70.dp) // <-- Твоя желаемая высота (можно 65.dp, 60.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(45.dp), spotColor = Color.Black),
            shape = RoundedCornerShape(45.dp), // Сильное скругление
            color = ColorPrimaryContainer, // Цвет фона меню
        ) {
            // Используем Row вместо NavigationBar для полного контроля
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly, // Равномерное распределение
                verticalAlignment = Alignment.CenterVertically   // Идеальное центрирование по вертикали
            ) {
                // Кнопка 1: Дашборд (Активная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Home,
                    label = "Дашборд",
                    isSelected = true,
                    onClick = { /* Навигация */ }
                )

                // Кнопка 2: Настройки (Неактивная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Settings,
                    label = "Настройки",
                    isSelected = false,
                    onClick = { /* Навигация */ }
                )
            }
        }
    }
}

@Composable
fun CustomBottomMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) ColorOnPrimaryContainer else ColorSecondary
    val backgroundColor = if (isSelected) ColorPrimary.copy(alpha = 0.1f) else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp)) // Скругление клика
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp), // Регулируй ширину зоны клика здесь
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Контейнер иконки (можно добавить фон для активной, если нужно)
        Box(
            contentAlignment = Alignment.Center,
//            modifier = Modifier.background(backgroundColor, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 2.dp) // Раскомментируй, если хочешь "капсулу" вокруг иконки
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp) // <-- Размер иконки (сделай 32.dp если хочешь еще больше)
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // Отступ между иконкой и текстом

        Text(
            text = label,
            fontSize = 14.sp,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun MeterCardFun(meter: MeterCardModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPrimary), // Основной цвет карточки
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Верхняя часть: Миниатюра + Текст
            Row(
                modifier = Modifier
                    .padding(top = 11.dp, start = 9.dp, bottom = 0.dp, end = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Заглушка для фото (серый прямоугольник)
                Surface(
                    modifier = Modifier.size(60.dp),
                    color = ColorOutlineVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, // Временная иконка
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = ColorPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Текстовый блок справа от фото
                Column {
                    Text(
                        text = meter.title.uppercase(), // БОЛЬШИМИ БУКВАМИ
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Описание",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = meter.description,
                        fontSize = 12.sp,
                        color = ColorOutlineVariant, // Светлый текст описания
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = meter.lastUpdate,
                        fontSize = 12.sp,
                        color = Color(0xFFE5E5E5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = Color.White, thickness = 1.dp)

            // Нижняя часть: Кнопки с обводкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // Кнопка "Добавить фото"
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp), // Занимает половину ширины
                    shape = androidx.compose.ui.graphics.RectangleShape
                ) {
                    Text("Добавить фото", color = Color.White, fontSize = 16.sp)
                }

                VerticalDivider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxHeight().width(1.dp)
                )

                // Кнопка "Детали"
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp), // Занимает половину ширины
                    shape = androidx.compose.ui.graphics.RectangleShape
                ) {
                    Text("Подробнее", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AppHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
        // Название приложения
        Text(
            text = "MeterMorphosis",
//            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = ColorPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp
        )
        // Подзаголовок
        Text(
            text = "Войдите, чтобы использовать приложение",
            fontSize = 14.sp,
            color = ColorSecondary,
            modifier = Modifier.padding(top = 4.dp),
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun InputContainer(
    loginValue: String,
    onLoginChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    repeatPasswordValue: String = "",
    onRepeatPasswordChange: (String) -> Unit = {},
    isRegistration: Boolean
) {
    // Карточка с закругленными углами 19 и цветом Primary Container
    Card(
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPrimaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Поле Логин
            SimpleTextField(
                value = loginValue,
                onValueChange = onLoginChange,
                placeholder = "Логин",
                isPassword = false
            )

            // Разделитель (тонкая линия)
            HorizontalDivider(color = ColorOutlineVariant)

            // Поле Пароль
            SimpleTextField(
                value = passwordValue,
                onValueChange = onPasswordChange,
                placeholder = "Пароль",
                isPassword = true
            )

            // Третье поле, если это регистрация
            if (isRegistration) {
                HorizontalDivider(color = ColorOutlineVariant)
                SimpleTextField(
                    value = repeatPasswordValue,
                    onValueChange = onRepeatPasswordChange,
                    placeholder = "Повторите пароль",
                    isPassword = true
                )
            }
        }
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    // Прозрачное текстовое поле без стандартной обводки Material Design
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = ColorSecondary
            )
        },
        visualTransformation = if(isPassword) PasswordVisualTransformation()
                               else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent, // Убираем черту снизу
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun BottomNavigationText(mainText: String, actionText: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            fontSize = 16.sp,
            color = ColorOnSurfaceVariant // Или Secondary по желанию
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = actionText,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            color = ColorPrimary, // Цвет ссылки
            modifier = Modifier.clickable { onClick() }
        )
    }
}