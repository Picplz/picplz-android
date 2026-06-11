package com.hm.picplz.data.service

import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.provider.TokenManager.AuthRole
import javax.inject.Inject

class UserService
    @Inject
    constructor(
        private val tokenManager: TokenManager,
        private val memberService: MemberService,
    ) {
        suspend fun getUser(): User {
            if (tokenManager.getAuthRole() != AuthRole.USER) return emptyUserData

            val memberId = tokenManager.getMemberId() ?: return emptyUserData

            return memberService.getMemberInfo(memberId)
                .fold(
                    onSuccess = { memberProfile ->
                        User(
                            id = memberProfile.id.toString(),
                            nickname = memberProfile.nickname,
                            userType = memberProfile.userType,
                            profileImageUri = memberProfile.profileImage,
                        )
                    },
                    onFailure = { emptyUserData },
                )
        }
    }
