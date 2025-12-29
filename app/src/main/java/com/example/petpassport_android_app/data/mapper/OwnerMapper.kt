package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.OwnerDto
import com.example.petpassport_android_app.domain.model.Owner

fun OwnerDto.toDomain(): Owner {
    val count = this.pets?.size ?: 0
    return Owner(
        id = this.ownerId,
        telegramNick = this.telegramNick,
        petCount = count,
        summary = "Владелец с $count питомцами"
    )
}
fun Owner.toDto(): OwnerDto {
    return OwnerDto(
        ownerId = this.id,
        telegramId = 0L,
        telegramNick = this.telegramNick,
        pets = null
    )
}