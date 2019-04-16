package com.trebit.reststudy.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.trebit.reststudy.TEMP_FORDER_PATH
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.content.ContentUris
import android.os.Build
import android.util.Log


/**
 * Jstargram
 * Class: FileUtils
 * Created by kangjonghyuk on 16/04/2019.
 *
 * Description:
 */

class FileUtils {

    companion object {

        fun createImageFile(): File {
            val timeStamp = SimpleDateFormat("HHmmss").format(Date())
            val imgFileName = "Jstagram_${timeStamp}_"
            val storageDir = File(TEMP_FORDER_PATH)
            if (!storageDir.exists())
                storageDir.mkdirs()

            return File.createTempFile(imgFileName, ".jpg", storageDir)
        }
    }
}