package com.hussienfahmy.myGpaManager.data.user_data.mapper

import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData

internal fun FirebaseUserData.toDomain(): UserData = UserData(
    id = id,
    name = name,
    photoUrl = photoUrl,
    email = email,
    academicInfo = UserData.AcademicInfo(
        university = academicInfo.university,
        faculty = academicInfo.faculty,
        department = academicInfo.department,
        level = academicInfo.level,
        semester = when (academicInfo.semester) {
            FirebaseUserData.AcademicInfo.Semester.First -> UserData.AcademicInfo.Semester.First
            FirebaseUserData.AcademicInfo.Semester.Second -> UserData.AcademicInfo.Semester.Second
        }
    ),
    academicProgress = UserData.AcademicProgress(
        cumulativeGPA = academicProgress.cumulativeGPA,
        creditHours = academicProgress.creditHours
    )
)