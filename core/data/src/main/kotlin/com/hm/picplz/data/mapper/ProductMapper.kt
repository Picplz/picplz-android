package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.CreateProductRequest
import com.hm.picplz.domain.model.CreateProductCommand

fun CreateProductCommand.toRequest() =
    CreateProductRequest(
        photographerId = photographerId,
        name = name,
        description = description,
        shootPrice = shootPrice,
        shootDuration = shootDuration,
        otherDetails = otherDetails,
        productPhotos = productPhotos,
        amount = amount,
        editedYn = editedYn,
        editPrice = editPrice,
    )
