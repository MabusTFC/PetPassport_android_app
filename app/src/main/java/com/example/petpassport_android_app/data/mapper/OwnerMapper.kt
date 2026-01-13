package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.User.OwnerDto
import com.example.petpassport_android_app.data.dto.User.OwnerPetDto
import com.example.petpassport_android_app.data.dto.User.PetSummaryDto
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet

fun OwnerDto.toDomain(): Owner {
    return Owner(
        id = ownerId,
        login = login,
        telegramId = telegramId?.toString(),
        telegramNick = telegramNick,
        pets = pets?.map { it.toDomain() } ?: emptyList()
    )
}
fun Owner.toDto(): OwnerDto {
    return OwnerDto(
        ownerId = id,
        login = login,
        telegramId = telegramId,
        telegramNick = telegramNick,
        pets = pets.map { OwnerPetDto(it.id, it.name) }
    )
}

fun OwnerPetDto.toDomain(): Pet {
    return Pet(
        id = id,
        name = name,
        breed = "",
        birthDate = "",
        weight = 0.0,
        photoUrl = ""
    )
}

fun PetSummaryDto.toDomain(): Pet {
    return Pet(
        id = id,
        name = name,
        breed = "",
        birthDate = "",
        weight = 0.0,
        photoUrl = ""
    )
}

