package com.bn.pd.mvvm.fragment

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentRegexBinding
import com.bn.pd.mvvm.viewmodel.RegexViewModel


@FragmentAnnotation("Regex", "Demo")
class RegexFragment : RBaseFragment<RegexViewModel, FragmentRegexBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_regex

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    private fun findAll(): String {
        val src2 = "电话:010-12345678;传真:010-10171695;备用:010-20141017"
        val re2 = """(\d{3}-\d{8})"""   //"\\d{3}-\\d{8}"
        var result = "$src2\n $re2"
        Regex(re2).findAll(src2)
            .forEach { result = result + "\n" + (it.value + " 位置：[" + it.range + "]") }
        return result
    }

    private fun findAll2(): String {
        val src2 = "电话:010-12345678;[^1],传真:010-10171695;[^2],备用:010-20141017"
        val re2 = """\[\^\d\]"""
        var result = "$src2\n $re2"
        Regex(re2).findAll(src2)
            .forEach { result = result + "\n" + (it.value + " 位置：[" + it.range + "]") }
        return result
    }

    private fun findAll3(): SpannableStringBuilder {
        val src2 = "电话:010-12345678;[^1],传真:010-10171695;[^2],备用:010-20141017"
        var sb = SpannableStringBuilder(src2)
        val re2 = """\[\^\d\]"""
        var result = "$src2\n $re2"
        Regex(re2).findAll(src2).forEach {
            sb.setSpan(
                ForegroundColorSpan(Color.BLUE),
                it.range.start,
                it.range.start + it.value.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return sb
    }

    private fun findAll4(): SpannableStringBuilder {
        val src2 = "电话:010-12345678;[^1],传真:010-10171695;[^2],备用:010-20141017"
        var sb = SpannableStringBuilder(src2.replace(Regex("""\[\^"""), """\["""))
        val re2 = """\[\d\]"""
        Regex(re2).findAll(sb).forEach {
            sb.setSpan(
                ForegroundColorSpan(Color.BLUE),
                it.range.start,
                it.range.start + it.value.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return sb
    }

    private fun match1() {
        val r1 = Regex("[a-z]+")
        val r2 = Regex("[a-z]+", RegexOption.IGNORE_CASE)
        val text = "ABCdef"
        val content = """
            regex: ${r1.pattern}
            text: ${text} 
            result:${r1.matches(text)}
            
            regex: ${r2.pattern} ignore case
            text:${text}
            result:${r2.matches(text)}
        """.trimIndent()
        binding.tvMatch.text = content
    }

    private fun match2() {
        val r1 = Regex("[a-z]+")
        val r2 = Regex("[a-z]+")
        val text = "ABCdef"
        val text2 = "ABC"
        val content = """
            regex: ${r1.pattern}
            text: ${text} 
            result:${r1.containsMatchIn(text)}
            
            regex: ${r2.pattern}
            text:${text2}
            result:${r2.containsMatchIn(text2)}
        """.trimIndent()
        binding.tvMatchContains.text = content
    }


    private fun replace1() {
        val r1 = Regex("[a-z]+")
        val r2 = Regex("[0-9]+")
        val r3 = Regex("""\[\^[0-9]+\]""")
        val text = "ABCdef"
        val text2 = "1234ABC1234"
        val text3 = """
            [^1]
            this is text
            [^2]
            this is text2
        """.trimIndent()
        val content = """
            regex: ${r1.pattern}
            text: ${text} 
            replace: result to 1234
            result:${r1.replace(text, "1234")}
            
            regex: ${r2.pattern}
            text:${text2}
            replace: result * 2
            result:${r2.replace(text2, { (it.value.toInt() * 2).toString() })}
            
            regex: ${r3.pattern}
            text: ${text3}
            replace: ^ to ""
            result:${r3.replace(text3, { it.value.toString().replace("^", "") })}
            
        """.trimIndent()
        binding.tvReplace.text = content
    }


    private fun matchEntire() {
        val r1 = Regex("[0-9]+")
        val text = "ABCdef"
        val text2 = "12341234"
        val content = """
            regex: ${r1.pattern}
            text: ${text} 
            result:${r1.matchEntire(text)}
            
            regex: ${r1.pattern}
            text:${text2}
            result:${r1.matchEntire(text2)}
            
            regex: ${r1.pattern}
            text: ${text2}
            result:${r1.matchEntire(text2)?.value}
            
        """.trimIndent()
        binding.tvMatchEntire.text = content
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_findall -> {
                val result = findAll()
                binding.tvFindall.text = result
            }
            R.id.tv_find_all2 -> {
                val result = findAll2()
                binding.tvFindAll2.text = result
            }
            R.id.tv_find_all3 -> {
                val result = findAll3()
                binding.tvFindAll3.text = result
            }
            R.id.tv_find_replace_all -> {
                val result = findAll4()
                binding.tvFindReplaceAll.text = result
            }
            R.id.tv_match -> {
                match1()
            }
            R.id.tv_match_contains -> {
                match2()
            }
            R.id.tv_replace -> {
                replace1()
            }
            R.id.tv_match_entire -> {
                matchEntire()
            }
        }
    }


}
