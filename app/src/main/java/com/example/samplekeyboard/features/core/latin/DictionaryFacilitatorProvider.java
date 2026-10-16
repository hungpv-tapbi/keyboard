/*
 * Copyright (C) 2013 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package com.example.samplekeyboard.features.core.latin;

import com.example.samplekeyboard.features.core.latin.DictionaryFacilitatorImpl;

/**
 * Factory for instantiating DictionaryFacilitator objects.
 */
public class DictionaryFacilitatorProvider {
    public static DictionaryFacilitator getDictionaryFacilitator(boolean isNeededForSpellChecking) {
        return new DictionaryFacilitatorImpl();
    }
}
