package com.bn.pd.mvvm.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentFlowBinding
import com.bn.pd.mvvm.viewmodel.FlowViewModel
import kotlinx.coroutines.*


@FragmentAnnotation("Flow", "Demo")
class FlowFragment : RBaseFragment<FlowViewModel, FragmentFlowBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_flow

    val mainScope = MainScope()
    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.loadingLive.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t == true) {
                    showLoadingDialog()
                } else {
                    hideLoadingDialog()
                }
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                viewModel.simpleCoroutines {
                    binding.message.text = "Hello World"
                }
            }
            R.id.tv_event -> {
                viewModel.simpleCoroutines2(1000) {
                    binding.tvEvent.text = "Show Hello world 1000ms"
                }
            }
            R.id.tv_job1 -> {
                viewModel.simpleJob1(1000) {
                    binding.tvJob1.text = "Job Not Join"
                }
            }

            R.id.tv_job2 -> {
                viewModel.simpleJob2(1000) {
                    binding.tvJob2.text = "Job And Join"
                }
            }
            R.id.tv_job3 -> {
                viewModel.simpleJob3 {
                    binding.tvJob3.text = it
                }
            }
            R.id.tv_repeat -> {
                viewModel.simpleJobRepeat {
                    binding.tvRepeat.text = it
                }
            }
            R.id.tv_cancel_job -> {
                viewModel.jobCancel {
                    binding.tvCancelJob.text = it
                }
            }

            R.id.tv_cancel_job1 -> {
                viewModel.jobNotCancel {
                    binding.tvCancelJob1.text = it
                }
            }
            R.id.tv_cancel_job2 -> {
                viewModel.jobCancelOk {
                    binding.tvCancelJob2.text = it
                }
            }
            R.id.tv_cancel_exception -> {
                viewModel.jobCancelException {
                    binding.tvCancelException.text = it
                }
            }
            R.id.tv_cancel_finally1 -> {
                viewModel.jobCancelException2 {
                    binding.tvCancelFinally1.text = it
                }
            }
            R.id.tv_cancel_nonCancellable -> {
                viewModel.jobNoCancellable {
                    binding.tvCancelNonCancellable.text = it
                }
            }
            R.id.tv_timeout -> {
                viewModel.jobTimeout {
                    binding.tvTimeout.text = it
                }
            }
            R.id.tv_async -> {
                viewModel.doSomething {
                    binding.tvAsync.text = it
                }
            }
            R.id.tv_async_lazy -> {
                viewModel.doSomethingMeasureTime {
                    binding.tvAsyncLazy.text = it
                }

            }
            R.id.tv_dispatch_thread_name -> {
                viewModel.dispatchRun {
                    binding.tvDispatchThreadName.text = it
                }
            }
            R.id.tv_dispatch_thread_name2 -> {
                val threadLocal = ThreadLocal<String>()

                val job = mainScope.launch(Dispatchers.IO) {
                    threadLocal.set("IO")

                    withContext(Dispatchers.Main) {

                        binding.tvDispatchThreadName2.text =
                            "main:" + Thread.currentThread().name + ",local:" + threadLocal.get()
                        threadLocal.set("main")

                    }

                    yield()
                    threadLocal.set("IO2")

                    delay(1000)
                    withContext(Dispatchers.Main) {
                        binding.tvDispatchThreadName2.text =
                            "after.yield. main:" + Thread.currentThread().name + ",local:" + threadLocal.get()

                    }
                }
                mainScope.launch {
                    job.join()
                }
//                mainScope.launch(Dispatchers.Main+CoroutineName("test")) {
//                    binding.tvDispatchThreadName2.text = Thread.currentThread().name
//                }
            }

        }
    }


}
