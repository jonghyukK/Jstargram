package com.trebit.reststudy.utils

import android.os.Environment
import com.trebit.reststudy.TEMP_FORDER_PATH
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
            if ( !storageDir.exists())
                storageDir.mkdirs()

            return File.createTempFile(imgFileName, ".jpg", storageDir)
        }


        fun getBytes(iss: InputStream): ByteArray{
            val byteBuff = ByteArrayOutputStream()

            val buffSize = 1024
            val buff = ByteArray(buffSize)

            var len = 0
            while (iss.read(buff) != -1) {
                len = iss.read(buff)
                byteBuff.write(buff, 0, len)
            }

            return byteBuff.toByteArray()
        }
    }
}