package com.ozgegn.animateddownloader

sealed class ButtonState {
    object Clicked: ButtonState()
    object Loading: ButtonState()
    object Completed: ButtonState()
}