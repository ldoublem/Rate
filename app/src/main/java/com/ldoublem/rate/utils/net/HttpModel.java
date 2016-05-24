package com.ldoublem.rate.utils.net;


import com.ldoublem.rate.baseActivity.BaseRequest;

/**
 * Created by Administrator on 2016/2/23.
 */
public interface HttpModel {
    /**
     *
     * @param queryParameter
     * @param url
     * @param transactionListener
     * @param Type post and get
     */
    void start(BaseRequest queryParameter, String url, StringTransactionListener transactionListener, int Type);

}
