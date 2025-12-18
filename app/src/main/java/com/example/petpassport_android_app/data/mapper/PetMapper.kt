package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.PetDto
import com.example.petpassport_android_app.domain.model.Pet

fun PetDto.toPet(): Pet{
    return Pet(
        id = id,
        name = name,
        breed = breed ?: "",
        weight = weight,
        birthDate = birthDate,
        photoUrl = this.photo?.firstOrNull()?.url ?: ""
    )
}