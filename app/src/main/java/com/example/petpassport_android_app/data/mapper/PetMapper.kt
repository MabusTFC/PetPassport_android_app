package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.BuildConfig
import com.example.petpassport_android_app.data.dto.User.PetDto
import com.example.petpassport_android_app.domain.model.Pet

import com.example.petpassport_android_app.BuildConfig.BASE_URL

fun PetDto.toDomain(): Pet{
    val rawUrl = this.photo?.firstOrNull()?.url
    val cleanUrl = if (rawUrl?.startsWith("/") == true) rawUrl.substring(1) else rawUrl
    return Pet(
        id = id,
        name = name,
        breed = breed ?: "",
        weight = weight,
        birthDate = birthDate,
        photoUrl = if (cleanUrl.isNullOrBlank()) "" else "$BASE_URL/$cleanUrl"
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