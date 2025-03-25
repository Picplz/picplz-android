package com.hm.picplz.data.model

data class KaKaoAddressRequest(
    val x: String,
    val y: String
)

data class KaKaoAddressResponse(
    val meta: Meta,
    val documents: List<Document>
) {
    data class Meta(
        val total_count: Int
    )

    data class Document(
        val address: Address,
        val road_address: RoadAddress?
    )

    data class Address(
        val address_name: String,
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
        val mountain_yn: String,
        val main_address_no: String,
        val sub_address_no: String
    )

    data class RoadAddress(
        val address_name: String,
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
        val road_name: String,
        val underground_yn: String,
        val main_building_no: String,
        val sub_building_no: String,
        val building_name: String,
        val zone_no: String
    )
}

data class KaKaoPlaceRequest(
    val query: String,
)

data class KaKaoPlaceResponse(
    val meta: Meta,
    val documents: List<Place>
) {
    data class Meta(
        val total_count: Int,
        val pageable_count: Int,
        val is_end: Boolean,
        val same_name: RegionInfo
    )

    data class RegionInfo(
        val region: List<String>,
        val keyword: String,
        val selected_region: String,
    )

    data class Place(
        val id: String,
        val place_name: String,
        val category_name: String,
        val category_group_code: String,
        val category_group_name: String,
        val phone: String,
        val address_name: String,
        val road_address_name: String,
        val x: String,
        val y: String,
        val place_url: String,
        val distanc: String
    )
}