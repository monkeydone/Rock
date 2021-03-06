package com.bn.pd.mvvm.fragment

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.bn.pd.R
import com.bn.pd.databinding.FragmentFlowBinding
import com.bn.pd.mvvm.viewmodel.FlowViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis


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

            }

            R.id.tv_flow_1 -> {
                mainScope.launch {
                    val list = viewModel.simpleList(6)
                    list.collect {
                        binding.tvFlow1.text = "it: ${it}"
                    }
                    binding.tvFlow1.text = "End List"
                    list.collect {
                        binding.tvDispatchThreadName2.text = "it : ${it}"
                    }
                    binding.tvDispatchThreadName2.text = "End List"
                }
            }

            R.id.tv_flow_2 -> {
                mainScope.launch {
                    val list = viewModel.simpleList(8)
                    withTimeoutOrNull(300) {
                        list.collect {
                            withContext(Dispatchers.Main) {
                                binding.tvFlow1.text = "it: ${it}"

                            }
                        }

                    }
                    withContext(Dispatchers.Main) {
                        binding.tvDispatchThreadName2.text = "End List"

                    }


                }
            }
            R.id.tv_flow_3 -> {
                mainScope.launch {
                    (1..4).asFlow().collect {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            binding.tvFlow3.text = "it: ${it}"
                        }
                    }
                }
            }
            R.id.tv_flow_map -> {
                mainScope.launch {
                    (1..4).asFlow().map {
                        viewModel.mapHelper("${it}")
                    }.collect {
                        delay(100)
                        withContext(Dispatchers.Main) {
                            binding.tvFlowMap.text = "it : ${it}"
                        }
                    }
                }
            }
            R.id.tv_flow_transform -> {
                mainScope.launch {
                    (1..4).asFlow().map {
                        FlowViewModel.FlowResult(it, viewModel.mapHelper("${it}"))
                    }.transform { it ->
                        val it2 = FlowViewModel.FlowResult(it.value, "desc: ${it.desc}")
                        emit(it)
                        emit(it2)
                    }.collect {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            binding.tvFlowMap.text = "it:${it}"
                        }
                    }
                }
            }
            R.id.tv_flow_take -> {
                mainScope.launch {
                    val n = (1..4).asFlow()

                    n.take(2).collect {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            binding.tvFlowTake.text = "it:${it}"
                        }
                    }
                }
            }
            R.id.tv_flow_reduce -> {
                mainScope.launch {
                    val n = (1..5).asFlow()
                    val sum = n.map { it * it }.reduce { a, b ->
                        a + b
                    }

                    withContext(Dispatchers.Main) {
                        binding.tvFlowReduce.text = "(1..5).sum ${sum}"
                    }
                }
            }
            R.id.tv_flow_filter -> {
                mainScope.launch {
                    (1..15).asFlow().filter {
                        it % 2 == 0
                    }.map {
                        delay(1000)
                        "string ${it}"
                    }.collect {
                        withContext(Dispatchers.Main) {
                            binding.tvFlowFilter.text = "${it}"
                        }
                    }
                }

            }
            R.id.tv_flow_flowOn -> {
                val n = (1..15).asFlow().filter {
                    it % 2 == 0
                }.map {
                    delay(500)
                    "string ${it}"
                }.flowOn(Dispatchers.IO)

                mainScope.launch {
                    val time = measureTimeMillis {
                        n.collect {
                            delay(1000)
                            withContext(Dispatchers.Main) {
                                binding.tvFlowFlowOn.text = "${it}"
                            }
                        }
                    }
                    binding.tvFlowFlowOn.text = "time: ${time}"

                }


            }
            R.id.tv_flow_buffer -> {
                val n = (1..15).asFlow().filter {
                    it % 2 == 0
                }.map {
                    delay(500)
                    "string ${it}"
                }.flowOn(Dispatchers.IO)

                mainScope.launch {
                    val time = measureTimeMillis {
                        n.buffer().collect {
                            delay(1000)
                            withContext(Dispatchers.Main) {
                                binding.tvFlowFlowOn.text = "${it}"
                            }
                        }
                    }
                    binding.tvFlowFlowOn.text = "time: ${time}"

                }
            }

            R.id.tv_flow_conflate -> {
                mainScope.launch {
                    viewModel.simpleList(5).conflate().collect {
                        delay(3000)
                        binding.tvFlowConflate.text = "${it}"

                    }
                }

            }
            R.id.tv_flow_collectLastest -> {
                mainScope.launch {
                    viewModel.simpleList(5).collectLatest {
                        delay(3000)
                        binding.tvFlowCollectLastest.text = "${it}"

                    }
                }

            }
            R.id.tv_flow_zip -> {
                mainScope.launch {
                    showLoadingDialog()

                    val n = viewModel.simpleList(10, 100)
                    val s = flowOf("one", "two", "three").onEach { 400 }

                    n.zip(s) { a, b -> "$a -> $b" }
                        .collect {
                            hideLoadingDialog()
                            delay(100)
                            binding.tvFlowZip.text = "$it"
                        }

                }
            }
            R.id.tv_flow_combine -> {
                mainScope.launch {
                    showLoadingDialog()

//                    val n = viewModel.simpleList(10,500)
                    val n = (1..10).asFlow().onEach { 400 }
                    val s = flowOf("one", "two", "three").onEach { 1000 }

                    n.combine(s) { a, b -> "$a -> $b" }
                        .onCompletion {
                            binding.tvFlowCombine.text =
                                binding.tvFlowCombine.getText().toString() + "Done"
                        }
                        .collect {
                            hideLoadingDialog()
                            delay(100)
                            binding.tvFlowCombine.text =
                                binding.tvFlowCombine.getText().toString() + " \n $it"
                        }


                }
            }

            R.id.tv_flow_cancel -> {
                mainScope.launch {
                    showLoadingDialog()

//                    val n = viewModel.simpleList(10,500)
                    val n = (1..10).asFlow().onEach { 400 }

                    n
                        .collect {
                            hideLoadingDialog()
                            delay(100)
                            if (it == 5) cancel()
                            binding.tvFlowCombine.text =
                                binding.tvFlowCombine.getText().toString() + " \n $it"
                        }


                }
            }
            R.id.tv_channel_base -> {
                val channel = Channel<Int>()
                showLoadingDialog()

                mainScope.launch {
                    for (i in 0..20) {
                        delay(100)
//                        binding.tvChannelBase.text = "${i}"
                        mainScope.launch {
                            channel.send(i)
                        }
                    }

                    repeat(20) {
                        val t = channel.receive()
                        withContext(Dispatchers.Main) {
                            hideLoadingDialog()
                            appendText(binding.tvChannelBase, t.toString())
                        }
                    }
                }


            }

            R.id.tv_channel_base2 -> {
                val channel = Channel<Int>()

                mainScope.launch {
                    showLoadingDialog()
                    for (i in 0..20) {
                        delay(100)
                        if (i == 10) {
                            channel.close()
                            break
                        }
                        mainScope.launch {
                            channel.send(i)
                        }

                    }

                    for (i in channel) {
                        val t = i
                        withContext(Dispatchers.Main) {
                            hideLoadingDialog()
                            appendText(binding.tvChannelBase, t.toString())
                        }
                    }


                }


            }
            R.id.tv_channel_base3 -> {
                mainScope.launch {
                    val numbers = produceNumbers(this)
                    val squares = square(this, numbers)
                    var text = ""

                    repeat(20) {
                        val t = squares.receive()
                        text += " $t"
                        withContext(Dispatchers.Main) {
                            binding.tvChannelBase3.text = text
                        }

                    }
                }


            }

            R.id.tv_channel_base4 -> {
                mainScope.launch {
                    var text = ""

                    val numbers = produceNumbers(this)
                    repeat(4) {
                        mainScope.launch {
                            val l = launchProcessor(this, it, numbers)
                            for (i in l) {
                                delay(100)
                                val t = i
                                text += " $t \n"
                                withContext(Dispatchers.Main) {
                                    binding.tvChannelBase3.text = text
                                }
                            }
                        }


                    }


                }


            }

            R.id.tv_channel_base5 -> {
                val channel = Channel<String>()
                mainScope.launch {
                    var text = ""
                    mainScope.launch {
                        createString(channel, "foo", 200L)

                    }
                    mainScope.launch {
                        createString(channel, "foo2", 500L)
                    }

                    for (i in channel) {
                        delay(100)
                        text += "$i \n"
                        withContext(Dispatchers.Main) {
                            binding.tvChannelBase5.text = text
                        }
                    }
//                    this.coroutineContext.cancelChildren()

                }


            }


        }
    }

    suspend fun createString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }

    fun produceNumbers(p: CoroutineScope) = p.produce<Int> {
        var x = 1
        while (true) send(x++)
    }

    fun launchProcessor(
        p: CoroutineScope,
        id: Int,
        numbers: ReceiveChannel<Int>
    ): ReceiveChannel<String> = p.produce {
        for (x in numbers) send("Processor ${id} receiver ${x} ")
    }


    fun square(p: CoroutineScope, numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = p.produce {
        for (x in numbers) send(x * x)
    }

    fun appendText(textView: TextView, src: String) {
        textView.text = textView.text.toString() + "\n" + src
    }


}
