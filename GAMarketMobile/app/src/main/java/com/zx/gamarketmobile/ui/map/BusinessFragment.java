package com.zx.gamarketmobile.ui.map;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.gamarketmobile.R;
import com.zx.gamarketmobile.adapter.BusinessAdapter;
import com.zx.gamarketmobile.entity.BizInfoEntity;
import com.zx.gamarketmobile.ui.base.BaseFragment;

import java.util.List;

/**
 *
 * Create By Stanny On 2016/9/22
 * 功能：业务信息
 *
 */
public class BusinessFragment extends BaseFragment {

	private List<BizInfoEntity> mBusinessList;// 业务信息
	private RecyclerView mRvBusiness;

	public static BusinessFragment newInstance(int index, List<BizInfoEntity> bizInfo) {
		BusinessFragment details = new BusinessFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		details.setArguments(args);
		details.mBusinessList = bizInfo;
		return details;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_business, container, false);
		mRvBusiness = (RecyclerView) view.findViewById(R.id.rv_normal_view);
		mRvBusiness.setLayoutManager(mLinearLayoutManager);
		((SwipeRefreshLayout)view.findViewById(R.id.srl_normal_layout)).setEnabled(false);
		BusinessAdapter businessAdapter = new BusinessAdapter(getActivity(), mBusinessList);
		mRvBusiness.setAdapter(businessAdapter);
		return view;
	}
}