package com.tt.focuslayer.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.tt.focuslayer.R
import com.tt.focuslayer.util.dp2px

/**
 * @Author tt
 * @Date 2020/3/16-17:02
 */
class FocusConstraintLayout
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr),
                                                   View.OnFocusChangeListener {
    /**
     * 获取焦点后是否放大
     */
    private var isScale: Boolean = false
    /**
     * 获取焦点后需放大的倍数
     */
    private var scaleSize: Float = 1.1f
    /**
     * 圆角
     */
    private var cornerRadius: Float = 0f
    /**
     * 内容背景色
     */
    private var contentBgColor: Int = 0
    /**
     * 获取焦点时内容背景色
     */
    private var focusContentBgColor: Int = 0
    /**
     * 获取焦点时的边框颜色
     */
    private var focusStrokeColor: Int = 0
    /**
     * 获取焦点时的边框的宽度
     */
    private var focusStrokeWidth: Int = 0

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.FocusLayer)
            isScale = attributes.getBoolean(R.styleable.FocusLayer_isScale, true)
            scaleSize = attributes.getFloat(R.styleable.FocusLayer_scaleSize, 1.1f)
            cornerRadius =
                    attributes.getFloat(R.styleable.FocusLayer_layerRadius, 3f)
            contentBgColor =
                    attributes.getColor(R.styleable.FocusLayer_contentBgColor, Color.parseColor("#4F6D9A"))
            focusContentBgColor =
                    attributes.getColor(R.styleable.FocusLayer_focusContentBgColor, 0)
            focusStrokeColor =
                    attributes.getColor(R.styleable.FocusLayer_focusStrokeColor, Color.parseColor("#00BC71"))
            focusStrokeWidth =
                    attributes.getInt(R.styleable.FocusLayer_focusStrokeWidth, 1)
            attributes.recycle()
        }

        val stateListDrawable = StateListDrawable()
        val normalDrawable = GradientDrawable()
        normalDrawable.cornerRadius = dp2px(cornerRadius).toFloat()
        normalDrawable.setColor(contentBgColor)

        val focusDrawable = GradientDrawable()
        focusDrawable.cornerRadius = dp2px(cornerRadius).toFloat()
        focusDrawable.setColor(if (focusContentBgColor != 0) focusContentBgColor else contentBgColor)
        focusDrawable.setStroke(dp2px(focusStrokeWidth), focusStrokeColor)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusDrawable)
        stateListDrawable.addState(intArrayOf(), normalDrawable)

        this.onFocusChangeListener = this
        this.background = stateListDrawable
        this.isFocusable = true
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v == null) {
            return
        }

        if (hasFocus && isScale) {
            ViewCompat.animate(v).scaleX(scaleSize).scaleY(scaleSize)
                .translationZ(0f).start()
        } else {
            ViewCompat.animate(v).scaleX(1f).scaleY(1f)
                .translationZ(0f).start()
        }
    }
}