package com.bn.pd.mvvm.fragment

import android.Manifest
import android.view.View
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.bn.pd.R
import com.bn.pd.databinding.FragmentOtherBinding
import com.bn.pd.mvvm.viewmodel.OtherViewModel
import com.bn.utils.AppUtils
import com.bn.utils.toast
import com.example.floatingwindowdemo.custom.floatview.FloatWindowHelper
import com.permissionx.guolindev.PermissionX
import java.io.File


@FragmentAnnotation("Other", "Demo")
class OtherFragment : RBaseFragment<OtherViewModel, FragmentOtherBinding>(), View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_other

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
        Aria.download(this).register()

    }

    override fun initData() {
        viewModel.loadData()
    }

    var taskId: Long = 0

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        binding.tvPercent.text = "speed ${task.speed} percent:${task.percent} path:${task.filePath}"

    }


    @Download.onTaskComplete
    fun complete(task: DownloadTask) {
        requireActivity().runOnUiThread {
            AppUtils.installApk(requireActivity(), File(task.filePath))
        }
        binding.tvPercent.text = "speed ${task.speed} percent:${task.percent} path:${task.filePath}"
    }

    private fun getFile(apkName: String): File? {
        // 使用缓存目录,这个时候不需要申请存储权限
        // 目录不存在，那么创建
        val dir = File(requireContext().getExternalCacheDir(), "download")
        if (!dir.exists()) {
            dir.mkdir()
        }
        // 创建文件
        return File(dir, apkName)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_download -> {
                "message".toast()
                val downlaod_url =
                    "https://release.windimg.com/tmp/iOS/artgain/app-release_71d4258c80e5f3c341890632248d8028ac4d5a2d.apk"
                taskId = Aria.download(this)
                    .load(downlaod_url) //读取下载地址
                    .setFilePath(getFile("a.apk")?.absolutePath) //设置文件保存的完整路径
                    .create() //创建并启动下载

                AppUtils.checkoutInstallPermission(requireActivity())
            }

            R.id.tv_request_permission -> {
                PermissionX.init(activity)
                    .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            "All permissions are granted".toast()
                        } else {
                            "These permissions are denied: $deniedList".toast()
                        }
                    }
            }
            R.id.tv_update_apk -> {
                val downlaod_url =
                    "https://release.windimg.com/tmp/iOS/artgain/app-release_71d4258c80e5f3c341890632248d8028ac4d5a2d.apk"
                FloatWindowHelper.requestPermission(requireActivity())

            }


        }
    }


}
