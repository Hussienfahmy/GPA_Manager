package com.hussienfahmy.grades_setting_presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hussienfahmy.core_ui.domain.use_cases.FilterToDigitsOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTextDialogViewModel @Inject constructor(
    private val filterToDigitsOnly: FilterToDigitsOnly,
) : ViewModel() {

    var value by mutableStateOf("")
        private set

    fun onValueChanged(newValue: String) {
        value = filterToDigitsOnly(newValue)
    }
}