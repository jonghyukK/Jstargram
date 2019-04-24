package com.yalantis.ucrop.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.IntRange
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

import com.yalantis.ucrop.callback.BitmapLoadCallback
import com.yalantis.ucrop.model.ExifInfo
import com.yalantis.ucrop.util.BitmapLoadUtils
import com.yalantis.ucrop.util.FastBitmapDrawable
import com.yalantis.ucrop.util.RectUtils

/**
 * Interface for rotation and scale change notifying.
 */
interface TransformImageListener {

    /**
     * Setter for [.mMaxBitmapSize] value.
     * Be sure to call it before [.setImageURI] or other image setters.
     *
     * @param maxBitmapSize - max size for both width and height of bitmap that will be used in the view.
     */
    var maxBitmapSize: Int
        get() {
            if (mMaxBitmapSize <= 0) {
                mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(getContext())
            }
            return mMaxBitmapSize
        }
        set(maxBitmapSize) {
            mMaxBitmapSize = maxBitmapSize
        }

    val imageInputPath: String
        get() = mImageInputPath

    val imageOutputPath: String
        get() = mImageOutputPath

    val exifInfo: ExifInfo
        get() = mExifInfo

    /**
     * @return - current image scale value.
     * [1.0f - for original image, 2.0f - for 200% scaled image, etc.]
     */
    val currentScale: Float
        get() = getMatrixScale(mCurrentImageMatrix)

    /**
     * @return - current image rotation angle.
     */
    val currentAngle: Float
        get() = getMatrixAngle(mCurrentImageMatrix)

    val viewBitmap: Bitmap?
        @Nullable
        get() = if (getDrawable() == null || getDrawable() !is FastBitmapDrawable) {
            null
        } else {
            (getDrawable() as FastBitmapDrawable).getBitmap()
        }

    fun onLoadComplete()

    fun onLoadFailure(@NonNull e: Exception)

    fun onRotate(currentAngle: Float)

    fun onScale(currentScale: Float)

    @JvmOverloads
    abstract constructor(context: Context, attrs: AttributeSet = null, defStyle: Int = 0) : super(context, attrs, defStyle) {
        init()
    }

    fun setTransformImageListener(transformImageListener: TransformImageListener) {
        mTransformImageListener = transformImageListener
    }

    @Override
    fun setScaleType(scaleType: ScaleType) {
        if (scaleType === ScaleType.MATRIX) {
            super.setScaleType(scaleType)
        } else {
            Log.w(TAG, "Invalid ScaleType. Only ScaleType.MATRIX can be used")
        }
    }

    @Override
    fun setImageBitmap(bitmap: Bitmap) {
        setImageDrawable(FastBitmapDrawable(bitmap))
    }

    /**
     * This method takes an Uri as a parameter, then calls method to decode it into Bitmap with specified size.
     *
     * @param imageUri - image Uri
     * @throws Exception - can throw exception if having problems with decoding Uri or OOM.
     */
    @Throws(Exception::class)
    fun setImageUri(@NonNull imageUri: Uri, @Nullable outputUri: Uri) {
        val maxBitmapSize = maxBitmapSize

        BitmapLoadUtils.decodeBitmapInBackground(getContext(), imageUri, outputUri, maxBitmapSize, maxBitmapSize,
                object : BitmapLoadCallback() {

                    @Override
                    fun onBitmapLoaded(@NonNull bitmap: Bitmap, @NonNull exifInfo: ExifInfo, @NonNull imageInputPath: String, @Nullable imageOutputPath: String) {
                        mImageInputPath = imageInputPath
                        mImageOutputPath = imageOutputPath
                        mExifInfo = exifInfo

                        mBitmapDecoded = true
                        setImageBitmap(bitmap)
                    }

                    @Override
                    fun onFailure(@NonNull bitmapWorkerException: Exception) {
                        Log.e(TAG, "onFailure: setImageUri", bitmapWorkerException)
                        if (mTransformImageListener != null) {
                            mTransformImageListener.onLoadFailure(bitmapWorkerException)
                        }
                    }
                })
    }

