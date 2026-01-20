package com.hm.picplz.ui.screen.sign_up.sign_up_client

sealed interface SignUpClientSideEffect {
    data object NavigateToPrev : SignUpClientSideEffect
}
