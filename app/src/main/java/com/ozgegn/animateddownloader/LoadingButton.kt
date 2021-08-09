package com.ozgegn.animateddownloader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var buttonColor: Int = 0
    private var textColor: Int = 0
    private var buttonProgressColor: Int = 0
    private var buttonText: String = ""
    private var loadingText : String = ""

    private var text: String = ""

    private var progressIndicator = 0

    private var valueAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val rect = Rect()
    private var progressArc = RectF()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            is ButtonState.Loading -> {
                isEnabled = false
                valueAnimator = ValueAnimator.ofInt(0, 1000).apply {
                    addUpdateListener {
                        progressIndicator = animatedValue as Int
                        invalidate()
                    }
                    duration = 1500
                    start()
                }
            }
            is ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
                isEnabled = false
            }

            is ButtonState.Completed -> {
                progressIndicator = 0
                text = buttonText
                isEnabled = true
            }
        }
        invalidate()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            buttonProgressColor = getColor(R.styleable.LoadingButton_buttonProgressColor, 0)
            buttonText = getString(R.styleable.LoadingButton_buttonText) ?: ""
            loadingText = getString(R.styleable.LoadingButton_buttonLoadingText) ?: ""
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = buttonColor
        //draw the button
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        text = buttonText

        if (buttonState == ButtonState.Loading) {
            paint.color = buttonProgressColor
            val progressRect = progressIndicator / 1000f * widthSize
            canvas?.drawRect(0f, 0f, progressRect, heightSize.toFloat(), paint)

            val sweepAngle = progressIndicator / 1000f * 360f
            paint.color = ContextCompat.getColor(context, R.color.white)
            canvas?.drawArc(progressArc, 0f, sweepAngle, true, paint)
            text = loadingText
        }

        paint.color = textColor
        paint.getTextBounds(text, 0, buttonText.length, rect)
        val centerbutton = measuredHeight.toFloat() / 2 - rect.centerY()
        canvas?.drawText(text, widthSize / 2f, centerbutton, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 1)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        progressArc = drawRecF()
    }

    private fun drawRecF() =
        RectF(
            widthSize - 100f,
            heightSize / 2 - 25f,
            widthSize.toFloat() - 50f,
            heightSize / 2 + 25f
        )

    fun startLoading() {
        buttonState = ButtonState.Loading
    }

    fun completeLoading() {
        buttonState = ButtonState.Completed
    }
}