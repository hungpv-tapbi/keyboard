/*
 * Copyright (C) 2013 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package com.example.samplekeyboard.features.core.latin.makedict;

import com.example.samplekeyboard.features.core.latin.makedict.FusionDictionary;
import com.example.samplekeyboard.features.core.latin.makedict.FusionDictionary.PtNode;
import com.example.samplekeyboard.features.core.latin.makedict.UnsupportedFormatException;

import java.io.IOException;
import java.util.HashMap;

/**
 * An interface of binary dictionary encoder.
 */
public interface DictEncoder {
    void writeDictionary(FusionDictionary dict, FormatSpec.FormatOptions formatOptions)
        throws IOException, UnsupportedFormatException;
    void setPosition(int position);
    int getPosition();
    void writePtNodeCount(int ptNodeCount);
    void writePtNode(PtNode ptNode, FusionDictionary dict, HashMap<Integer, Integer> codePointToOneByteCodeMap);
}
