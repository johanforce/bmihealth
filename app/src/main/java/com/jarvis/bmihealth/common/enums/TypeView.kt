package com.jarvis.bmihealth.common.enums

//@IntDef(TypeView.height, TypeView.weight, TypeView.strideLength)
//@Retention(RetentionPolicy.SOURCE)
//annotation class TypeView {
//    companion object {
//        var height = 0
//        var weight = 1
//        var strideLength = 2
//    }
//}

enum class TypeView(val index: Int) {
    HEIGHT(0),
    WEIGHT(1),
    STRIDE_LENGTH(2)
}

