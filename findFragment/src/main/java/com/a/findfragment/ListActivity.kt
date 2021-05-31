package com.a.findfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        if (savedInstanceState == null) {
            val fragmentName = intent.getStringExtra(FRAGMENT_NAME)
            val f =
                if (fragmentName == null) ListFragment() else createFragmentByName(fragmentName!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, f)
                .commitNow()
        }
    }

    companion object {
        const val FRAGMENT_NAME = "fragmentName"
        const val PARAM_PARENT_NAME = "fragmentParentName"
        fun startFragment(context: Context, fragmentName: String, parentName: String = "") {
            val intent = Intent();
            intent.setClass(context, ListActivity::class.java)
            intent.putExtra(FRAGMENT_NAME, fragmentName)
            intent.putExtra(PARAM_PARENT_NAME, parentName)
            context.startActivity(intent)
        }

        fun startListActivity(context: Context) {
            val intent = Intent();
            intent.setClass(context, ListActivity::class.java)
            context.startActivity(intent)
        }

        fun createFragmentByName(fragmentName: String): Fragment {
            val f = Class.forName("$fragmentName").newInstance() as Fragment
            return f;
        }
    }
}