package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CreateCustomerRequest
import com.hm.picplz.data.source.CustomerSource
import com.hm.picplz.domain.model.CustomerSignup
import javax.inject.Inject

interface CustomerService {
    suspend fun createCustomer(signup: CustomerSignup): AppResult<Unit>
}

class CustomerServiceImpl
    @Inject
    constructor(
        private val customerSource: CustomerSource,
    ) : CustomerService {
        override suspend fun createCustomer(signup: CustomerSignup): AppResult<Unit> =
            customerSource.createCustomer(signup.toRequest())
    }

private fun CustomerSignup.toRequest(): CreateCustomerRequest =
    CreateCustomerRequest(
        nickname = nickname,
        socialEmail = socialEmail,
        socialProvider = socialProvider,
        socialCode = socialCode,
        profileImage = profileImage,
    )
