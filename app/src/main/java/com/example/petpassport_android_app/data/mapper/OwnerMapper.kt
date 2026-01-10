package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.OwnerDto
import com.example.petpassport_android_app.data.dto.OwnerPetDto
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet

fun OwnerDto.toDomain(): Owner {
    return Owner(
        id = this.ownerId,
        telegramNick = this.telegramNick,
        telegramId = this.telegramId,
        pets = pets?.map { it.toDomain() } ?: emptyList()
    )
}
fun Owner.toDto(): OwnerDto {
    return OwnerDto(
        ownerId = this.id,
        telegramId = this.telegramId,
        telegramNick = this.telegramNick,
        pets = pets.map { OwnerPetDto(id = it.id, name = it.name) }
    )
}

fun OwnerPetDto.toDomain(): Pet {
    return Pet(
        id = this.id,
        name = this.name,
        breed = "Не указана",
        birthDate = "Не указана",
        weight = 0.0,
        photoUrl = ""
    )

}