package com.app.mindtrack.common

import java.util.Calendar

actual fun getCurrentHour(): Int {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
}