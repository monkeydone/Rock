package com.a.findfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import java.util.*

@Keep
@FragmentAnnotation("简单例子", "root")
class EmptyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_empty, container, false)
        val view = FrameLayout(container!!.context)
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