    /**
     * This method calculates scale value for given Matrix object.
     */
    fun getMatrixScale(@NonNull matrix: Matrix): Float {
        return Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2) + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y), 2))
    }

    /**
     * This method calculates rotation angle for given Matrix object.
     */
    fun getMatrixAngle(@NonNull matrix: Matrix): Float {
        return -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI))
    }

    @Override
    fun setImageMatrix(matrix: Matrix) {
        super.setImageMatrix(matrix)
        mCurrentImageMatrix.set(matrix)
        updateCurrentImagePoints()
    }

    /**
     * This method translates current image.
     *
     * @param deltaX - horizontal shift
     * @param deltaY - vertical shift
     */
    fun postTranslate(deltaX: Float, deltaY: Float) {
        if (deltaX != 0f || deltaY != 0f) {
            mCurrentImageMatrix.postTranslate(deltaX, deltaY)
            setImageMatrix(mCurrentImageMatrix)
        }
    }

    /**
     * This method scales current image.
     *
     * @param deltaScale - scale value
     * @param px         - scale center X
     * @param py         - scale center Y
     */
    fun postScale(deltaScale: Float, px: Float, py: Float) {
        if (deltaScale != 0f) {
            mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py)
            setImageMatrix(mCurrentImageMatrix)
            if (mTransformImageListener != null) {
                mTransformImageListener.onScale(getMatrixScale(mCurrentImageMatrix))
            }
        }
    }

    /**
     * This method rotates current image.
     *
     * @param deltaAngle - rotation angle
     * @param px         - rotation center X
     * @param py         - rotation center Y
     */
    fun postRotate(deltaAngle: Float, px: Float, py: Float) {
        if (deltaAngle != 0f) {
            mCurrentImageMatrix.postRotate(deltaAngle, px, py)
            setImageMatrix(mCurrentImageMatrix)
            if (mTransformImageListener != null) {
                mTransformImageListener.onRotate(getMatrixAngle(mCurrentImageMatrix))
            }
        }
    }

    protected fun init() {
        setScaleType(ScaleType.MATRIX)
    }

    @Override
    protected fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        super.onLayout(changed, left, top, right, bottom)
        if (changed || mBitmapDecoded && !mBitmapLaidOut) {

            left = getPaddingLeft()
            top = getPaddingTop()
            right = getWidth() - getPaddingRight()
            bottom = getHeight() - getPaddingBottom()
            mThisWidth = right - left
            mThisHeight = bottom - top

            onImageLaidOut()
        }
    }

    /**
     * When image is laid out [.mInitialImageCenter] and [.mInitialImageCenter]
     * must be set.
     */
    protected fun onImageLaidOut() {
        val drawable = getDrawable() ?: return

        val w = drawable!!.getIntrinsicWidth()
        val h = drawable!!.getIntrinsicHeight()

        Log.d(TAG, String.format("Image size: [%d:%d]", w.toInt(), h.toInt()))

        val initialImageRect = RectF(0, 0, w, h)
        mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect)
        mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect)

        mBitmapLaidOut = true

        if (mTransformImageListener != null) {
            mTransformImageListener.onLoadComplete()
        }
    }

    /**
     * This method returns Matrix value for given index.
     *
     * @param matrix     - valid Matrix object
     * @param valueIndex - index of needed value. See [Matrix.MSCALE_X] and others.
     * @return - matrix value for index
     */
    protected fun getMatrixValue(@NonNull matrix: Matrix, @IntRange(from = 0, to = MATRIX_VALUES_COUNT) valueIndex: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[valueIndex]
    }

    /**
     * This method logs given matrix X, Y, scale, and angle values.
     * Can be used for debug.
     */
    @SuppressWarnings("unused")
    protected fun printMatrix(@NonNull logPrefix: String, @NonNull matrix: Matrix) {
        val x = getMatrixValue(matrix, Matrix.MTRANS_X)
        val y = getMatrixValue(matrix, Matrix.MTRANS_Y)
        val rScale = getMatrixScale(matrix)
        val rAngle = getMatrixAngle(matrix)
        Log.d(TAG, "$logPrefix: matrix: { x: $x, y: $y, scale: $rScale, angle: $rAngle }")
    }

    /**
     * This method updates current image corners and center points that are stored in
     * [.mCurrentImageCorners] and [.mCurrentImageCenter] arrays.
     * Those are used for several calculations.
     */
    private fun updateCurrentImagePoints() {
        mCurrentImageMatrix.mapPoints(mCurrentImageCorners, mInitialImageCorners)
        mCurrentImageMatrix.mapPoints(mCurrentImageCenter, mInitialImageCenter)
    }

}
/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 *
 *
 * This class provides base logic to setup the image, transform it with matrix (move, scale, rotate),
 * and methods to get current matrix state.
 */
