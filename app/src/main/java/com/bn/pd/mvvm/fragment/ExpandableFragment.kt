package com.bn.pd.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.a.findfragment.EmptyFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.ExpandableFragmentBinding

@FragmentAnnotation("扩展的TextView", "UI")
class ExpandableFragment : Fragment() {
    lateinit var binding: ExpandableFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(this.layoutInflater, R.layout.expandable_fragment, null, false)

//        val view = inflater.inflate(R.layout.expandable_fragment, container, false)
//        val view = FrameLayout(container!!.context)
//        view.setBackgroundColor(getRandomColor())

        val content = """
                   binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tvHello.setOnClickListener {

            PersonService().testStr1.toast()
//            Toast.makeText(it.getContext(), PersonService().testStr1, Toast.LENGTH_LONG).show()
        }        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tvHello.setOnClickListener {

            PersonService().testStr1.toast()
//            Toast.makeText(it.getContext(), PersonService().testStr1, Toast.LENGTH_LONG).show()
        }

        """.trimIndent()

        binding.tvExpandable.addMessage("点我", content)
        binding.tvExpandable.addMessage("另外一个title", content)

//        binding.addMessage("说明", content)
//        binding.tvExpandable.addMessage("说明", content)
        return binding.root
    }


    companion object {
        fun newInstance(): Fragment = EmptyFragment()
    }
}
