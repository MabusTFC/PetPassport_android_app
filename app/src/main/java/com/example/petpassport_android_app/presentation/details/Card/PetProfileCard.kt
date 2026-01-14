
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetProfileCard(
    pet: Pet,
    onBack: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Кнопка назад
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
        }

        Spacer(Modifier.height(20.dp))

        // Фото питомца
        AsyncImage(
            model = if (pet.photoUrl.isNullOrEmpty()) R.drawable.avatar_pet_defualt else pet.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // Имя и данные питомца
        Text(
            pet.name,
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.Primary
        )
        Spacer(Modifier.height(8.dp))
        Text("Порода: ${pet.breed}", style = MaterialTheme.typography.headlineSmall)
        Text("Вес: ${pet.weight} кг", style = MaterialTheme.typography.headlineSmall)
        Text("Дата рождения: ${pet.birthDate}", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // Кнопка редактирования
        FloatingActionButton(
            onClick = onEditClick,
            containerColor = AppColors.Primary,
            contentColor = AppColors.White,
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetProfileCardPreview() {
    PetProfileCard(
        pet =  Pet(
            id = 1,
            name = "Бастер",
            breed = "Лабрадор",
            weight = 25.0,
            birthDate = "12.03.2020",
            photoUrl = ""
        ),
        onBack = {},
        onEditClick = {}
    )
}

