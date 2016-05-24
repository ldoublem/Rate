package com.ldoublem.rate.baseActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper;
import com.ldoublem.rate.R;
import com.ldoublem.rate.entity.HttpData;
import com.ldoublem.rate.entity.Rate;
import com.ldoublem.rate.entity.RequestCurrency;
import com.ldoublem.rate.utils.DBUtil;
import com.ldoublem.rate.utils.ParseEntity;
import com.ldoublem.rate.utils.net.AsyncHttpNetCenter;
import com.ldoublem.rate.utils.net.GetHttpResult;
import com.ldoublem.rate.utils.net.HttpModelImpl;
import com.ldoublem.rate.utils.net.HttpPresenter;
import com.ldoublem.rate.utils.net.HttpPresenterImpl;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.tsz.afinal.FinalDb;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements SlidingMenu.OnOpenedListener, GetHttpResult {


    protected Toolbar toolbar;
    protected LinearLayout ly_content;
    private SlidingActivityHelper mHelper = null;
    //SlidingMenu
    private SlidingMenu mSlidingMenu;
    public HttpPresenter mHttpPresenter;
    public ImageLoader mImageloader = null;
    private Map<String, SoftReference<Rate>> mRateCache = new HashMap<String, SoftReference<Rate>>();

    protected FinalDb mFinalDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (isSupportSwipeBack()) {

            initSlidingMenu(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ly_content = (LinearLayout) findViewById(R.id.ly_content);
        View mView = (setContentView() == 0 ? null : this.getLayoutInflater().inflate(setContentView(), null));
        if (mView != null) {
            ly_content.addView(mView);
        }
        mHttpPresenter = new HttpPresenterImpl();
        mImageloader = ImageLoader.getInstance();
        if (!mImageloader.isInited()) {
            mImageloader.init(ImageLoaderConfiguration.createDefault(BaseActivity.this));

        }
        mFinalDb = BaseApplication.GetDbHelper();
        ButterKnife.bind(this);
        initView();


    }

    /**
     * 初始化布局
     */
    public abstract int setContentView();

    /**
     * 初始化控件
     */
    public abstract void initView();

    protected boolean isSupportSwipeBack() {
        return true;
    }


    /**
     * 初始化布局
     */
    public abstract void getRateList(List<Rate> rates, int fromType);


    private void initSlidingMenu(Bundle savedInstanceState) {
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);

        //这里借用了SlidingMenu的setBehindContentView方法来设置一个透明菜单
        View behindView = new View(this);
        behindView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        behindView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setBehindContentView(behindView);

        mSlidingMenu = getSlidingMenu();
        //设置阴影宽度为10个px
        mSlidingMenu.setShadowWidth(10);
        //设置阴影
        mSlidingMenu.setShadowDrawable(R.drawable.slide_shadow);
        //设置下面的布局，也就是我们上面定义的透明菜单离右边屏幕边缘的距离为0，也就是滑动开以后菜单会全屏幕显示
        mSlidingMenu.setBehindOffset(0);
        mSlidingMenu.setFadeDegree(0.35f);
        //菜单打开监听，因为菜单打开后我们要finish掉当前的Activity
        mSlidingMenu.setOnOpenedListener(this);

        //设置手势滑动方向，因为我们要实现微信那种右滑动的效果，这里设置成SlidingMenu.LEFT模式
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //因为微信是只有边缘滑动，我们设置成TOUCHMODE_MARGIN模式，如果你想要全屏幕滑动，只需要把这个改成TOUCHMODE_FULLSCREEN就OK了
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null)
            mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        if (mHelper != null) {

            return mHelper.findViewById(id);
        }
        return findViewById(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mHelper != null) {
            mHelper.onSaveInstanceState(outState);
        }
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        if (mHelper != null) {
            mHelper.registerAboveContentView(v, params);
        }
    }

    public void setBehindContentView(int id) {
        setBehindContentView(getLayoutInflater().inflate(id, null));
    }

    public void setBehindContentView(View v) {
        setBehindContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setBehindContentView(View v, ViewGroup.LayoutParams params) {
        mHelper.setBehindContentView(v, params);
    }

    public SlidingMenu getSlidingMenu() {
        return mHelper.getSlidingMenu();
    }

    public void toggle() {
        if (mHelper != null) {
            mHelper.toggle();
        }
    }

    public void showContent() {

        if (mHelper != null) {
            mHelper.showContent();
        }
    }

    public void showMenu() {
        if (mHelper != null) {
            mHelper.showMenu();
        }
    }

    public void showSecondaryMenu() {
        if (mHelper != null) {
            mHelper.showSecondaryMenu();
        }
    }

    public void setSlidingActionBarEnabled(boolean b) {
        if (mHelper != null) {
            mHelper.setSlidingActionBarEnabled(b);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mHelper != null) {
            boolean b = mHelper.onKeyUp(keyCode, event);
            if (b) return b;
        }
        return super.onKeyUp(keyCode, event);
    }

    //滑动完全打开菜单后结束掉当前的Activity
    @Override
    public void onOpened() {
        this.finish();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
    public void openActivity(Class<?> pClass, Bundle pBundle,int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent,requestCode);
    }



    @Override
    public void finish() {
        // 清除网络请求队列
        AsyncHttpNetCenter.getInstance().clearRequestQueue(this);
        super.finish();
    }

    @Override
    public void getError(int status, String error, String tag) {

    }

    @Override
    public void getResult(String result, String tag) {
        if (tag.equals(G.HttpGetTpye)) {
            HttpData hdata = new ParseEntity<HttpData>().parseOne(result, HttpData.class);
            TaskGetList task = new TaskGetList(null);
            task.execute(hdata.getRetData().toString());
        }

    }

    @Override
    public void showProgress(boolean flag, String message, DialogInterface.OnDismissListener onDismissListener) {

    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message, null);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "", null);
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void close() {
        finish();
    }


    public void getHttpInfo() {

        mHttpPresenter.GetInfo(this, new BaseRequest(),
                G.HttpGetTpye, HttpModelImpl.Get, G.HttpGetTpye);

    }

    public class TaskGetList extends AsyncTask<String, Integer, List<Rate>> {//继承AsyncTask
        String code = null;
        int type = 0;//0本地  1网络

        public TaskGetList(String c) {
            code = c;
            type = 0;//此方法是本地查询
        }


        @Override
        protected List<Rate> doInBackground(String... params) {//处理后台执行的任务，在后台线程执行

            List<Rate> listRate = new ArrayList<>();
            if (params.length != 0) {
                type = 1;
                String json = ParseEntity.getJsonStringFromAssets(BaseActivity.this, "jsons_rate.json");
                listRate = new ParseEntity<Rate>().parse(json, Rate[].class);
                for (Rate r : listRate) {

                    if (getRateByPath(r.getCode()) == null) {
                        addRateToCache(r);
                    }

                }

                List<String> strings = new ParseEntity<String>().parse(params[0].toString(), String[].class);
                for (String s : strings) {
                    Rate r = getRateByPath(s);
                    if (r != null) {
                        DBUtil.SaveRateEntity(mFinalDb, r);
                    }
                }
            }
            listRate = DBUtil.getRateListEntity(mFinalDb, code);

            if(code==null)
            {
                listRate.addAll(0,DBUtil.getRateUsualListEntity(mFinalDb));
            }



            return listRate;
        }

        protected void onProgressUpdate(Integer... progress) {//在调用publishProgress之后被调用，在ui线程执行

        }

        @Override
        protected void onPostExecute(List<Rate> rates) {
            super.onPostExecute(rates);
//            mDatas.clear();
//            mDatas.addAll(rates);
//            adapter.notifyDataSetChanged();
//            mRecyclerView.refreshComplete();
            getRateList(rates, type);

        }

        protected void onPreExecute() {//在 doInBackground(Params...)之前被调用，在ui线程执行

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    public void addRateToCache(Rate rate) {
        SoftReference<Rate> softRate = new SoftReference<Rate>(rate);
        mRateCache.put(rate.getCode().toUpperCase(), softRate);
    }


    public Rate getRateByPath(String path) {
        // 从缓存中取软引用的Bitmap对象
        SoftReference<Rate> softRate = mRateCache.get(path.toUpperCase());
        // 判断是否存在软引用
        if (softRate == null) {
            return null;
        }
        Rate rate = softRate.get();
        return rate;
    }



    public void getHttpGetCurrency(String fromCurrency,String toCurrency,String tag) {

        RequestCurrency mRequestCurrency=new RequestCurrency();
        mRequestCurrency.amount="1";
        mRequestCurrency.fromCurrency=fromCurrency;
        mRequestCurrency.toCurrency=toCurrency;
        mHttpPresenter.GetInfo(this,mRequestCurrency ,
                G.HttpGetCurrency, HttpModelImpl.Get,tag);

    }






}
