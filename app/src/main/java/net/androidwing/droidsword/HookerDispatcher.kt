package net.androidwing.droidsword

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import net.androidwing.droidsword.hooker.ActivityHooker
import net.androidwing.droidsword.hooker.DebugHooker
import net.androidwing.droidsword.hooker.FragmentHooker
import net.androidwing.droidsword.hooker.ViewClickedHooker
import net.androidwing.droidsword.utils.LogUtils
import net.androidwing.droidsword.utils.XSP

/**
 * Created  on 2018/12/11.
 */
class HookerDispatcher {
  fun dispatch(lpparam: XC_LoadPackage.LoadPackageParam?) {


    val targetName = XSP.getString(Config.ENABLE_PACKAGE_NAME,"")

    if(targetName == lpparam?.packageName){
      ViewClickedHooker().hook(lpparam!!)
      ActivityHooker().hook(lpparam)
      FragmentHooker().hook(lpparam)
    }

    DebugHooker().hook(lpparam!!)

    if (lpparam.packageName == BuildConfig.APPLICATION_ID) {
      LogUtils.e("inject isHook")
      XposedHelpers.findAndHookMethod("net.androidwing.droidsword.MainActivity",
          lpparam.classLoader, "isHook",
          object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
              LogUtils.e("in the isHook")

              param?.result = true
            }
          })
    }
  }
}