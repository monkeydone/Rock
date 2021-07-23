package com.a.sync.mvvm.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.a.base.RBaseFragment
import com.a.base.observer
import com.a.findfragment.ListActivity
import com.a.sync.*
import com.a.sync.client.DoKitWsClient
import com.a.sync.databinding.FragmentSyncSimpleBinding
import com.a.sync.mvvm.viewmodel.SyncListViewModel
import com.a.sync.mvvm.viewmodel.SyncSimpleViewModel
import com.a.sync.server.DoKitWsServer
import com.a.sync.server.HostInfo
import com.a.videoplayer.mvvm.fragment.VideoPlayerFragment
import com.bn.utils.random
import com.bn.utils.string
import com.bn.utils.toast
import com.jwsd.libzxing.activity.CaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class SyncSimpleFragment : RBaseFragment<SyncSimpleViewModel, FragmentSyncSimpleBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_sync_simple

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
        observer(liveConnectionInfo) {
            handleConnectionInfo(it)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sync_scan_qr -> {
                val intent = Intent(requireContext(), CaptureActivity::class.java)
                intent.putExtra("type", 0)
                startActivityForResult(intent, REQUEST_CONNECT_CODE)
            }
            R.id.tv_sync_simple_create_qr -> {
//                val fragmentName = "com.a.sync.mvvm.fragment.ServerInfoSimpleFragment"
                val fragmentName =
                    ServerInfoSimpleFragment.javaClass.canonicalName.replace(".Companion", "")
                ListActivity.startFragment(binding.root.context, fragmentName, fragmentName)
            }
            R.id.tv_sync_send_message -> {
//                sendTestMessage()
                VideoPlayerFragment.openVideo("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
            }
            R.id.tv_sync_play_video -> {
//                VideoPlayerFragment.openVideo("http://10.129.100.121:8000/m3u8?name=${100.random()}")

                sendVideoMessage("http://10.129.100.121:8000/m3u8?name=${100.random()}")
            }


        }
    }



    private fun sendTestMessage() {
        if (Utils.WS_MODE == WSMode.HOST) {
            val text = "random ${1000.random()}"
            "send message from host to client  by ${text}".toast()
            DoKitWsServer.send(
                WSEvent(
                    WSMode.HOST,
                    WSEType.WSE_TEST,
                    mutableMapOf("text" to "${text}"),
                    null
                )
            )

            SyncListViewModel.addMessage(text, Utils.WS_MODE)
        } else if (Utils.WS_MODE == WSMode.CLIENT) {
            val text = "random ${1000.random()}"
            "send message from client to host  by ${text}".toast()
            DoKitWsClient.send(
                WSEvent(
                    WSMode.CLIENT,
                    WSEType.WSE_TEST,
                    mutableMapOf("text" to "${text}"),
                    null
                )
            )
            SyncListViewModel.addMessage(text, Utils.WS_MODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
//        "test: requestCode:${requestCode} resultCode:${resultCode} data:${data?.extras}".toast()
        if (requestCode == REQUEST_CONNECT_CODE) {
            val code = data?.getStringExtra("result")
            val uri = Uri.parse(code)
            handleScanResult(uri)
        }
    }

    private fun handleConnectionInfo(info: String?) {
        binding.tvSyncListMessage.text = info
    }

    private fun handleScanResult(uri: Uri) {
        DoKitWsClient.connect(uri.host!!, uri.port, uri.path!!) { code, message ->
            withContext(Dispatchers.Main) {
                when (code) {
                    DoKitWsClient.CONNECT_SUCCEED -> {
                        Utils.HOST_INFO =
                            GsonUtils.fromJson<HostInfo>(message, HostInfo::class.java)
                        handleConnectionInfo("${R.string.sync_connect_device.string()} ${Utils.HOST_INFO?.deviceName}")
                    }
                    DoKitWsClient.CONNECT_FAIL -> {
                        handleConnectionInfo("${R.string.sync_connect_device_fail.string()}")
                    }
                    else -> {

                    }
                }
            }
        }

    }

    companion object {
        const val REQUEST_CONNECT_CODE = 101
        public var liveConnectionInfo: MutableLiveData<String> = MutableLiveData<String>()

        fun sendVideoMessage(file: File) {


        }

        fun sendVideoMessage(url: String) {
            val text = url
            if (Utils.WS_MODE == WSMode.HOST) {
                "send message from host to client  by ${text}".toast()
                DoKitWsServer.send(
                    WSEvent(
                        WSMode.HOST,
                        WSEType.WSE_VIDEO,
                        mutableMapOf(WSEType.WSE_VIDEO.toString() to "${text}"),
                        null
                    )
                )

                SyncListViewModel.addMessage(text, Utils.WS_MODE)
            } else if (Utils.WS_MODE == WSMode.CLIENT) {
                "send message from client to host  by ${text}".toast()
                DoKitWsClient.send(
                    WSEvent(
                        WSMode.CLIENT,
                        WSEType.WSE_VIDEO,
                        mutableMapOf(WSEType.WSE_VIDEO.toString() to "${text}"),
                        null
                    )
                )
                SyncListViewModel.addMessage(text, Utils.WS_MODE)
            }
        }

    }

}
