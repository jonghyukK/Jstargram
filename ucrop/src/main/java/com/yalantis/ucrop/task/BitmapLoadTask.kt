package com.yalantis.ucrop.task

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.ParcelFileDescriptor
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log

import com.yalantis.ucrop.callback.BitmapLoadCallback
import com.yalantis.ucrop.model.ExifInfo
import com.yalantis.ucrop.util.BitmapLoadUtils
import com.yalantis.ucrop.util.FileUtils

import java.io.File
import java.io.FileDescriptor
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.BufferedSource
import okio.Okio
import okio.Sink

/**
 * Creates and returns a Bitmap for a given Uri(String url).
 * inSampleSize is calculated based on requiredWidth property. However can be adjusted if OOM occurs.
 * If any EXIF config is found - bitmap is transformed properly.
 */
class BitmapLoadTask(@param:NonNull private val mContext: Context,
                     @param:NonNull private var mInputUri: Uri?, @param:Nullable private val mOutputUri: Uri?,
                     private val mRequiredWidth: Int, private val mRequiredHeight: Int,
                     private val mBitmapLoadCallback: BitmapLoadCallback) : AsyncTask<Void, Void, BitmapLoadTask.BitmapWorkerResult>() {

    private val filePath: String?
        get() = if (ContextCompat.checkSelfPermission(mContext, permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
            FileUtils.getPath(mContext, mInputUri)
        } else {
            null
        }

    class BitmapWorkerResult {

        internal var mBitmapResult: Bitmap
        internal var mExifInfo: ExifInfo
        internal var mBitmapWorkerException: Exception? = null

        constructor(@NonNull bitmapResult: Bitmap, @NonNull exifInfo: ExifInfo) {
            mBitmapResult = bitmapResult
            mExifInfo = exifInfo
        }

        constructor(@NonNull bitmapWorkerException: Exception) {
            mBitmapWorkerException = bitmapWorkerException
        }

    }

    @Override
    @NonNull
    protected fun doInBackground(vararg params: Void): BitmapWorkerResult {
        if (mInputUri == null) {
            return BitmapWorkerResult(NullPointerException("Input Uri cannot be null"))
        }

        try {
            processInputUri()
        } catch (e: NullPointerException) {
            return BitmapWorkerResult(e)
        } catch (e: IOException) {
            return BitmapWorkerResult(e)
        }

        val parcelFileDescriptor: ParcelFileDescriptor?
        try {
            parcelFileDescriptor = mContext.getContentResolver().openFileDescriptor(mInputUri, "r")
        } catch (e: FileNotFoundException) {
            return BitmapWorkerResult(e)
        }

        val fileDescriptor: FileDescriptor
        if (parcelFileDescriptor != null) {
            fileDescriptor = parcelFileDescriptor!!.getFileDescriptor()
        } else {
            return BitmapWorkerResult(NullPointerException("ParcelFileDescriptor was null for given Uri: [$mInputUri]"))
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        if (options.outWidth === -1 || options.outHeight === -1) {
            return BitmapWorkerResult(IllegalArgumentException("Bounds for bitmap could not be retrieved from the Uri: [$mInputUri]"))
        }

        options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, mRequiredWidth, mRequiredHeight)
        options.inJustDecodeBounds = false

        var decodeSampledBitmap: Bitmap? = null

        var decodeAttemptSuccess = false
        while (!decodeAttemptSuccess) {
            try {
                decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
                decodeAttemptSuccess = true
            } catch (error: OutOfMemoryError) {
                Log.e(TAG, "doInBackground: BitmapFactory.decodeFileDescriptor: ", error)
                options.inSampleSize *= 2
            }

        }

        if (decodeSampledBitmap == null) {
            return BitmapWorkerResult(IllegalArgumentException("Bitmap could not be decoded from the Uri: [$mInputUri]"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BitmapLoadUtils.close(parcelFileDescriptor)
        }

        val exifOrientation = BitmapLoadUtils.getExifOrientation(mContext, mInputUri)
        val exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation)
        val exifTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation)

        val exifInfo = ExifInfo(exifOrientation, exifDegrees, exifTranslation)

        val matrix = Matrix()
        if (exifDegrees != 0) {
            matrix.preRotate(exifDegrees)
        }
        if (exifTranslation != 1) {
            matrix.postScale(exifTranslation, 1)
        }
        return if (!matrix.isIdentity()) {
            BitmapWorkerResult(BitmapLoadUtils.transformBitmap(decodeSampledBitmap, matrix), exifInfo)
        } else BitmapWorkerResult(decodeSampledBitmap, exifInfo)

    }

    @Throws(NullPointerException::class, IOException::class)
    private fun processInputUri() {
        val inputUriScheme = mInputUri!!.getScheme()
        Log.d(TAG, "Uri scheme: $inputUriScheme")
        if ("http".equals(inputUriScheme) || "https".equals(inputUriScheme)) {
            try {
                downloadFile(mInputUri, mOutputUri)
            } catch (e: NullPointerException) {
                Log.e(TAG, "Downloading failed", e)
                throw e
            } catch (e: IOException) {
                Log.e(TAG, "Downloading failed", e)
                throw e
            }

        } else if ("content".equals(inputUriScheme)) {
            val path = filePath
            if (!TextUtils.isEmpty(path) && File(path).exists()) {
                mInputUri = Uri.fromFile(File(path))
            } else {
                try {
                    copyFile(mInputUri, mOutputUri)
                } catch (e: NullPointerException) {
                    Log.e(TAG, "Copying failed", e)
                    throw e
                } catch (e: IOException) {
                    Log.e(TAG, "Copying failed", e)
                    throw e
                }

            }
        } else if (!"file".equals(inputUriScheme)) {
            Log.e(TAG, "Invalid Uri scheme $inputUriScheme")
            throw IllegalArgumentException("Invalid Uri scheme$inputUriScheme")
        }
    }

    @Throws(NullPointerException::class, IOException::class)
    private fun copyFile(@NonNull inputUri: Uri, @Nullable outputUri: Uri?) {
        Log.d(TAG, "copyFile")

        if (outputUri == null) {
            throw NullPointerException("Output Uri is null - cannot copy image")
        }

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = mContext.getContentResolver().openInputStream(inputUri)
            outputStream = FileOutputStream(File(outputUri!!.getPath()))
            if (inputStream == null) {
                throw NullPointerException("InputStream for given input Uri is null")
            }

            val buffer = ByteArray(1024)
            var length: Int
            while ((length = inputStream!!.read(buffer)) > 0) {
                outputStream!!.write(buffer, 0, length)
            }
        } finally {
            BitmapLoadUtils.close(outputStream)
            BitmapLoadUtils.close(inputStream)

            // swap uris, because input image was copied to the output destination
            // (cropped image will override it later)
            mInputUri = mOutputUri
        }
    }

    @Throws(NullPointerException::class, IOException::class)
    private fun downloadFile(@NonNull inputUri: Uri, @Nullable outputUri: Uri?) {
        Log.d(TAG, "downloadFile")

        if (outputUri == null) {
            throw NullPointerException("Output Uri is null - cannot download image")
        }

        val client = OkHttpClient()

        var source: BufferedSource? = null
        var sink: Sink? = null
        var response: Response? = null
        try {
            val request = Request.Builder()
                    .url(inputUri.toString())
                    .build()
            response = client.newCall(request).execute()
            source = response!!.body().source()

            val outputStream = mContext.getContentResolver().openOutputStream(outputUri)
            if (outputStream != null) {
                sink = Okio.sink(outputStream)
                source!!.readAll(sink)
            } else {
                throw NullPointerException("OutputStream for given output Uri is null")
            }
        } finally {
            BitmapLoadUtils.close(source)
            BitmapLoadUtils.close(sink)
            if (response != null) {
                BitmapLoadUtils.close(response!!.body())
            }
            client.dispatcher().cancelAll()

            // swap uris, because input image was downloaded to the output destination
            // (cropped image will override it later)
            mInputUri = mOutputUri
        }
    }

    @Override
    protected fun onPostExecute(@NonNull result: BitmapWorkerResult) {
        if (result.mBitmapWorkerException == null) {
            mBitmapLoadCallback.onBitmapLoaded(result.mBitmapResult, result.mExifInfo, mInputUri!!.getPath(), if (mOutputUri == null) null else mOutputUri!!.getPath())
        } else {
            mBitmapLoadCallback.onFailure(result.mBitmapWorkerException)
        }
    }

    companion object {

        private val TAG = "BitmapWorkerTask"
    }

}
