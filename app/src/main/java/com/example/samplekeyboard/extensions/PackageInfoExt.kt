/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2024 Fcitx5 for Android Contributors
 */

package com.example.samplekeyboard.extensions

import android.content.pm.PackageInfo
import com.example.samplekeyboard.utils.isAtLeastApi28

val PackageInfo.versionCodeCompat: Long
    get() = if (isAtLeastApi28) {
        longVersionCode
    } else {
        @Suppress("DEPRECATION")
        versionCode.toLong()
    }
