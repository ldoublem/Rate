package com.ldoublem.rate.utils.net;


import com.ldoublem.rate.baseActivity.IBaseView;

/**
 * Created by lumingmin on 16/4/20.
 */
public interface GetHttpResult extends IBaseView {


    /**
     * 获取网络请求结果
     *
     * @param result
     */
    void getResult(String result,String tag);

    void getError(int status, String error,String tag);

}
