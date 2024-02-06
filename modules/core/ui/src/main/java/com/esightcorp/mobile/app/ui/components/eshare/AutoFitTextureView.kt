/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.eshare

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
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
        Log.i(TAG, "onMeasure: w:$width  h:$height")
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.i(TAG, "onMeasure:1 Setting dimensions to w:$width , h:${width * mRatioHeight/mRatioWidth}")
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)
            } else {
                Log.i(TAG, "onMeasure:2 Setting dimensions to w:${height * mRatioWidth / mRatioHeight} , h:${height}")
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
            }
        }
    }

    init {
        setAspectRatio(4, 3)
    }

    companion object{
        private const val TAG = "AutoFitTextureView"
    }
}