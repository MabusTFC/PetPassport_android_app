package com.example.petpassport_android_app.presentation.details.Card


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.*

/**
 * Карточка-иконка для типа события
 *
 * @param event - событие PetEvent
 * @param size - размер иконки (width = height)
 */
@Composable
fun EventIconLabelCard(
    event: PetEvent,
    dp: Int = 60,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = eventIcLabelRes(event)),
        contentDescription = null,
        modifier = Modifier
            .width(dp.dp)   // ширина
            .height((dp/2).dp), // высота
        contentScale = ContentScale.Fit
    )
}

@DrawableRes
fun eventIcLabelRes(event: PetEvent): Int = when (event) {
    is Vaccine -> R.drawable.ic_vac_txt
    is Treatment -> R.drawable.ic_tr_txt
    is DoctorVisit -> R.drawable.ic_vis_txt
}