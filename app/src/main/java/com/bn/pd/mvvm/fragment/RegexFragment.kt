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
        }
    }


}
