package meow.util

import android.graphics.Typeface

object FontHelper {

    var map: HashMap<String?, Typeface>? = null
}

//fun Context?.getFont(name: String?): Typeface? {
//    if (this == null)
//        return null
//
//    var fontName = name
//
//    if (FontHelper.map == null)
//        FontHelper.map = HashMap()
//
//    if (fontName.isEmptyTrimAllSpaces()) {
//        fontName = controller.defaultTypefaceResId
//    }
//
//    if (FontHelper.map!![fontName] == null) {
//        Log.d("FontHelper","init font : $fontName")
//        FontHelper.map!![fontName] = Typeface.createFromAsset(assets, fontName)
//    }
//
//    return FontHelper.map!![fontName]
//}

//fun Context?.getFont(res: Int): Typeface? {
//    if (this == null)
//        return null
//    return getFont(getString(res))
//}