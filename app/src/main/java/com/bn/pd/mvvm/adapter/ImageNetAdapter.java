package com.bn.pd.mvvm.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bn.pd.R;
import com.bn.pd.mvvm.viewmodel.BannerViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

/**
 * 自定义布局，网络图片
 */
public class ImageNetAdapter extends BannerAdapter<BannerViewModel.DataBean, ImageHolder> {

    public ImageNetAdapter(List<BannerViewModel.DataBean> mDatas) {
        super(mDatas);
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) BannerUtils.getView(parent, R.layout.banner_image);
        //通过裁剪实现圆角
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView, 20);
        }
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, BannerViewModel.DataBean data, int position, int size) {
        //通过图片加载器实现圆角，你也可以自己使用圆角的imageview，实现圆角的方法很多，自己尝试哈
        Glide.with(holder.itemView)
                .load(data.getImageUrl())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.imageView);
    }

}

class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public ImageHolder(@NonNull View view) {
        super(view);
        this.imageView = (ImageView) view;
    }
}