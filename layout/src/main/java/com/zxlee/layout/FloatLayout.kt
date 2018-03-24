package com.zxlee.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 根据按钮多少自动换行的ViewGroup
 */
class FloatLayout : ViewGroup {
    private var maxWidth: Int = 0// 可使用的最大宽度

    private val mRowHeight = ArrayList<Int>() //每一行的最大高度，在measure时存起来，待layout时使用
    private val mRowIndex = ArrayList<Int>() //每行第一个View的下标

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var containerHeight = 0// 容器的高度,也就是本布局的高度。初始化赋值为0.
        var maxHeightInRow = 0 //记录每一行里最高的View的高度
        var maxWidthInRow = 0 //记录最宽的一行的宽度
        var widthRow = 0 //记录每一行目前的宽度

        mRowHeight.clear()
        mRowIndex.clear()
        mRowIndex.add(0)

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            /**
             * measure(int widthMeasureSpec,int
             * heightMeasureSpec)用于设置子组件显示模式.有三个值：<br></br>
             * MeasureSpec.AT_MOST 该组件可以设置自己的大小,但是最大不能超过其父组件的限定<br></br>
             * MeasureSpec.EXACTLY 无论该组件设置大小是多少,都只能按照父组件限制的大小来显示<br></br>
             * MeasureSpec.UNSPECIFIED 该组件不受父组件的限制,可以设置任意大小
             */
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            val viewWitdh = getViewWidth(view)
            if (viewWitdh > maxWidth) {
                throw RuntimeException("子View附加margin比父View还宽，出错啦！")
            }
            val viewHeight = getViewHeight(view)
            if (viewWitdh + widthRow > maxWidth) {
                //超宽了，换行
                mRowHeight.add(maxHeightInRow)
                mRowIndex.add(i)
                containerHeight += maxHeightInRow
                maxWidthInRow = Math.max(widthRow, maxWidthInRow)
                maxHeightInRow = viewHeight
                widthRow = viewWitdh
            } else {
                //不换行，更新累计宽度和最大高度
                maxHeightInRow = Math.max(maxHeightInRow, viewHeight)
                widthRow += viewWitdh
            }
        }
        containerHeight += maxHeightInRow //最后一行的高度要加上
        maxWidthInRow = Math.max(widthRow, maxWidthInRow)
        setMeasuredDimension(if (widthMode == MeasureSpec.AT_MOST) maxWidthInRow else maxWidth, containerHeight)// onMeasure方法的关键代码,该句设置父容器的大小。
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        var top = paddingTop
        var right = 0
        var bottom = 0

        var row = 0 //当前行

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
            if (mRowIndex.contains(i)) {
                //每行第一个view
                left = paddingLeft
                top += if (row > 0) mRowHeight[row - 1] else 0 //顶部坐标= 顶部padding + 加上行高
                ++row
            }
            right = left + marginLayoutParams.leftMargin + view.measuredWidth
            bottom = top + marginLayoutParams.topMargin + view.measuredHeight
            view.layout(left + marginLayoutParams.leftMargin, top + marginLayoutParams.topMargin, right, bottom)

            left += getViewWidth(view)
        }
    }

    private fun getViewHeight(view: View): Int {
        val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
        return view.measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
    }

    private fun getViewWidth(view: View): Int {
        val marginLayoutParams: MarginLayoutParams = view.layoutParams as MarginLayoutParams
        return view.measuredWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}
