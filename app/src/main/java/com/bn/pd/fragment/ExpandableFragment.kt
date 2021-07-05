package com.bn.pd.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a.findfragment.EmptyFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import java.util.*

@FragmentAnnotation("扩展的TextView", "UI")
class ExpandableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.expandable_fragment, container, false)
//        val view = FrameLayout(container!!.context)
        view.setBackgroundColor(getRandomColor())
        return view
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.rgb(random.nextInt(254), random.nextInt(254), random.nextInt(254))
    }

    companion object {
        fun newInstance(): Fragment = EmptyFragment()
    }
}
