package com.esightcorp.mobile.app.ui.components.eshare

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import java.io.InputStream

class AutoFitTextureView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): TextureView(context!!, attrs, defStyle) {

    private var mRatioWidth = 0
    private var mRatioHeight = 0


    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    private fun setAspectRatio(width: Int, height: Int) {
        require(!(width < 0 || height < 0)) { "Size cannot be negative." }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
            }
        }
    }

    fun setImageStream(inputStream: InputStream) {
        Log.i(TAG, "setImageStream: ")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val canvas = lockCanvas()
        if(canvas != null){
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            unlockCanvasAndPost(canvas)
        }

    }

    init {
        setAspectRatio(16, 9)
    }

    companion object{
        private const val TAG = "AutoFitTextureView"
    }
}