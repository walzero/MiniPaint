package com.walterrezende.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

private const val STROKE_WIDTH = 12f

class MyCanvasView(context: Context) : View(context) {

    private lateinit var virtualCanvas: Canvas
    private lateinit var virtualBitmap: Bitmap

    private val drawColor =
        ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    private var path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private val touchTolerance =
        ViewConfiguration.get(context).scaledTouchSlop

    private val backGroundColor =
        ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    private lateinit var frame: Rect

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::virtualBitmap.isInitialized) virtualBitmap.recycle()

        virtualBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        virtualCanvas = Canvas(virtualBitmap)
        virtualCanvas.drawColor(backGroundColor)

        val inset = 20
        frame = Rect(inset, inset, w - inset, h - inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(virtualBitmap, 0f, 0f, null)
        canvas?.drawRect(frame, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            motionTouchEventX = ev.x
            motionTouchEventY = ev.y

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> touchStart()
                MotionEvent.ACTION_MOVE -> touchMove()
                MotionEvent.ACTION_UP -> touchUp()
            }
        }
        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            virtualCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }
}