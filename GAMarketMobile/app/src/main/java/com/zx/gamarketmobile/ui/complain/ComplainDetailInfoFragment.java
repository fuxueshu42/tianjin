package com.zx.gamarketmobile.ui.complain;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.gamarketmobile.R;
import com.zx.gamarketmobile.adapter.CaseCompDetailInfoAdapter;
import com.zx.gamarketmobile.entity.ComplainInfoDetailsBean;
import com.zx.gamarketmobile.entity.KeyValueInfo;
import com.zx.gamarketmobile.http.ApiData;
import com.zx.gamarketmobile.http.BaseHttpResult;
import com.zx.gamarketmobile.ui.base.BaseFragment;
import com.zx.gamarketmobile.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By Stanny On 2017/3/22
 * 功能：统计分析基本信息Fragment
 */
public class ComplainDetailInfoFragment extends BaseFragment {

    private CaseCompDetailInfoAdapter mCompAdapter;
    private RecyclerView mRvInfo;
    private String fId = "";
    private ApiData getCompById = new ApiData(ApiData.HTTP_ID_compInfoById);
    private List<KeyValueInfo> dataInfoList = new ArrayList<>();

    public static ComplainDetailInfoFragment newInstance(String fId) {
        ComplainDetailInfoFragment details = new ComplainDetailInfoFragment();
        details.fId = fId;
        return details;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.normal_swipe_recycler_view, container, false);
        mRvInfo = (RecyclerView) view.findViewById(R.id.rv_normal_view);
        view.findViewById(R.id.srl_normal_layout).setEnabled(false);
        getCompById.setLoadingListener(this);
        mRvInfo.setLayoutManager(mLinearLayoutManager);
        mCompAdapter = new CaseCompDetailInfoAdapter(getActivity(), dataInfoList);
        mRvInfo.setAdapter(mCompAdapter);
        return view;
    }

    //当界面可见之后再进行数据加载（懒加载）
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && dataInfoList.size() == 0) {
            getCompById.loadData(fId);
        }
    }

    @Override
    public void onLoadComplete(int id, BaseHttpResult b) {
        super.onLoadComplete(id, b);
        switch (id) {
            case ApiData.HTTP_ID_compInfoById:
                ComplainInfoDetailsBean compInfoEntity = (ComplainInfoDetailsBean) b.getEntry();
                getDataList(compInfoEntity.getBaseInfo());
                mCompAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void getDataList(ComplainInfoDetailsBean.BaseInfoBean compInfoEntity) {
        dataInfoList.clear();
        KeyValueInfo info = new KeyValueInfo("投诉编号: ", compInfoEntity.getFRegId());
        dataInfoList.add(info);
        info = new KeyValueInfo("登记人: ", compInfoEntity.getFRegName());
        dataInfoList.add(info);
        info = new KeyValueInfo("登记单位: ", compInfoEntity.getFRegOrg());
        dataInfoList.add(info);
        info = new KeyValueInfo("信息来源: ", compInfoEntity.getFSource());
        dataInfoList.add(info);
        info = new KeyValueInfo("类型: ", compInfoEntity.getFType());
        dataInfoList.add(info);
        info = new KeyValueInfo("所在乡镇: ", compInfoEntity.getFStation());
        dataInfoList.add(info);
        info = new KeyValueInfo("投诉举报人姓名: ", compInfoEntity.getFProductName());
        dataInfoList.add(info);
//        info = new KeyValueInfo("投诉举报人联系方式: ", compInfoEntity.getfpro());
//        dataInfoList.add(info);
        info = new KeyValueInfo("投诉举报分类: ", compInfoEntity.getFAcrossType());
        dataInfoList.add(info);
        info = new KeyValueInfo("被投诉举报方名称或姓名: ", compInfoEntity.getFBrandName());
        dataInfoList.add(info);
//        info = new KeyValueInfo("被投诉举报方联系方式: ", compInfoEntity.getfByReportedTel());
//        dataInfoList.add(info);
//        info = new KeyValueInfo("被投诉方地址: ", compInfoEntity.getfByReportedAddress());
//        dataInfoList.add(info);
        info = new KeyValueInfo("登记时间: ", DateUtil.getDateFromMillis(compInfoEntity.getFRegTime()));
        dataInfoList.add(info);
        info = new KeyValueInfo("投诉举报内容: ", compInfoEntity.getFContent());
        dataInfoList.add(info);
    }

}
