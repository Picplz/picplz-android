package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.AreaData
import com.hm.picplz.domain.model.Area

fun AreaData.toDomain(): Area {
    return Area(
        id = id,
        name = name,
        dong = dong,
        ri = ri
    )
}
