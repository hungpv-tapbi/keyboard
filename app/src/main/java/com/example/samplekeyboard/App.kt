package com.example.samplekeyboard

import android.app.Application
import android.os.Build
import com.example.samplekeyboard.features.core.keyboard.emoji.SupportedEmojis
import com.example.samplekeyboard.features.core.latin.RichInputMethodManager
import com.example.samplekeyboard.features.core.latin.checkVersionUpgrade
import com.example.samplekeyboard.features.core.latin.define.DebugFlags
import com.example.samplekeyboard.features.core.latin.settings.Defaults
import com.example.samplekeyboard.features.core.latin.settings.Settings
import com.example.samplekeyboard.features.core.latin.transferOldPinnedClips
import com.example.samplekeyboard.features.core.latin.utils.FoldableUtils
import com.example.samplekeyboard.features.core.latin.utils.LayoutUtilsCustom
import com.example.samplekeyboard.features.core.latin.utils.Log
import com.example.samplekeyboard.features.core.latin.utils.SubtypeSettings
import com.example.samplekeyboard.features.core.latin.utils.prefs
import com.example.samplekeyboard.features.core.latin.utils.upgradeToolbarPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DebugFlags.init(this)
        FoldableUtils.init(this)
        Settings.init(this)
        SubtypeSettings.init(this)

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch { // do some uncritical work in background for faster startup
            SupportedEmojis.load(this@App)
            LayoutUtilsCustom.removeMissingLayouts(this@App)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            @Suppress("DEPRECATION")
            Log.i(
                "startup", "Starting ${applicationInfo.processName} version ${packageInfo.versionName} (${
                    packageInfo.versionCode
                }) on Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
            )
        }

        RichInputMethodManager.init(this)
        checkVersionUpgrade(this)
        if (BuildConfig.DEBUG) // do this on every debug apk start because we may work on adding a new toolbar key
            upgradeToolbarPrefs(prefs())
        transferOldPinnedClips(this) // todo: remove in a few months, maybe end 2026
        app = this
        Defaults.initDynamicDefaults(this)
    }

    companion object {
        // used so JniUtils can access application once
        private var app: App? = null
        fun getApp(): App? {
            val application = app
            app = null
            return application
        }
    }
}
