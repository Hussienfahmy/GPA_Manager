package com.hussienfahmy.semester_subjctets_domain.use_case

data class SubjectUseCases(
    val observeSubjectsWithGrades: ObserveSubjects,
    val clearGrade: ClearGrade,
    val deleteSubject: DeleteSubject,
    val addSubject: AddSubject,
    val updateName: UpdateName,
    val setGrade: SetGrade,
)