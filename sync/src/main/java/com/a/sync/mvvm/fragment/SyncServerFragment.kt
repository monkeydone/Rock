package com.a.sync.mvvm.fragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.view.View
import com.a.base.RBaseFragment
import com.a.sync.*
import com.a.sync.client.DoKitWsClient
import com.a.sync.databinding.FragmentSyncServerBinding
import com.a.sync.mvvm.viewmodel.SyncServerViewModel
import com.a.sync.server.DoKitWsServer
import com.a.sync.server.HostInfo
import com.bn.utils.random
import com.bn.utils.toast
import com.jwsd.libzxing.QRCodeManager
import com.jwsd.libzxing.activity.CaptureActivity
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SyncServerFragment : RBaseFragment<SyncServerViewModel, FragmentSyncServerBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_sync_server

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
        binding.tvCreateQr.setImageBitmap(
            QRCodeManager.getInstance().createQRCode("this is a text", 200, 200)
        )
        val host = "ws://${Utils.IP_ADDRESS_BY_WIFI}:${Utils.MC_WS_PORT}/mc"
        binding.ivCode.setImageBitmap(QRCodeManager.getInstance().createQRCode(host, 500, 500))
        binding.tvHost.text = host

        if (Utils.WS_MODE == WSMode.UNKNOW) {
            DoKitWsServer.start {
                requireActivity().runOnUiThread {
                    "server running...".toast()
                    binding.tvEvent.text = "${host} created"
                }
            }
        }

    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                val intent = Intent(requireContext(), CaptureActivity::class.java)
                intent.putExtra("type", 0)
                startActivityForResult(intent, REQUEST_CODE)

            }
            R.id.tv_connect_server -> {
                val intent = Intent(requireContext(), CaptureActivity::class.java)
                intent.putExtra("type", 0)
                startActivityForResult(intent, REQUEST_CONNECT_CODE)
            }
            R.id.tv_event -> {
                PermissionX.init(activity)
                    .permissions(
                        Manifest.permission.CAMERA
                    )
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            "All permissions are granted".toast()
                        } else {
                            "These permissions are denied: $deniedList".toast()
                        }
                    }
            }

            R.id.tv_send_message -> {
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
                }

            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        "test: requestCode:${requestCode} resultCode:${resultCode} data:${data?.extras}".toast()
        if (requestCode == REQUEST_CODE) {
            binding.tvEvent.text = data?.getStringExtra("result")
        } else if (requestCode == REQUEST_CONNECT_CODE) {
            val code = data?.getStringExtra("result")
//            val code = "ws://10.129.100.121:4444/mc"
            val uri = Uri.parse(code)
            handleScanResult(uri)
        }

    }

    /**
     * 处理返回结果
     */
    private fun handleScanResult(uri: Uri) {
        DoKitWsClient.connect(uri.host!!, uri.port, uri.path!!) { code, message ->
            withContext(Dispatchers.Main) {
                when (code) {
                    DoKitWsClient.CONNECT_SUCCEED -> {
                        message?.toast()
                        binding.tvEvent.text = message?.toString()
                        Utils.HOST_INFO =
                            GsonUtils.fromJson<HostInfo>(message, HostInfo::class.java)
                    }
                    DoKitWsClient.CONNECT_FAIL -> {
                        message?.toast()
                    }
                    else -> {

                    }
                }
            }
        }

    }

    companion object {
        const val REQUEST_CODE = 100
        const val REQUEST_CONNECT_CODE = 101
    }

}
