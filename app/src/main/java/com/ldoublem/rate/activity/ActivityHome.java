package com.ldoublem.rate.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.ldoublem.rate.R;
import com.ldoublem.rate.baseActivity.BaseActivity;
import com.ldoublem.rate.baseActivity.G;
import com.ldoublem.rate.entity.Currency;
import com.ldoublem.rate.entity.Rate;
import com.ldoublem.rate.utils.BitmapUtil;
import com.ldoublem.rate.utils.DBUtil;
import com.ldoublem.rate.utils.ParseEntity;
import com.ldoublem.rate.utils.RataAdapter;
import com.ldoublem.rate.view.SwipeMenuListView.SwipeMenu;
import com.ldoublem.rate.view.SwipeMenuListView.SwipeMenuCreator;
import com.ldoublem.rate.view.SwipeMenuListView.SwipeMenuItem;
import com.ldoublem.rate.view.SwipeMenuListView.SwipeMenuListView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lumingmin on 16/5/22.
 */
public class ActivityHome extends BaseActivity {


    @Bind(R.id.riv_country)
    RoundedImageView riv_country;
    @Bind(R.id.tv_code)
    TextView tv_code;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.sml_list)
    SwipeMenuListView sml_list;
    @Bind(R.id.et_number)
    EditText et_number;


    private List<Rate> mDataList = new ArrayList<>();

    private List<Rate> mGetDataList = new ArrayList<>();


    private Rate isFromCurrencyRate = null;
    RataAdapter adapter = null;
    long amount = 0;


    @Override
    public int setContentView() {
        return R.layout.layout_activity_home;
    }

    @Override
    public void initView() {
        intChlidView();
        showInfo();
        getHttpGetCurrencyInfo(isFromCurrencyRate);


    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void getRateList(List<Rate> rates, int fromType) {

        showInfo();
        getHttpGetCurrencyInfo(isFromCurrencyRate);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            openActivity(ActivityAbout.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(String result, String tag) {
        super.getResult(result, tag);
        et_number.setEnabled(true);

        if (tag.contains(G.HttpGetCurrency)) {

            Currency hdata = new ParseEntity<Currency>().parseOne(result, Currency.class);
            if (hdata != null) {
                DBUtil.UpdateRateEntity(mFinalDb, hdata.getRetData().getCurrency(), hdata.getRetData().getToCurrency());
            }
            if (tag.equals(G.HttpGetCurrency + mGetDataList.get(mGetDataList.size() - 1).getCode())) {
                Message m = mHandler.obtainMessage();
                mHandler.sendMessageDelayed(m, 300);
            }
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showInfo();
        }
    };


    @Override
    public void getError(int status, String error, String tag) {
        super.getError(status, error, tag);

        et_number.setText("0");
        et_number.setEnabled(false);


    }

    private void showInfo() {
        isFromCurrencyRate = DBUtil.getRateFromCurrencyEntity(mFinalDb);


        if (isFromCurrencyRate == null) {
            getHttpInfo();
        } else {

            String imageUri = "flag/" + isFromCurrencyRate.getCode().toLowerCase() + ".png";
            riv_country.setImageBitmap(BitmapUtil.getImageFromAssetsFile(ActivityHome.this, imageUri));
            tv_code.setText(isFromCurrencyRate.getCode().toUpperCase());
            tv_name.setText(isFromCurrencyRate.getName());


            list_notifyDataSetChanged();

        }


    }

    private void list_notifyDataSetChanged() {

        mDataList.clear();
        mGetDataList.clear();
        mDataList = DBUtil.getRateSelectListEntity(mFinalDb);
        mGetDataList = DBUtil.getRateSelectAllListEntity(mFinalDb);


        adapter.setList(mDataList, amount);
//        adapter.notifyDataSetChanged();


    }


    private void intChlidView() {


        adapter = new RataAdapter(ActivityHome.this);

        sml_list.setAdapter(adapter);

        sml_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Rate r = (Rate) parent.getAdapter().getItem(position);
                DBUtil.UpdateRateisFromCurrencyEntity(mFinalDb, r, isFromCurrencyRate);
                showInfo();
                getHttpGetCurrencyInfo(r);


            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                if (menu.getViewType() == 0) {
                    setMenuStyle(menu, R.mipmap.change);
                }
            }

        };
        sml_list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        sml_list.setMenuCreator(creator);
        sml_list.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

                if (sml_list.mTouchView != null && sml_list.mTouchView.isOpen()) {
                    Bundle b = new Bundle();
                    b.putSerializable("RATE", (Rate) adapter.getItem(position));
                    openActivity(ActivityAddRate.class, b, G.RequestCodeByHome);
                    sml_list.mTouchView.closeMenu();
                }
            }
        });


        et_number.addTextChangedListener(new TextWatcher() {
                                             @Override
                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                             }

                                             @Override
                                             public void onTextChanged(CharSequence s, int start, int before, int count) {


                                                 if (s.toString().equals("")) {
                                                     amount = 0;
                                                 } else {
                                                     amount = Long.valueOf(s.toString());
                                                 }
//                                                 adapter.setAmount(amount);
                                                 adapter.setList(mDataList, amount);

                                             }

                                             @Override
                                             public void afterTextChanged(Editable s) {

                                                 String text = s.toString();

                                                 if (text.length() > 11) {
                                                     et_number.setText(text.substring(0, 11));
                                                     et_number.setSelection(11);
                                                 }


                                             }
                                         }

        );
    }


    private void setMenuStyle(SwipeMenu menu, int iconid) {


        // create item
        SwipeMenuItem deleteItem = new SwipeMenuItem(this);
        // set item background

        deleteItem
                .setBackground(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        // set item width
        deleteItem.setWidth(dp2px(160));
        // set a icon
        deleteItem.setIcon(iconid);
        // add to menu
        menu.addMenuItem(deleteItem);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
                .getDisplayMetrics());
    }

    private void getHttpGetCurrencyInfo(Rate isFromCurrencyRate) {
        for (Rate r : mGetDataList) {
            getHttpGetCurrency(isFromCurrencyRate.getCode(), r.getCode(), G.HttpGetCurrency + r.getCode());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == G.RequestCodeByHome) {
            showInfo();
        }


    }
}
