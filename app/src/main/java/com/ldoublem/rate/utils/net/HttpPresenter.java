package com.ldoublem.rate.utils.net;

import com.ldoublem.rate.baseActivity.BaseRequest;

/**
 * Created by Administrator on 2016/2/23.
 */
public interface HttpPresenter {

    void GetInfo(GetHttpResult mGetHttpResult, BaseRequest queryParameter, String url, int Type,String tag);
}
