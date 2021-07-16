package com.a.sync.mvvm.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.ListActivity
import com.a.sync.GsonUtils
import com.a.sync.R
import com.a.sync.Utils
import com.a.sync.client.DoKitWsClient
import com.a.sync.databinding.FragmentSyncSimpleBinding
import com.a.sync.mvvm.viewmodel.SyncSimpleViewModel
import com.a.sync.server.HostInfo
import com.bn.utils.toast
import com.jwsd.libzxing.activity.CaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SyncSimpleFragment : RBaseFragment<SyncSimpleViewModel, FragmentSyncSimpleBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_sync_simple

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
    }

    override fun initData() {
        viewModel.loadData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sync_scan_qr -> {
                val intent = Intent(requireContext(), CaptureActivity::class.java)
                intent.putExtra("type", 0)
                startActivityForResult(intent, REQUEST_CONNECT_CODE)
            }
            R.id.tv_sync_simple_create_qr -> {
                val fragmentName = "com.a.sync.mvvm.fragment.ServerInfoSimpleFragment"
                ListActivity.startFragment(binding.root.context, fragmentName, fragmentName)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        "test: requestCode:${requestCode} resultCode:${resultCode} data:${data?.extras}".toast()
        if (requestCode == REQUEST_CONNECT_CODE) {
            val code = data?.getStringExtra("result")
//            val code = "ws://10.129.100.121:4444/mc"
            val uri = Uri.parse(code)
            handleScanResult(uri)
        }

    }

    private fun handleScanResult(uri: Uri) {
        DoKitWsClient.connect(uri.host!!, uri.port, uri.path!!) { code, message ->
            withContext(Dispatchers.Main) {
                when (code) {
                    DoKitWsClient.CONNECT_SUCCEED -> {
                        message?.toast()
//                        binding.tvEvent.text = message?.toString()
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
