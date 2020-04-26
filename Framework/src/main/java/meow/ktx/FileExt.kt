/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.ktx

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import meow.controller
import meow.core.ui.FragmentActivityInterface
import java.io.File
import java.io.FileOutputStream

/**
 * Meow File Utils.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-18
 */

object MeowFileUtils {

    fun isExternalStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        val writable: Boolean
        val available: Boolean

        when (state) {
            Environment.MEDIA_MOUNTED -> {
                writable = true
                available = writable
            }
            Environment.MEDIA_MOUNTED_READ_ONLY -> {
                available = true
                writable = false
            }
            else -> {
                writable = false
                available = writable
            }
        }

        return available && writable
    }

    fun deleteAllFiles(path: String): Boolean {
        return avoidException {
            val f = File(path)
            if (f.exists()) {
                val files = f.listFiles() ?: return true
                for (file in files) {
                    if (file.isDirectory) {
                        deleteAllFiles(file.toString())
                    } else {
                        file.delete()
                    }
                }
            }
            f.delete()
        } ?: false
    }

}

fun Context.clearCache() {
    val f = cacheDir
    MeowFileUtils.deleteAllFiles(f.path ?: "")
}

fun File?.safeListFiles() =
    avoidException { if (safeExist()) this?.listFiles() else null } ?: arrayOf()

fun File?.safeDelete() = avoidException { this?.delete() } ?: false
fun File?.safeExist() = avoidException { this?.exists() } ?: false
fun File?.safeLength() = avoidException { this?.length() } ?: 0L

fun FragmentActivityInterface<*>.getAppRootPath(
    folderName: String,
    fileName: String? = null,
    forceInternal: Boolean = false
): String {
    return avoidException {
        val root = if (MeowFileUtils.isExternalStorageAvailable() && !forceInternal) {
            @Suppress("DEPRECATION")
            Environment.getExternalStorageDirectory().toString()
        } else {
            context().filesDir.path
        }

        val rootFolderName = controller.rootFolderName
        val rootFolder = File(root, rootFolderName)
        val folder = File(root, "$rootFolderName/$folderName")

        if (!rootFolder.exists()) {
            rootFolder.mkdirs()
        }
        if (!folder.exists()) {
            folder.mkdirs()
        }

        if (folderName == "")
            "$root/$rootFolderName/$fileName"
        else
            "$root/$rootFolderName/$folderName/$fileName"
    } ?: "sdcard/"
}

fun FragmentActivityInterface<*>.getAppCachePath(
    folderName: String,
    fileName: String? = null,
    forceInternal: Boolean = false
): String {
    return avoidException {
        val cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
            !MeowFileUtils.isExternalStorageAvailable() && !forceInternal
        )
            context().externalCacheDir?.path
        else
            context().cacheDir.path

        val folder = File(cachePath, folderName)

        if (!folder.exists()) {
            folder.mkdirs()
        }

        "$cachePath/$folderName/$fileName"
    } ?: "sdcard/"
}

fun Context?.saveBitmapInFile(
    bitmap: Bitmap,
    folderPath: String,
    name: String? = null
) = avoidException {
    val fileName = name ?: generateUUID() + ".jpeg"
        val path = folderPath + fileName
        val fos = FileOutputStream(path)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        path
    }

fun Context?.loadStringFromAssets(fileName: String): String? {
    if (this == null)
        return null
    return avoidException {
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, charset("UTF-8"))
    }
}