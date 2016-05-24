package com.ldoublem.rate.utils.net;

import android.content.Context;

import com.ldoublem.rate.baseActivity.BaseModel;
import com.ldoublem.rate.baseActivity.BaseRequest;
import com.ldoublem.rate.baseActivity.G;


/**
 * Created by Administrator on 2016/2/23.
 */
public class HttpModelImpl extends BaseModel implements HttpModel {

    public static final int Post = 2;
    public static final int Get = 1;

    public HttpModelImpl(Context context) {
        super(context);
    }

    @Override
    public void start(BaseRequest queryParameter, String url, StringTransactionListener transactionListener, int Type) {
        setHeader("apikey", G.Apikey);
        if (Type == 1) {
            get(getContext(), url, queryParameter, transactionListener);
        } else {
            post(getContext(), url, queryParameter, transactionListener);
        }
    }
}
