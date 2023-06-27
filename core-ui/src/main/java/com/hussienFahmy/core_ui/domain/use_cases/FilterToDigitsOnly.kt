package com.hussienFahmy.core_ui.domain.use_cases

class FilterToDigitsOnly {
    operator fun invoke(input: String): String {
        return input.filter{ it.isDigit() || it == '.' }
    }
}