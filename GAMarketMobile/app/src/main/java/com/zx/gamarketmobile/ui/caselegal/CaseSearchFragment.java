package com.zx.gamarketmobile.ui.caselegal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.gamarketmobile.R;
import com.zx.gamarketmobile.adapter.CaseListAdapter;
import com.zx.gamarketmobile.entity.CaseInfoEntity;
import com.zx.gamarketmobile.entity.NormalListEntity;
import com.zx.gamarketmobile.http.ApiData;
import com.zx.gamarketmobile.http.BaseHttpResult;
import com.zx.gamarketmobile.listener.LoadMoreListener;
import com.zx.gamarketmobile.listener.MyItemClickListener;
import com.zx.gamarketmobile.ui.base.BaseFragment;
import com.zx.gamarketmobile.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By Xiangb On 2017/3/10
 * 功能：案件查询fragment
 */
public class CaseSearchFragment extends BaseFragment implements LoadMoreListener, MyItemClickListener, View.OnClickListener {

    private RecyclerView rvSearch;
    private SwipeRefreshLayout srlSearch;
    private EditText etSearchDetail;
    private ImageView ivDoSearch;
    private CaseListAdapter mAdapter;
    private List<CaseInfoEntity> dataList = new ArrayList<>();
    private int mPageSize = 10;
    private int mPageNo = 1;
    private int mTotalNo = 0;
    private ApiData pageAyxx = new ApiData(ApiData.HTTP_ID_casePageAyxx);

    public static CaseSearchFragment newInstance() {
        CaseSearchFragment fragment = new CaseSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_search, container, false);
        etSearchDetail = (EditText) view.findViewById(R.id.et_case_search);
        ivDoSearch = (ImageView) view.findViewById(R.id.iv_case_search);
        rvSearch = (RecyclerView) view.findViewById(R.id.rv_normal_view);
        srlSearch = (SwipeRefreshLayout) view.findViewById(R.id.srl_normal_layout);
        pageAyxx.setLoadingListener(this);
        rvSearch.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CaseListAdapter(getActivity(), dataList, false);
        rvSearch.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this);
        srlSearch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPageNo > 1) {
                    mPageNo--;
                }
                loadData();
            }
        });
        ivDoSearch.setOnClickListener(this);
        etSearchDetail.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    Util.closeKeybord(etSearchDetail, getActivity());
                    mPageNo = 1;
                    loadData();
                }
                return false;
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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    //item点击事件
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
        intent.putExtra("entity", dataList.get(position));
        intent.putExtra("showExcute", false);
        startActivity(intent);
    }

    //数据加载
    private void loadData() {
        pageAyxx.loadData(etSearchDetail.getText().toString(), mPageNo, mPageSize, userInfo.getDepartmentAlias());
    }

    @Override
    public void onLoadComplete(int id, BaseHttpResult b) {
        super.onLoadComplete(id, b);
        srlSearch.setRefreshing(false);
        switch (id) {
            case ApiData.HTTP_ID_casePageAyxx:
                NormalListEntity normalListEntity = (NormalListEntity) b.getEntry();
                mTotalNo = normalListEntity.getTotal();
                mAdapter.setStatus(0, mPageNo, mTotalNo);
                List<CaseInfoEntity> entityList = normalListEntity.getCaseInfoEntityList();
                dataList.clear();
                dataList.addAll(entityList);
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_case_search:
                mPageNo = 1;
                loadData();
                break;

            default:
                break;
        }
    }
}