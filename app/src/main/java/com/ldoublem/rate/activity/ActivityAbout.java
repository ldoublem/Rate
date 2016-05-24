package com.ldoublem.rate.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.ldoublem.rate.R;
import com.ldoublem.rate.baseActivity.BaseActivity;
import com.ldoublem.rate.entity.Rate;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lumingmin on 16/5/24.
 */
public class ActivityAbout extends BaseActivity {


    @OnClick(R.id.ly_weibo)
    void weibo() {

        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/u/1791039083"));
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        getContext().startActivity(it);

    }

    @Bind(R.id.ly_weixin)
    LinearLayout ly_weixin;

    @OnClick(R.id.ly_weixin)
    void weixin() {



        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("hello");
        builder.setMessage("我的微信号:ldoublem，点击复制");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        // 将文本内容放到系统剪贴板里。
                        cm.setText("ldoublem");
            }
        });
        builder.show();

    }

    @Override
    public int setContentView() {
        return R.layout.layout_activity_about;
    }

    @Override
    public void initView() {
        initBar();
    }

    @Override
    public void getRateList(List<Rate> rates, int fromType) {

    }

    private void initBar() {
        toolbar.setTitle(R.string.action_about);
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
}
