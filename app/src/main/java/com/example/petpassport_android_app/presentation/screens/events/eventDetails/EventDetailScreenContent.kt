package com.example.petpassport_android_app.presentation.screens.eventDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.presentation.details.Card.EditEventBottomSheet
import com.example.petpassport_android_app.presentation.screens.home.NewPrimaryDark
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale

private fun formatEventDate(isoDate: String): String {
    return try {
        val parsed = ZonedDateTime.parse(isoDate)
        val dateFormatter = ofPattern("d MMMM yyyy", Locale("ru"))
        val timeFormatter = ofPattern("HH:mm")
        "${parsed.format(dateFormatter)}  |  ${parsed.format(timeFormatter)}"
    } catch (e: Exception) {
        isoDate
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreenContent(
    event: PetEvent,
    petName: String = "",
    onBack: () -> Unit,
    onSave: (PetEvent) -> Unit = {},
    onDelete: () -> Unit = {},
    isLoading: Boolean = false
) {
    val scrollState = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }

    // --- ШТОРКА РЕДАКТИРОВАНИЯ ---
    if (showEditSheet) {
        EditEventBottomSheet(
            event = event,
            onDismiss = { showEditSheet = false },
            onSave = { updatedEvent ->
                showEditSheet = false
                onSave(updatedEvent)
            },
            onDelete = {
                showEditSheet = false
                showDeleteDialog = true
            }
        )
    }

    //ДИАЛОГ ПОДТВЕРЖДЕНИЯ УДАЛЕНИЯ
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    "Удалить процедуру?",
                    fontWeight = FontWeight.Bold,
                    color = NewPrimaryDark
                )
            },
            text = {
                Text("Это действие нельзя отменить", color = Color.Gray)
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) {
                    Text("Удалить", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена", color = NewPrimaryDark)
                }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF3F3F8),
        topBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onBack) {
                        Icon(Icons.Default.KeyboardArrowLeft, null, tint = NewPrimaryDark)
                        Text("Назад", color = NewPrimaryDark, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cat),
                            contentDescription = null,
                            tint = NewPrimaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = petName, color = Color.Gray, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //ЗАГОЛОВОК + КНОПКИ
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = event.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = NewPrimaryDark
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { showEditSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NewPrimaryDark),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        Text("Редактировать", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = NewPrimaryDark,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Удалить", color = Color.Gray)
                        }
                    }
                }
            }

            //ДАТА И ВРЕМЯ
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Дата и время", color = Color.Gray, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = formatEventDate(event.date),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NewPrimaryDark
                    )
                }
            }

            when (event) {
                is DoctorVisit -> {
                    DetailCard(label = "Клиника", value = event.clinic)
                    DetailCard(label = "Врач", value = event.doctor)
                    if (event.diagnosis.isNotBlank()) {
                        SectionCard(title = "Диагноз", content = event.diagnosis)
                    }
                }
                is Vaccine -> {
                    DetailCard(label = "Препарат", value = event.medicine)
                }
                is Treatment -> {
                    DetailCard(label = "Препарат", value = event.remedy)
                    DetailCard(label = "Паразит", value = event.parasite)
                    if (!event.nextTreatmentDate.isNullOrBlank()) {
                        DetailCard(label = "Следующая обработка", value = event.nextTreatmentDate)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(label, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NewPrimaryDark
            )
        }
    }
}

@Composable
private fun SectionCard(title: String, content: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = NewPrimaryDark,
        modifier = Modifier.padding(vertical = 4.dp)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = content,
            fontSize = 15.sp,
            color = Color(0xFF444444),
            modifier = Modifier.padding(20.dp),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true, name = "DoctorVisit")
@Composable
fun EventDetailDoctorVisitPreview() {
    MaterialTheme {
        EventDetailScreenContent(
            event = DoctorVisit(
                id = 1,
                title = "Приём ветеринара",
                date = "2026-04-05T15:00:00Z",
                petId = 1,
                clinic = "Ветеринарная клиника доктора Глушкова В.Г.",
                doctor = "Иванова Анна Сергеевна",
                diagnosis = "Общее состояние удовлетворительное. Признаки кожной чувствительности, склонность к дерматиту. Предварительный диагноз: лёгкий аллергический дерматит."
            ),
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Vaccine")
@Composable
fun EventDetailVaccinePreview() {
    MaterialTheme {
        EventDetailScreenContent(
            event = Vaccine(
                id = 2,
                title = "Вакцинация от бешенства",
                date = "2026-05-01T13:00:00Z",
                petId = 1,
                medicine = "Nobivac Rabies"
            ),
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Treatment")
@Composable
fun EventDetailTreatmentPreview() {
    MaterialTheme {
        EventDetailScreenContent(
            event = Treatment(
                id = 3,
                title = "Обработка от клещей",
                date = "2026-06-15T10:00:00Z",
                petId = 1,
                remedy = "Bravecto",
                parasite = "Клещи",
                nextTreatmentDate = "2026-09-15"
            ),
            petName = "Соня",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun EventDetailLoadingPreview() {
    MaterialTheme {
        EventDetailScreenContent(
            event = Vaccine(
                id = 2,
                title = "Вакцинация от бешенства",
                date = "2026-05-01T13:00:00Z",
                petId = 1,
                medicine = "Nobivac Rabies"
            ),
            petName = "Соня",
            onBack = {},
            isLoading = true
        )
    }
}