package com.qinmr.util.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;
import com.qinmr.util.R;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图集adapter
 * Created by mrq on 2017/4/20.
 */
public class PhotoSetAdapter extends PagerAdapter {

    private List<String> mImgList;
    private Context mContext;
    private OnTapListener mTapListener;

    public PhotoSetAdapter(Context context, List<String> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
    }


    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_photo_pager, null, false);
        final PhotoView photo = (PhotoView) view.findViewById(R.id.iv_photo);
        final SpinKitView loadingView = (SpinKitView) view.findViewById(R.id.loading_view);
        final TextView tvReload = (TextView) view.findViewById(R.id.tv_reload);

        final RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.GONE);
                photo.setImageDrawable(resource);
                return true;
            }
        };
//        ImageLoader.loadFitCenter(mContext, mImgList.get(position % mImgList.size()), photo, requestListener);
        Glide.with(mContext).load(mImgList.get(position % mImgList.size())).fitCenter().dontAnimate().listener(requestListener).into(photo);
        photo.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mTapListener != null) {
                    mTapListener.onPhotoClick();
                }
            }
        });
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvReload.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
//                ImageLoader.loadFitCenter(mContext, mImgList.get(position % mImgList.size()), photo, requestListener);
                Glide.with(mContext).load(mImgList.get(position % mImgList.size())).fitCenter().dontAnimate().listener(requestListener).into(photo);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setTapListener(OnTapListener listener) {
        mTapListener = listener;
    }

    public interface OnTapListener {
        void onPhotoClick();
    }
}
