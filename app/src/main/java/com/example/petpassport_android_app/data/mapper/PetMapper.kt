package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.PetDto
import com.example.petpassport_android_app.domain.model.Pet

fun PetDto.toDomain(): Pet{
    return Pet(
        id = id,
        name = name,
        breed = breed ?: "",
        weight = weight,
        birthDate = birthDate,
        photoUrl = this.photo?.firstOrNull()?.url ?: ""
    )
}
fun Pet.toDto(ownerId: Int): PetDto {
    return PetDto(
        id = this.id,
        name = this.name,
        breed = this.breed,
        weight = this.weight,
        birthDate = this.birthDate,
        ownerId = ownerId,
        photo = null
    )
}