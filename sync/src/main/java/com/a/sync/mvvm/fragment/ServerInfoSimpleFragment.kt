package com.a.sync.mvvm.fragment

import android.view.View
import com.a.base.RBaseFragment
import com.a.sync.R
import com.a.sync.Utils
import com.a.sync.WSMode
import com.a.sync.databinding.FragmentServerInfoSimpleBinding
import com.a.sync.mvvm.viewmodel.ServerInfoSimpleViewModel
import com.a.sync.server.DoKitWsServer
import com.bn.utils.toast
import com.jwsd.libzxing.QRCodeManager


class ServerInfoSimpleFragment :
    RBaseFragment<ServerInfoSimpleViewModel, FragmentServerInfoSimpleBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_server_info_simple

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel

        val host = "ws://${Utils.IP_ADDRESS_BY_WIFI}:${Utils.MC_WS_PORT}/mc"
        binding.ivCode.setImageBitmap(QRCodeManager.getInstance().createQRCode(host, 500, 500))
        binding.tvHost.text = host

        if (Utils.WS_MODE == WSMode.UNKNOW) {
            DoKitWsServer.start {
                requireActivity().runOnUiThread {
                    "server running...".toast()
                    binding.tvEvent.visibility = View.VISIBLE
                    binding.tvEvent.text = "${host} created"
                }
            }
        } else if (Utils.WS_MODE == WSMode.HOST) {
            binding.tvEvent.visibility = View.VISIBLE
            binding.tvEvent.text = "${host} created"
        }
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }

    companion object {

    }

}
