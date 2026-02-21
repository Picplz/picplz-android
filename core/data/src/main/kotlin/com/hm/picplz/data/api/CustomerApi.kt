package com.hm.picplz.data.api

import com.hm.picplz.data.model.CreateCustomerRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerApi {
    @POST("api/v1/customers")
    suspend fun createCustomer(
        @Body request: CreateCustomerRequest,
    ): Response<Unit>
}
