package com.qinmr.util.ui.video.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.qinmr.mylibrary.adapter.BaseAdapter;
import com.qinmr.mylibrary.callback.ILoadDataView;
import com.qinmr.mylibrary.listener.OnRequestDataListener;
import com.qinmr.util.R;
import com.qinmr.util.adapter.VideoListAdapter;
import com.qinmr.util.db.table.VideoInfo;
import com.qinmr.util.helper.RecyclerViewHelper;
import com.qinmr.util.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by mrq on 2017/4/25.
 */

public class VideoListFragment extends BaseFragment implements ILoadDataView<List<VideoInfo>> {

    private static final String VIDEO_ID_KEY = "VideoIdKey";

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;

    private String mVideoId;
    private VideoListHelper helper;
    private BaseAdapter mAdapter;

    public static VideoListFragment newInstance(String videoId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID_KEY, videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoId = getArguments().getString(VIDEO_ID_KEY);
            helper = new VideoListHelper(this, mVideoId);
        }
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    public void initViews() {
        mAdapter = new VideoListAdapter(mContext);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvPhotoList, slideAdapter);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                helper.getMoreData();
            }
        });
    }

    @Override
    public void updateViews() {
        helper.getData();
    }

    @Override
    public void loadData(List<VideoInfo> data) {
        mAdapter.updateItems(data);
    }

    @Override
    public void loadMoreData(List<VideoInfo> data) {
        mAdapter.loadComplete();
        mAdapter.addItems(data);
    }

    @Override
    public void loadNoData() {
        mAdapter.noMoreData();
    }
}