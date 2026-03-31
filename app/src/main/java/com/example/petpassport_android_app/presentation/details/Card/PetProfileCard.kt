package com.example.petpassport_android_app.presentation.details.Card
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.KeyboardArrowLeft

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine


// Цвета из вашего нового дизайна
private val NewBgColor = Color(0xFFF4F5F9)
private val NewPrimaryDark = Color(0xFF2E1A7A)
private val SecondaryTextColor = Color(0xFF4A378B).copy(alpha = 0.5f)

@Composable
fun PetProfileCard(
    pet: Pet,
    events: List<PetEvent>,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenEvents: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F5F9))
            .statusBarsPadding() // Отступ сверху для челки
            .navigationBarsPadding() // ДОБАВИТЬ: Отступ снизу для навигационной полоски
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        // --- ВЕРХНЯЯ НАВИГАЦИЯ ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onBack) {
                    Icon(Icons.Default.KeyboardArrowLeft, null, tint = Color(0xFF2E1A7A))
                    Text("Назад", color = Color(0xFF2E1A7A), fontWeight = FontWeight.Bold)
                }
                Text(pet.name, color = Color.Gray)
            }
        }

        // --- ФОТО ---
        AsyncImage(
            model = if (pet.photoUrl.isNullOrBlank()) R.drawable.no_photo_pet else pet.photoUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // --- ИНФОРМАЦИОННАЯ КАРТОЧКА ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(pet.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1A7A))
                Divider(Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

                InfoRow("Порода", pet.breed)
                Spacer(Modifier.height(16.dp))
                InfoRow("Дата рождения", pet.birthDate)
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) { InfoRow("Пол", "Женский") }
                    Column(Modifier.weight(1f)) { InfoRow("Вес", "${pet.weight} кг") }
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onOpenEvents,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Добавить процедуру") }

                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onEditProfile,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFD1D1D1))
                ) { Text("Изменить данные", color = Color(0xFF2E1A7A)) }
            }
        }

        Spacer(Modifier.height(32.dp))

        // --- БЛОК ПРЕДСТОЯЩИЕ ПРОЦЕДУРЫ ---
        Text("Предстоящие процедуры", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1A7A))
        Spacer(Modifier.height(16.dp))

        if (events.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет предстоящих процедур", color = Color.Gray)
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                events.forEach { event ->
                    com.example.petpassport_android_app.presentation.components.EventCard(event = event)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- НИЖНЯЯ КНОПКА (Исправленная) ---
        Button(
            onClick = onOpenEvents,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Убрали лишний padding отсюда
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Добавить процедуру", fontWeight = FontWeight.Bold)
        }

        // ДОБАВИТЬ ЭТО: пустой отступ в самом конце списка для красоты скролла
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = Color(0xFF2E1A7A), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}


@Preview(
    showBackground = true,
    heightDp = 1200
)
@Preview(showBackground = true)
@Composable
fun PetProfileCardPreview() {
    // Создаем тестовый список процедур
    val testEvents = listOf(
        Vaccine(
            id = 1,
            title = "Вакцинация от бешенства",
            date = "21 сентября 2025  |  13:00",
            petId = 1,
            medicine = "Nobivac"
        ),
        Treatment(
            id = 2,
            title = "Обработка от клещей",
            date = "15 октября 2025  |  10:00",
            petId = 1,
            remedy = "Bravecto",
            parasite = "Клещи",
            nextTreatmentDate = "15.01.2026"
        ),
        DoctorVisit(
            id = 3,
            title = "Плановый осмотр",
            date = "05 ноября 2025  |  16:30",
            petId = 1,
            clinic = "ВетКлиник",
            doctor = "Иванов И.И.",
            diagnosis = "Здоров"
        )
    )

    // Оборачиваем в тему, чтобы правильно отображались Card и Button
    MaterialTheme {
        PetProfileCard(
            pet = Pet(
                id = 1,
                name = "Соня",
                breed = "Бернский зенненхунд",
                weight = 4.0,
                birthDate = "05 ноября 2014 (11 лет)",
                photoUrl = "" // Пустая строка для отображения дефолтной картинки
            ),
            events = testEvents, // Передаем наши тестовые данные
            onBack = {},
            onEditProfile = {},
            onOpenEvents = {}
        )
    }
}

