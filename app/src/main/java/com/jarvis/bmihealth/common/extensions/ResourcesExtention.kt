/*
 * Copyright © OMRON HEALTHCARE Co., Ltd. 2020. All rights reserved.
 */

package com.jarvis.bmihealth.common.extensions

import android.content.res.Resources

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
