package com.ldoublem.rate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ldoublem.rate.R;
import com.ldoublem.rate.baseActivity.BaseActivity;
import com.ldoublem.rate.baseActivity.G;
import com.ldoublem.rate.entity.Currency;
import com.ldoublem.rate.entity.Rate;
import com.ldoublem.rate.utils.DBUtil;
import com.ldoublem.rate.utils.ParseEntity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;
import com.zhy.base.adapter.recyclerview.support.SectionAdapter;
import com.zhy.base.adapter.recyclerview.support.SectionSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lumingmin on 16/5/22.
 */
public class ActivityAddRate extends BaseActivity {


    @Bind(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @Bind(R.id.et_code)
    EditText et_code;
    private List<Rate> mDatas = new ArrayList<>();

    private SectionAdapter<Rate> adapter;


    private Rate mChangeRate;
    private List<Rate> mDataList = new ArrayList<>();
    private Rate isFromCurrencyRate = null;

    @Override
    public int setContentView() {
        return R.layout.layout_activity_addrate;
    }

    @Override
    public void initView() {

        getIntentInfo();
        initBar();
        intChlidView();
        TaskGetList task = new TaskGetList(null);
        task.execute();

        mDataList = DBUtil.getRateSelectAllListEntity(mFinalDb);
        isFromCurrencyRate = DBUtil.getRateFromCurrencyEntity(mFinalDb);


    }

    private void getIntentInfo() {
        mChangeRate = (Rate) getIntent().getExtras().getSerializable("RATE");

    }


    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }


    @Override
    public void getRateList(List<Rate> rates, int fromType) {

        if (fromType == 0 && rates.size() == 0) {
            getHttpInfo();
        } else {
            mDatas.clear();
            mDatas.addAll(rates);
            adapter.notifyDataSetChanged();
            mRecyclerView.refreshComplete();
        }
    }

    private void initBar() {
        toolbar.setTitle(R.string.action_add);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void intChlidView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.mipmap.icon_font_downblack);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        SectionSupport<Rate> sectionSupport = new SectionSupport<Rate>() {
            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.list_addatte_item_top;
            }

            @Override
            public int sectionTitleTextViewId() {
                return R.id.id_header_title;
            }

            @Override
            public String getTitle(Rate s) {

                if (s.getIsUsual() == 1) {
                    return "常用";
                }

                return s.getCode().substring(0, 1).toUpperCase();
            }
        };

        adapter = new SectionAdapter<Rate>(this, R.layout.list_addrate_item, mDatas, sectionSupport) {

            @Override
            public void convert(ViewHolder holder, final Rate rate) {
                holder.setText(R.id.id_item_list_title, rate.getCode() + "   " + rate.getName());

                RoundedImageView riv_country = (RoundedImageView) holder.getConvertView().findViewById(R.id.riv_country);
                String imageUri = "assets://flag/" + rate.getCode().toLowerCase() + ".png";//+CPResourceUtil.getMipmapId(ActivityHome.this, r.getCode().toLowerCase()); // from drawables
                mImageloader.displayImage(imageUri, riv_country);

                if (rate.getIsSelect() == 1) {
                    holder.getConvertView().findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
                } else {
                    holder.getConvertView().findViewById(R.id.iv_selected).setVisibility(View.INVISIBLE);

                }


                holder.getConvertView().setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {


                        for (Rate r : mDataList) {
                            if (r.getCode().equals(rate.getCode())) {
                                showToast("该货币已在列表中了");
                                return;
                            }
                        }
                        getCurrencyByRate(rate);
                    }
                });


            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getHttpInfo();
            }

            @Override
            public void onLoadMore() {

            }
        });

        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                TaskGetList task;
                if (!s.toString().equals("")) {
                    task = new TaskGetList(s.toString());
                    task.execute();
                } else {
                    task = new TaskGetList(null);
                    task.execute();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
//
//
//                Rate r = mDatas.get(position - 1);
////                showToast(r.getName());
//
//                for (Rate rate : mDataList) {
//                    if (r.getCode().equals(rate.getCode())) {
//                        showToast("该货币已在列表中了");
//                        return;
//                    }
//                }
////                showToast(r.getName());
//                getCurrencyByRate(r);
//
//            }
//
//            @Override
//            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
//                return false;
//            }
//        });


    }


    @Override
    public void getResult(String result, String tag) {
        super.getResult(result, tag);


        if (tag.contains(G.HttpGetCurrency)) {

            Currency hdata = new ParseEntity<Currency>().parseOne(result, Currency.class);
            if (hdata != null) {
                DBUtil.UpdateRateEntity(mFinalDb, hdata.getRetData().getCurrency(), hdata.getRetData().getToCurrency());
                Rate rate = new Rate();
                rate.setCode(hdata.getRetData().getToCurrency());
                DBUtil.UpdateRateisSelectEntity(mFinalDb, mChangeRate, false);
                DBUtil.UpdateRateisSelectEntity(mFinalDb, rate, true);
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }


        }


    }

    @Override
    public void getError(int status, String error, String tag) {

        super.getError(status, error, tag);
    }


    private void getCurrencyByRate(Rate r) {

        if (isFromCurrencyRate != null) {
            getHttpGetCurrency(isFromCurrencyRate.getCode(), r.getCode(), G.HttpGetCurrency);

        }


    }


}
