package com.example.samplekeyboard.services

import android.view.View
import androidx.lifecycle.LifecycleOwner

class NeonImeService : LifecycleInputMethod(), LifecycleOwner {
    override fun onCreateInputView(): View? {
        return super.onCreateInputView()
    }
}