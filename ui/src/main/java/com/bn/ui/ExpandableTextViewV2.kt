package com.bn.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.bn.utils.ContextUtils
import com.bn.utils.dpToPx
import com.bn.utils.toArtColor

class ExpandableTextViewV2(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    private var message = SpannableStringBuilder()
    private val maxlines = 4
    private var contentWidth = 0
    var titleColor: Int
    var contentColor: Int
    var titleSize = 12
    var contentSize = 12
    var btnColor: Int = R.color.purple_700.toArtColor()
    var btnSize = 12
    var btnHeight = 18.0f
    var lineHeight = 12.0f
    var more = "展开"
    var some = "收起"
    private val layout: StaticLayout by lazy { getStaticLayout(message) }

    init {
        movementMethod = LinkMovementMethod.getInstance();
        highlightColor = Color.TRANSPARENT
        if (!ContextUtils.isInit()) {
            ContextUtils.init(context.applicationContext)
        }
        titleColor = R.color.purple_200.toArtColor()
        contentColor = R.color.teal_200.toArtColor()
        btnColor = R.color.purple_700.toArtColor()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (contentWidth != w - paddingLeft - paddingRight) {
            contentWidth = w - paddingLeft - paddingRight
            initAction()
        }
    }

    fun setMessage(text: String?) {
        text?.let {
            message = SpannableStringBuilder(text)
            initAction()
        }
    }

    fun getOneLineSpace(): SpannableStringBuilder {
        val content = "\n  \n"
        val b = SpannableStringBuilder(content)
//        b.setSpan(BackgroundColorSpan(Color.BLUE),0,content.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        b.setSpan(
            LineHeightSpan.Standard(dpToPx(lineHeight).toInt()),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return b
    }

    fun addMessage(title: String, data: String) {
        if (data.isEmpty()) {
            return
        }
        val b = SpannableStringBuilder(title);
        b.setSpan(
            ForegroundColorSpan(titleColor),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        b.setSpan(BackgroundColorSpan(Color.BLUE),0,title.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        b.setSpan(StyleSpan(BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        b.setSpan(
            AbsoluteSizeSpan(titleSize, true),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        b.setSpan(LineHeightSpan.Standard(com.windimg.giant.utils.ViewUtils.dpToPx(20.0f).toInt()), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val b2 = SpannableStringBuilder(data)
        b2.setSpan(
            ForegroundColorSpan(contentColor),
            0,
            data.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        b2.setSpan(
            AbsoluteSizeSpan(contentSize, true),
            0,
            data.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (message.isNotEmpty()) {
            message.append(getOneLineSpace())
            message.append(b)
            message.append("\n")
            message.append(b2)
        } else {
            message.append(b)
            message.append("\n")
            message.append(b2)
        }
        initAction()

    }

    private fun getEndString(expend: Boolean): SpannableStringBuilder {
        val content = if (expend) "\n${more}" else "\n${some}"
        val b = SpannableStringBuilder(content)
        b.setSpan(
            AbsoluteSizeSpan(btnSize, true),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        b.setSpan(
            ForegroundColorSpan(btnColor),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        b.setSpan(
            LineHeightSpan.Standard(dpToPx(btnHeight).toInt()),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return b
    }


    private fun initAction(expend: Boolean = false) {
        if (contentWidth == 0 || message.isBlank()) return

        if (expend) expand() else packUp()
    }

    private fun getStaticLayout(text: CharSequence): StaticLayout {
        return StaticLayout(
            text,
            paint,
            contentWidth,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )
    }

    /**
     * 将内容收起
     */
    private fun packUp() {
        if (layout.lineCount > maxlines) {
            val ssb = getSpannableString()
            initClick(ssb, true)
            text = ssb
        } else {
            text = message
        }
    }

    /**
     * 将"展开"添加到文章末尾
     */
    private fun getSpannableString(): SpannableStringBuilder {
        var ssb = SpannableStringBuilder()
        val endString = getEndString(true)
//        val offset = contentWidth - paint.measureText(endString)
//        var endIndex = layout.getOffsetForHorizontal(maxlines - 1, offset)

        var endIndex = layout.getLineEnd(maxlines)
        val ssr = SpannableStringBuilder()
        ssr.append(message)
        val content = ssr.delete(endIndex, message.length)
        ssb.append(content)
        while (getStaticLayout(ssb).lineCount >= maxlines + 1) {
            ssb = ssb.delete(endIndex - 1, endIndex)
            endIndex -= 1
        }
        ssb.append(endString)
        return ssb
    }

    /**
     * 将内容展开
     */
    private fun expand() {
        val ssb = SpannableStringBuilder()
        if (layout.lineCount > maxlines) {
            ssb.append(message)
            ssb.append(getEndString(false))
            initClick(ssb, false)
            text = ssb
        }
    }

    private fun initClick(ssb: SpannableStringBuilder, open: Boolean) {
        ssb.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                initAction(open)
            }

            override fun updateDrawState(ds: TextPaint) {
//                ds.color = ContextCompat.getColor(context,R.color.common_highlight_color)
//                ds.isUnderlineText = false
            }
        }, 0, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

}