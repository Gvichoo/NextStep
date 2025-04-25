package com.tbacademy.nextstep.presentation.screen.main.home.model

data class ReactionOption(
    val type: ReactionTypePresentation,
    val isSelected: Boolean = false,
    val isHovered: Boolean = false
)