package com.hm.picplz.data.service

import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserService
    @Inject
    constructor() {
        suspend fun getUser(): User {
            delay(3000)
            return emptyUserData
        }
    }
