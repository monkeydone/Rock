package com.a.findfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


open class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLeftExitMode()
        setContentView(R.layout.activity_list)
        initAnnotation(this)
        if (savedInstanceState == null) {
            val fragmentName = intent.getStringExtra(FRAGMENT_NAME)
            val f =
                if (fragmentName == null) ListFragment() else createFragmentByName(fragmentName!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, f)
                .commitNow()
        }
    }

    open fun setLeftExitMode() {
//        Slidr.attach(this)
    }

    companion object {
        const val FRAGMENT_NAME = "fragmentName"
        const val PARAM_PARENT_NAME = "fragmentParentName"

        var init = false
        fun startFragment(context: Context, fragmentName: String, parentName: String = "") {
            val intent = Intent();
            intent.setClass(context, ListActivity::class.java)
            intent.putExtra(FRAGMENT_NAME, fragmentName)
            intent.putExtra(PARAM_PARENT_NAME, parentName)
            context.startActivity(intent)
        }

        fun initAnnotation(context: Context) {
            if (!init) {
                val annotated = LocalPackage.getAnnotationClasses(
                    FragmentAnnotation::class.java,
                    context.packageCodePath
                )
                val parentNameList = ArrayList<String>()
                for (i in annotated) {
                    val c = i.getAnnotationsByType(FragmentAnnotation::class.java)
                    if (c.isNotEmpty()) {
                        ListViewModel.addAnnotation(c[0], i.canonicalName)
                        if (c[0].parentName.isNotEmpty() && !parentNameList.contains(c[0].parentName)) {
                            parentNameList.add(c[0].parentName)
                        }
                    }
                }

                for (i in parentNameList) {
                    val data = ListViewModel.ListDataModel(
                        i,
                        FragmentAnnotationData(
                            i,
                            "",
                            "com.a.findfragment.ListFragment"
                        )
                    )
                    ListViewModel.addAnnotation(data)
                }

                init = true
            }
        }

        fun startListActivity(context: Context) {
            val intent = Intent();
            intent.setClass(context, ListActivity::class.java)
            context.startActivity(intent)
        }

        fun createFragmentByName(fragmentName: String): Fragment {
            val f = Class.forName("$fragmentName".trim()).newInstance() as Fragment
            return f;
        }
    }
}