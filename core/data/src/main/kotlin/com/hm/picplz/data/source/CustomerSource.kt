package com.hm.picplz.data.source

import com.hm.picplz.data.api.CustomerApi
import com.hm.picplz.data.model.CreateCustomerRequest
import javax.inject.Inject

interface CustomerSource {
    suspend fun createCustomer(request: CreateCustomerRequest): Result<Unit>
}

class CustomerSourceImpl
    @Inject
    constructor(
        private val customerApi: CustomerApi,
    ) : CustomerSource {
        override suspend fun createCustomer(request: CreateCustomerRequest): Result<Unit> =
            runCatching {
                val response = customerApi.createCustomer(request)
                if (response.isSuccessful) {
                    Unit
                } else {
                    error("Create customer failed: ${response.code()} ${response.errorBody()?.string()}")
                }
            }
    }
