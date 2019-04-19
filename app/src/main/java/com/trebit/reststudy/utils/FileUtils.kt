package com.trebit.reststudy.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.trebit.reststudy.TEMP_FORDER_PATH
import com.trebit.reststudy.data.model.GalleryItems
import java.io.File
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
            if (!storageDir.exists())
                storageDir.mkdirs()

            return File.createTempFile(imgFileName, ".jpg", storageDir)
        }

        // Device Local Image 가져오기.
        fun getLocalImagesPath(ctx: Context): List<GalleryItems> {
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection= arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            val cursor = ctx.contentResolver.query(uri, projection, null, null, null)
            val columnIdxData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            var absolutePathOfImg = ""
            val localImgLists = mutableListOf<GalleryItems>()

            while (cursor.moveToNext()) {
                absolutePathOfImg = cursor.getString(columnIdxData)
                localImgLists.add(GalleryItems(absolutePathOfImg))
            }

            return localImgLists
        }

    }
}