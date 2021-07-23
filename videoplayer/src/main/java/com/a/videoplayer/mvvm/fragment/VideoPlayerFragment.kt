package com.a.videoplayer.mvvm.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import com.a.base.RBaseFragment
import com.a.findfragment.FragmentAnnotation
import com.a.findfragment.ListActivity
import com.a.videoplayer.R
import com.a.videoplayer.databinding.FragmentVideoPlayerBinding
import com.a.videoplayer.mvvm.viewmodel.VideoPlayerViewModel
import com.a.videoplayer.mvvm.viewmodel.VideoPlayerViewModel.Companion.PARAM_URL
import com.bn.utils.ContextUtils
import com.bn.utils.PermissionUtils
import com.bn.utils.toast
import com.permissionx.guolindev.PermissionX
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*


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
        val url: String = requireActivity().intent.getStringExtra(PARAM_URL) ?: ""
        if (url.isNotEmpty()) {
            binding.player.setVideoController(getControllView(true, ""))
            binding.player.setUrl(url)
            binding.player.start()
        }
    }


    private fun getControllView(isLive: Boolean, title: String): StandardVideoController {
        val controller = StandardVideoController(requireContext())
        //根据屏幕方向自动进入/退出全屏
        //根据屏幕方向自动进入/退出全屏
        controller.setEnableOrientation(true)

        val prepareView = PrepareView(requireContext()) //准备播放界面

        prepareView.setClickStart()
        val thumb: ImageView = prepareView.findViewById(R.id.thumb) //封面图

//        Glide.with(this).load(xyz.doikki.dkplayer.activity.api.PlayerActivity.THUMB).into(thumb)
        controller.addControlComponent(prepareView)

        controller.addControlComponent(CompleteView(requireContext())) //自动完成播放界面


        controller.addControlComponent(ErrorView(requireContext())) //错误界面


        val titleView = TitleView(requireContext()) //标题栏
        titleView.setTitle(title)
        controller.addControlComponent(titleView)

        //根据是否为直播设置不同的底部控制条

        //根据是否为直播设置不同的底部控制条
//        val isLive: Boolean = intent.getBooleanExtra(IntentKeys.IS_LIVE, requireActivity())
        if (isLive) {
            controller.addControlComponent(LiveControlView(requireContext())) //直播控制条
        } else {
            val vodControlView = VodControlView(requireContext()) //点播控制条
            //是否显示底部进度条。默认显示
//                vodControlView.showBottomProgress(false);
            controller.addControlComponent(vodControlView)
        }

        val gestureControlView = GestureView(requireContext()) //滑动控制视图

        controller.addControlComponent(gestureControlView)
        //根据是否为直播决定是否需要滑动调节进度
        //根据是否为直播决定是否需要滑动调节进度
        controller.setCanChangePosition(!isLive)

        //设置标题

//        //设置标题
//        val title: String = intent.getStringExtra(IntentKeys.TITLE)
//        titleView.setTitle(title)

        //注意：以上组件如果你想单独定制，我推荐你把源码复制一份出来，然后改成你想要的样子。
        //改完之后再通过addControlComponent添加上去
        //你也可以通过addControlComponent添加一些你自己的组件，具体实现方式参考现有组件的实现。
        //这个组件不一定是View，请发挥你的想象力😃

        //如果你不需要单独配置各个组件，可以直接调用此方法快速添加以上组件
//            controller.addDefaultControlComponent(title, isLive);

        //竖屏也开启手势操作，默认关闭
//            controller.setEnableInNormal(true);
        //滑动调节亮度，音量，进度，默认开启
//            controller.setGestureEnabled(false);
        //适配刘海屏，默认开启
//            controller.setAdaptCutout(false);
        //双击播放暂停，默认开启
//            controller.setDoubleTapTogglePlayEnabled(false);

        //在控制器上显示调试信息

        //注意：以上组件如果你想单独定制，我推荐你把源码复制一份出来，然后改成你想要的样子。
        //改完之后再通过addControlComponent添加上去
        //你也可以通过addControlComponent添加一些你自己的组件，具体实现方式参考现有组件的实现。
        //这个组件不一定是View，请发挥你的想象力😃

        //如果你不需要单独配置各个组件，可以直接调用此方法快速添加以上组件
//            controller.addDefaultControlComponent(title, isLive);

        //竖屏也开启手势操作，默认关闭
//            controller.setEnableInNormal(true);
        //滑动调节亮度，音量，进度，默认开启
//            controller.setGestureEnabled(false);
        //适配刘海屏，默认开启
//            controller.setAdaptCutout(false);
        //双击播放暂停，默认开启
//            controller.setDoubleTapTogglePlayEnabled(false);

        //在控制器上显示调试信息
//        controller.addControlComponent(DebugInfoView(this))
        //在LogCat显示调试信息
        //在LogCat显示调试信息
//        controller.addControlComponent(PlayerMonitor())

        //如果你不想要UI，不要设置控制器即可

        //如果你不想要UI，不要设置控制器即可
//        mVideoView.setVideoController(controller)
        return controller
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
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.player.release()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.message -> {
                "message".toast()
            }

        }
    }

    companion object {
        fun openVideo(context: Context, url: String) {
            val fragment = VideoPlayerFragment.javaClass.canonicalName
            val intent = Intent()
            intent.putExtra(VideoPlayerViewModel.PARAM_URL, url)
            ListActivity.startFragmentWithBundle(context, fragment, intent)
        }

        fun openVideo(url: String) {
            val topActivity = ContextUtils.getTopActivity()
            if (topActivity != null) {
                openVideo(topActivity, url)
            }
        }
    }

}
