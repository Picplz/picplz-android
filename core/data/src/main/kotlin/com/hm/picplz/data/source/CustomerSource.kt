package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.CustomerApi
import com.hm.picplz.data.model.CreateCustomerRequest
import com.hm.picplz.data.util.toHttpAppError
import javax.inject.Inject

interface CustomerSource {
    suspend fun createCustomer(request: CreateCustomerRequest): AppResult<Unit>
}

class CustomerSourceImpl
    @Inject
    constructor(
        private val customerApi: CustomerApi,
    ) : CustomerSource {
        override suspend fun createCustomer(request: CreateCustomerRequest): AppResult<Unit> =
            runCatchingAppError {
                val response = customerApi.createCustomer(request)
                if (response.isSuccessful) {
                    Unit
                } else {
                    throw response.toHttpAppError()
                }
            }
    }
