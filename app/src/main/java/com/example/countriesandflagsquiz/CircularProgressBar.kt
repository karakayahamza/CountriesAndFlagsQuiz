package com.example.countriesandflagsquiz

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var progress = 0
    private val maxProgress = 60

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = 8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f - circlePaint.strokeWidth / 2f

        // Çemberi çiz
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // İlerleme çizgisi açısını hesapla
        val sweepAngle = 360f * progress / maxProgress

        // İlerleme çizgisini çiz
        val progressPaint = Paint(circlePaint).apply {
            color = Color.GREEN
        }
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            sweepAngle,
            false,
            progressPaint
        )
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // Yeniden çizim yapılmasını sağlar
    }
}
