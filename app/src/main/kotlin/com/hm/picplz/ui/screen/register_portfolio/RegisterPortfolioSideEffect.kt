package com.hm.picplz.ui.screen.register_portfolio

sealed class RegisterPortfolioSideEffect {
    data object NavigateToPrev : RegisterPortfolioSideEffect()
}