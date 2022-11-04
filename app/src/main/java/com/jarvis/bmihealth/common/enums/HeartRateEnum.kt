@file:Suppress("unused")

package com.jarvis.bmihealth.common.enums

enum class HeartRateEnum(val activity: Int) {
    AFTER(1),
    BEFORE(2),
    EXERCISING(3),
    GENERAL(4),
    RELAX(5),
    TIRED(6),
    WAKE_UP(7),
    WORKING(8),
}
