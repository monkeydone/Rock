package com.bn.pd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bn.pd.databinding.ActivityMainBinding
import com.bn.pd.javassist.PersonService
import com.bn.utils.ContextUtils
import com.bn.utils.toast

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextUtils.init(baseContext.applicationContext)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tvHello.setOnClickListener {

            PersonService().testStr1.toast()
//            Toast.makeText(it.getContext(), PersonService().testStr1, Toast.LENGTH_LONG).show()
        }
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
        binding.tvExpandable.addMessage("说明", content)
        binding.tvExpandable.addMessage("说明", content)

    }
}