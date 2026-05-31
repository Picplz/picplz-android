package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CreateCustomerRequest
import com.hm.picplz.data.source.CustomerSource
import javax.inject.Inject

interface CustomerService {
    suspend fun createCustomer(request: CreateCustomerRequest): AppResult<Unit>
}

class CustomerServiceImpl
    @Inject
    constructor(
        private val customerSource: CustomerSource,
    ) : CustomerService {
        override suspend fun createCustomer(request: CreateCustomerRequest): AppResult<Unit> =
            customerSource.createCustomer(request)
    }
