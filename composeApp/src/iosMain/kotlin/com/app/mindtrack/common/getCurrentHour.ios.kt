package com.app.mindtrack.common

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSDate

actual fun getCurrentHour(): Int {
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(NSCalendarUnitHour, fromDate = NSDate())
    return components.hour.toInt()
}