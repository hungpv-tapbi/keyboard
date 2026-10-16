/*
 * Copyright (C) 2012 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package com.example.samplekeyboard.features.core.latin.spellcheck;

import android.service.textservice.SpellCheckerService.Session;

import com.example.samplekeyboard.features.core.latin.spellcheck.AndroidSpellCheckerSession;

public abstract class AndroidSpellCheckerSessionFactory {
    public static Session newInstance(AndroidSpellCheckerService service) {
        return new AndroidSpellCheckerSession(service);
    }
}
