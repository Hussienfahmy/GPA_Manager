package com.hussienfahmy.user_data_data.mapper

import com.hussienfahmy.user_data_data.model.UserData
import com.hussienfahmy.core.domain.user_data.model.UserData as UserDataDomain

internal fun UserData.toUserData(): UserDataDomain = UserDataDomain(
    name = name,
    photoUrl = photoUrl,
    email = email,
    academicInfo = UserDataDomain.AcademicInfo(
        university = academicInfo.university,
        faculty = academicInfo.faculty,
        department = academicInfo.department,
        level = academicInfo.level,
        semester = UserDataDomain.AcademicInfo.Semester.valueOf(academicInfo.semester.name),
    ),
    academicProgress = UserDataDomain.AcademicProgress(
        cumulativeGPA = academicProgress.cumulativeGPA,
        creditHours = academicProgress.creditHours,
    ),
    id = id
)