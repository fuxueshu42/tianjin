package com.zx.tjmarketmobile.ui.infomanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.tjmarketmobile.R;
import com.zx.tjmarketmobile.adapter.infomanager.LegalSelectLawAdapter;
import com.zx.tjmarketmobile.entity.KeyValueInfo;
import com.zx.tjmarketmobile.http.ApiData;
import com.zx.tjmarketmobile.http.BaseHttpResult;
import com.zx.tjmarketmobile.listener.LoadMoreListener;
import com.zx.tjmarketmobile.listener.MyItemClickListener;
import com.zx.tjmarketmobile.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzq on 2017/3/23.
 */

public class LegalSelectLawFragment extends BaseFragment implements LoadMoreListener, MyItemClickListener {
    private static final String TAG = "StandardMessageSelectFragment";
    private RecyclerView rvTodo;
    private SwipeRefreshLayout srlTodo;
    private LegalSelectLawAdapter mAdapter;
    private List<KeyValueInfo> dataList = new ArrayList<>();
    private int mPageSize = 10;
    public static String msg = "领取";
    public int mPageNo = 1;
    public int mTotalNo = 0;
    private ApiData getInfoStandar = new ApiData(ApiData.HTTP_ID_info_manager_legal_search);

    public static LegalSelectLawFragment newInstance() {
        LegalSelectLawFragment fragment = new LegalSelectLawFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_to_do_list, container, false);

        rvTodo = (RecyclerView) view.findViewById(R.id.rv_normal_view);
        srlTodo = (SwipeRefreshLayout) view.findViewById(R.id.srl_normal_layout);
        rvTodo.setLayoutManager(mLinearLayoutManager);
        getInfoStandar.setLoadingListener(this);
        mAdapter = new LegalSelectLawAdapter(getActivity(), dataList, true);
        rvTodo.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this);
        srlTodo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPageNo > 1) {
                    mPageNo--;
                }
                loadData();
            }
        });
        return view;
    }

    //加载更多
    @Override
    public void LoadMore() {
        if (mPageNo * mPageSize < mTotalNo) {
            mPageNo++;
            mAdapter.setStatus(1, mPageNo, mTotalNo);
            loadData();
        }
    }

    //item点击事件
    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    //数据加载
    @SuppressLint("LongLogTag")
    private void loadData() {
        if (msg.length() != 0) {
            getInfoStandar.loadData(msg);
        } else
            getInfoStandar.loadData("领取");
    }

    public void load(final String msg) {
        this.msg = msg;
        getInfoStandar.loadData(msg);
    }

    @Override
    public void onLoadComplete(int id, BaseHttpResult b) {
        super.onLoadComplete(id, b);
        srlTodo.setRefreshing(false);
        switch (id) {
            case ApiData.HTTP_ID_info_manager_legal_search:
                List<KeyValueInfo> myTaskListEntity = (List<KeyValueInfo>) b.getEntry();
                dataList.clear();
                dataList.addAll(myTaskListEntity);
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }
}
