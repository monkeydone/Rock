package com.bn.pd.mvvm.fragment

import android.content.res.ColorStateList
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.text.style.ImageSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentSpanBinding
import com.bn.pd.mvvm.viewmodel.SpanViewModel
import com.bn.utils.toArtColor
import com.bn.utils.toast


@FragmentAnnotation("Span", "Demo")
class SpanFragment : RBaseFragment<SpanViewModel, FragmentSpanBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_span

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel

        superscript()
        superscript2()
        backgroundText()
        foregroundText()
        maskFilterText()
        strikethroughText()
        underlineText()
        absoluteSizeText()
        relativeSizeText()
        imageText()
        scaleXText()
        styleText()
        bulletSpan()
//        bulletSpan2()
        quoteSpan()
//        quoteSpan2()

        alignmentSpanText()
        clickableSpanText()
        typefaceText()
        urlText()
        textAppearanceText()
    }

    fun superscript() {
        val ss = SpannableString("上标123.456")
        val superScriptTextSpan = SuperscriptSpan()
        ss.setSpan(superScriptTextSpan, 0, 2, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
        val sizeSpan = AbsoluteSizeSpan(18, true)
        ss.setSpan(sizeSpan, 0, 2, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.message.setText(ss)
    }

    fun superscript2() {
        val ss = SpannableString("下标123.456")

        val sizeSpan = AbsoluteSizeSpan(18, true)
        ss.setSpan(sizeSpan, 0, 2, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        val superScriptTextSpan = SubscriptSpan()
        ss.setSpan(superScriptTextSpan, 0, 2, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.tvEvent.setText(ss)
    }

    fun backgroundText() {
        val string = SpannableString("背景颜色Text with a background color span");
        string.setSpan(BackgroundColorSpan(Color.BLUE), 12, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvBackground.setText(string)
    }

    fun foregroundText() {
        val string = SpannableString("前景颜色Text with a background color span");
        string.setSpan(ForegroundColorSpan(Color.RED), 12, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvForeground.setText(string)
    }

    fun maskFilterText() {
        val string = SpannableString("浮雕效果Text with a background color span");
        val blurMask: MaskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
        val blurMaskFilterSpan = MaskFilterSpan(BlurMaskFilter(3.0f, BlurMaskFilter.Blur.OUTER))

        string.setSpan(MaskFilterSpan(blurMask), 10, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(blurMaskFilterSpan, 22, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvMaskfilter.setText(string)
    }

    fun strikethroughText() {
        val string = SpannableString("删除线Text with a background color span");
        string.setSpan(StrikethroughSpan(), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvStrikethrough.setText(string)
    }

    fun underlineText() {
        val string = SpannableString("下划线Text with a background color span");
        string.setSpan(UnderlineSpan(), 10, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvUnderLine.setText(string)
    }

    fun absoluteSizeText() {
        val string = SpannableString("字体大小Text with a background color span");
        string.setSpan(AbsoluteSizeSpan(28), 10, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvAbsoluteSize.setText(string)
    }


    fun relativeSizeText() {
        val string = SpannableString("相对字体大小Text with a background color span");
        string.setSpan(RelativeSizeSpan(0.5f), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvRelativeSize.setText(string)
    }


    fun imageText() {
        val imageSpan = ImageSpan(requireContext(), R.mipmap.ic_launcher, ImageSpan.ALIGN_BASELINE)
        val string = SpannableString("新增图片Text with a background color span");
        string.setSpan(imageSpan, 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvImage.setText(string)
    }

    fun typefaceText() {
        val ss = SpannableString("字体设置Text with a background color span");

        //預設的字體
        ss.setSpan(TypefaceSpan("default"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(TypefaceSpan("default-bold"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //將"在電線桿"設定成monospace字體
        ss.setSpan(TypefaceSpan("monospace"), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(TypefaceSpan("serif"), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(TypefaceSpan("sans-serif"), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTypeface.setText(ss)
    }

    fun scaleXText() {
        val string = SpannableString("X轴缩放Text with a background color span");
        string.setSpan(ScaleXSpan(2.0f), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvScaleX.setText(string)
    }

    fun styleText() {
        val string = SpannableString("文字格式斜体和粗体Text with a background color span");
        string.setSpan(StyleSpan(Typeface.BOLD), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(StyleSpan(Typeface.ITALIC), 18, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvStyle.setText(string)
    }

    fun bulletSpan() {
        val string = SpannableString("Text with\nBullet point\n文字开头的原点");
        string.setSpan(BulletSpan(), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        val string = SpannableString("文字开头的原点Text with a background color span");
//        string.setSpan(BulletSpan(), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvBullet.setText(string)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun bulletSpan2() {
        val string = SpannableString("Text with\nBullet point");
        string.setSpan(
            BulletSpan(40, R.color.permissionx_text_color.toArtColor(), 20),
            10,
            22,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvBullet.setText(string)
    }

    fun textAppearanceText() {
        var builder = SpannableStringBuilder()
        var price = "10,837"
        var note = "当前价"
        builder.append("¥")
        builder.append("${price} ")
        builder.append(note)
        builder.append("Appearance的设置方式")

        //默认字体，粗体，文字大小：60，红色
        var yuanSpan = TextAppearanceSpan(
            null, Typeface.BOLD, 60,
            ColorStateList.valueOf(Color.GREEN), null
        )
        builder.setSpan(yuanSpan, 0, 1, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        //默认字体，粗体，文字大小：80，红色
        var priceSpan = TextAppearanceSpan(
            null, Typeface.BOLD, 80,
            ColorStateList.valueOf(Color.BLUE), null
        )
        builder.setSpan(
            priceSpan,
            1,
            price.length + 1,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //serif字体，正常，文字大小：60，灰色
        var noteSpan = TextAppearanceSpan(
            "sans", Typeface.NORMAL, 60,
            ColorStateList.valueOf(Color.RED), null
        )
        builder.setSpan(
            noteSpan,
            builder.length - note.length,
            builder.length,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTextAppearance.setText(builder)
    }


    private fun urlText() {
        val intro = "各种连接情况 要連繫我的話，可以用電話、Email、簡訊、多媒體簡訊找到我，或是你想看看我的Github，或是可以開地圖看看我在哪裡"
        val ss = SpannableString(intro);

        //電話
        ss.setSpan(URLSpan("tel:123456789"), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //Email
        ss.setSpan(
            URLSpan("mailto:ericdoctor@yahoo.com.tw"),
            13,
            18,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        //網頁-我的Github
        ss.setSpan(
            URLSpan("https://github.com/NorthBei"),
            39,
            45,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        //簡訊 - 使用sms:或者smsto:
        ss.setSpan(URLSpan("sms:10086"), 19, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //多媒體簡訊 - 使用mms:或者mmsto:
        ss.setSpan(URLSpan("mms:10086"), 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //開啟預設地圖
        ss.setSpan(URLSpan("geo:24.787775, 120.997917"), 51, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        val tv = binding.tvUrl
        //在點擊之後如果要執行動作，就必須設定MovementMethod
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        //設定hover時的顏色
        tv.setHighlightColor(Color.RED);
        tv.setText(ss);
    }


    fun quoteSpan() {
        val string =
            SpannableString("引用能力Text with a background color span\n引用能力Text with a background color span \n引用能力Text with a background color span");
        val quoteSpan = QuoteSpan(Color.RED)
        //這邊設定的start(0)和end(6),start必須要是0才會出現引用線條,end基本上就沒有什麼用，只要有給值就好
        string.setSpan(quoteSpan, 0, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvQuoteSpan.setText(string)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun quoteSpan2() {
        val string = SpannableString("引用能力Text with a background color span");
        string.setSpan(QuoteSpan(Color.GREEN, 20, 40), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvQuoteSpan.setText(string)
    }

    fun alignmentSpanText() {
        val string =
            SpannableString("对其方式Text with a background color span\n 对其方式Text with a background \n 对其方式Text with a backg");
        val alignmentSpan = AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
        //這邊設定的start(0)和end(8),start必須要是0才會出現按照AlignmentSpan對齊,end基本上就沒有什麼用，只要有給值就好
        string.setSpan(alignmentSpan, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        string.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvAlignment.setText(string)
    }


    private fun clickableSpanText() {
        val ss = SpannableString("我是一串文字，點我點我")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(), "我被點了", Toast.LENGTH_SHORT).show()
            }
        }
        //"點我點我"變成可以點擊
        ss.setSpan(clickableSpan, 7, 11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        val tv = binding.tvClickable

        //在點擊之後如果要跳去執行動作，就必須設定MovementMethod
        tv.setMovementMethod(LinkMovementMethod.getInstance())
        //設定hover時的顏色
        tv.setHighlightColor(resources.getColor(R.color.permissionx_text_color))
        tv.setText(ss)
    }


    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
