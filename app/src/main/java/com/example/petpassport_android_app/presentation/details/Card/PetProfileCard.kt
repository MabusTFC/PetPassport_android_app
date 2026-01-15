
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.example.petpassport_android_app.presentation.details.button.OutlineButton
import com.example.petpassport_android_app.presentation.details.button.PrimaryButton
import com.example.petpassport_android_app.presentation.theme.AppColors

@Composable
fun PetProfileCard(
    pet: Pet,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenEvents: () -> Unit,
    //onAddEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Назад
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .width(150.dp)
                .height(40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Назад",
                modifier = Modifier.fillMaxSize()
            )
        }


        Spacer(Modifier.height(20.dp))

        // Фото
        AsyncImage(
            model = if (pet.photoUrl.isNullOrEmpty())
                R.drawable.avatar_pet_defualt
            else pet.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // Имя
        Text(
            pet.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(28.dp))

        Text("Порода: ${pet.breed}", style = MaterialTheme.typography.bodyLarge)
        Text("Вес: ${pet.weight} кг", style = MaterialTheme.typography.bodyLarge)
        Text("Дата рождения: ${pet.birthDate}", style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(35.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlineButton(
                text = "Изменить",
                onClick = onEditProfile,
            )

            Spacer(Modifier.height(15.dp))

            PrimaryButton(
                text = "Процедуры",
                onClick = onOpenEvents,
            )

            Spacer(Modifier.height(10.dp))

            //OutlineButton(
            //    text = "+ Процедура",
            //    onClick = onAddEvent,
            //)

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
        onEditProfile = {},
        onOpenEvents = {},
        //onAddEvent = {}
    )
}

