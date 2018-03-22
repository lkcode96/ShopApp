package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.FloatingLayout;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huang on 2016/9/11.
 */
public class WBrandActivity extends BaseActivity {
    public static final int goback = 2;
    @BindView(R.id.forewadImg)
    TextView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.floatinglayout)
    FloatingLayout floatinglayout;
    GridLayoutManager layoutManager;

    private List<TagGroup> datas = new ArrayList<>();
    String name="";
    @Override
    protected int getLayoutId() {
        return R.layout.goodsbrand;
    }

    @Override
    protected void initData() {
        title.setText("选择品牌");
        layoutManager = new GridLayoutManager(mContext, 4);
        if (NetUtils.isNetworkConnected(mContext)) {
            getBrandList();
        } else {
            showToastShort("无网络");
        }
        floatinglayout.setOnItemCheckedChangeListener(new FloatingLayout.OnItemCheckedChangeListener() {
            @Override
            public void onItemCheckedChange(int position) {
                String name = datas.get(position).getName();
                String bid = datas.get(position).getId();
                Bundle b = new Bundle();
                b.putString("name", name);
                b.putString("id", bid);
                CommonUtils.goResult(mContext, b, WBrandActivity.goback);
            }
        });
    }



    /**
     * 获取品牌列表
     */
    private void getBrandList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("name", name);
        HttpHelper.getInstance().post(mContext, Contants.PortA.GoodsBrand, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                Log.e("m_tag", json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<TagGroup> jsonHelper = new JsonHelper<TagGroup>(TagGroup.class);
                    datas.addAll(jsonHelper.getDatas(json));
                    floatinglayout.setTags(datas, "getName");
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }


    @OnClick({R.id.forewadImg, R.id.id_right_btu, R.id.submitTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forewadImg:
                finish();
                break;
            case R.id.id_right_btu:
                break;
            case R.id.submitTv:
                name = valueEt.getText().toString().trim();
                getBrandList();
                break;
            default:
                break;
        }
    }


}