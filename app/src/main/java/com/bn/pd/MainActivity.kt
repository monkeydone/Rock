package com.bn.pd

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bn.pd.databinding.ActivityMainBinding
import com.bn.pd.javassist.PersonService

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tvHello.setOnClickListener {

            Toast.makeText(it.getContext(), PersonService().testStr1, Toast.LENGTH_LONG).show()
        }

    }
}