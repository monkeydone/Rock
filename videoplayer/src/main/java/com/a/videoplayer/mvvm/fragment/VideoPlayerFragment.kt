package com.a.videoplayer.mvvm.fragment

import android.Manifest
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.a.videoplayer.R
import com.a.videoplayer.databinding.FragmentVideoPlayerBinding
import com.a.videoplayer.mvvm.viewmodel.VideoPlayerViewModel
import com.bn.utils.PermissionUtils
import com.bn.utils.toast
import com.permissionx.guolindev.PermissionX
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils


@FragmentAnnotation("VideoPlayer", "Demo")
class VideoPlayerFragment : RBaseFragment<VideoPlayerViewModel, FragmentVideoPlayerBinding>(),
    View.OnClickListener {
    override fun getContentId(): Int = R.layout.fragment_video_player

    override fun initView() {
        binding.onClickListener = this
        binding.viewModel = viewModel
        init2()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    }

    override fun initData() {
        viewModel.loadData()
    }


    private fun init2() {
        val imageView = ImageView(requireContext())
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.exo_controls_fastforward)
        val url = viewModel.getPlayUrl()

        val gsyVideoOptionBuilder = GSYVideoOptionBuilder()
            .setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setUrl(url)
            .setCacheWithPlay(true)
            .setVideoTitle("测试视频")
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
//                    orientationUtils.setEnable(binding.detailPlayer.isRotateWithSystem())
//                    isPlay = true
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    super.onQuitFullscreen(url, *objects)
//                    if (orientationUtils != null) {
//                        orientationUtils.backToProtVideo()
//                    }
                }
            })


        gsyVideoOptionBuilder.build(binding.videoPlayer)
        binding.videoPlayer.postDelayed(Runnable { binding.videoPlayer.startPlayLogic() }, 1000)

    }

    private fun init() {
        val videoPlayer = binding.videoPlayer
//        val source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
        val source1 = viewModel.getPlayUrl()
        videoPlayer.setUp(source1, true, "测试视频")
        videoPlayer.startWindowFullscreen(requireContext(), true, true)

        //增加封面
        val imageView = ImageView(requireContext())
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.exo_controls_fastforward)
        videoPlayer.setThumbImageView(imageView)
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE)
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE)
        //设置旋转
        val orientationUtils = OrientationUtils(requireActivity(), videoPlayer)

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton()
            .setOnClickListener(View.OnClickListener {
                orientationUtils.resolveByClick()
                videoPlayer.startWindowFullscreen(requireContext(), true, true)

            })
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(View.OnClickListener { onBackPressed() })
        videoPlayer.startPlayLogic()
    }


    private fun initRequestPermission() {
        if (!PermissionUtils.hasSelfPermissions(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
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

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            initRequestPermission()
        }
    }

    override fun consumeBackPress() {
        super.consumeBackPress()
        //释放所有
//        binding.videoPlayer.setVideoAllCallBack(null)
        requireActivity().finish()


    }

    override fun onPause() {
        super.onPause()
        binding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }


}
