package com.a.base

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton

abstract class RBaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : Fragment(),
    BackPressHandler {

    lateinit var binding: DB
    lateinit var viewModel: VM

    protected lateinit var rootView: FrameLayout

    private lateinit var skeleton: Skeleton
    private lateinit var stoneView: ViewGroup
    private lateinit var loadingView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initIntent()
        initObserver()
    }

    open fun initViewModel() {
        ClassUtil.getViewModel<VM>(this).also {
            it?.also {
                viewModel = ViewModelProvider(this).get(it)
                doObserve(viewModel)
            }
        }
    }

    protected fun doObserve(viewModel: BaseViewModel<*>) {
        observer(viewModel.loadingLive) { handleSkeleton(it) }
        viewModel.obserLoading(this) { handleLoading(it) }
    }

    /**
     * 处理  骨架屏(只显示一次)
     *      loadingView
     */
    private fun handleSkeleton(show: Boolean) = if (show) showSkeleton() else hideSkeleton()

    private fun handleLoading(show: Boolean) =
        if (show) showLoadingDialog() else hideLoadingDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initRootView(inflater, container)
        initLoadingState()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    /**
     * 添加一个根布局
     */
    private fun initRootView(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = FrameLayout(inflater.context)
        if (getContentId() != 0) {
            binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)
            binding.lifecycleOwner = this
            rootView.addView(binding.root)
        }
    }

    private fun initLoadingState() {
        when {
            getLoadingId() != 0 -> initLoadingView()
            getStoneId() != 0 -> initStoneView()
            getStoneItemId() != 0 -> initStoneItem()
        }
    }

    private fun initLoadingView() {
        loadingView = View.inflate(context, getLoadingId(), rootView) as ViewGroup
        loadingView.visibility = View.GONE
    }

    private fun initStoneView() {
        skeleton = View.inflate(context, getStoneId(), null)
            .createSkeleton(SkeletonConfig.default(requireContext()))
        stoneView = NestedScrollView(requireContext())
        stoneView.addView(skeleton as ViewGroup, layoutParams())
        rootView.addView(stoneView, layoutParams())
        stoneView.visibility = View.GONE
    }

    private fun initStoneItem() {
        stoneView = RecyclerView(requireContext()).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        skeleton = (stoneView as RecyclerView).applySkeleton(getStoneItemId(), 10)
        rootView.addView(stoneView, layoutParams())
        stoneView.visibility = View.GONE
    }

    fun showLoadingDialog() {
//        if (activity is BaseActivity) {
//            (activity as BaseActivity).showLoadingDialog()
//        }
    }

    fun hideLoadingDialog() {
//        if (activity is BaseActivity) {
//            (activity as BaseActivity).hideLoadingDialog()
//        }
    }

    private fun showSkeleton() {
        when {
            getLoadingId() != 0 -> loadingView.visibility = View.VISIBLE
            getStoneId() != 0 || getStoneItemId() != 0 -> {
                if (stoneView.tag == null) {
                    stoneView.tag = true
                    stoneView.visibility = View.VISIBLE
                    skeleton.showSkeleton()
                }
            }
            else -> {
            }
        }
    }

    private fun hideSkeleton() {
        when {
            getLoadingId() != 0 -> loadingView.visibility = View.GONE
            getStoneId() != 0 || getStoneItemId() != 0 -> {
                if (stoneView.visibility == View.VISIBLE) {
                    stoneView.visibility = View.GONE
                    skeleton.showOriginal()
                }
            }
            else -> {
            }
        }
    }

    override fun onBackPressed(): Boolean {
        val shouldInterceptBackPress = shouldInterceptBackPress()
        if (shouldInterceptBackPress) consumeBackPress()
        return shouldInterceptBackPress
    }

    open fun consumeBackPress() {
        if (isAdded) {
            requireActivity().finish()
        }
    }

    open fun shouldInterceptBackPress() = true

    fun reload() {
        initData()
    }

    /**
     * 通知该Fragment下的 所有子Fragment进行刷新
     */
    protected fun postChildrenRefresh() {
        childFragmentManager.fragments.forEach {
            if (it is RBaseFragment<*, *>) it.initData()
        }
    }

    /**
     * 设置骨架屏资源id
     * 与getStoneItemId()互斥
     */
    open fun getStoneId() = 0

    /**
     * 设置列表骨架屏item的资源id
     * 与getStoneId()互斥
     */
    open fun getStoneItemId() = 0

    /**
     * 设置全屏loading的资源id
     */
    open fun getLoadingId() = 0

    /**
     * 设置加载的页面资源id
     */
    abstract fun getContentId(): Int
    abstract fun initView()
    abstract fun initData()

    /**
     * 初始化 一些在View生命周期前的数据
     */
    open fun initIntent() {}

    /**
     * 初始化 DataBinding对应布局的参数
     */
    open fun initBinding() {}

    /**
     * 初始化 针对所有LiveData的监听
     */
    open fun initObserver() {}

    fun bindingIsInitialized() = ::binding.isInitialized
    fun viewModelIsInitialized() = ::viewModel.isInitialized

    private fun Int.color() = ContextCompat.getColor(requireContext(), this)
    private fun Float.dp() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

    private fun layoutParams() = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}