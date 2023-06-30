package com.hussienfahmy.gpa_system_sittings_domain.use_case

import com.hussienfahmy.gpa_system_sittings_domain.repository.GPASystemRepository

class ObserveGPA(
    private val repository: GPASystemRepository,
) {
    operator fun invoke() = repository.observeGPA()
}