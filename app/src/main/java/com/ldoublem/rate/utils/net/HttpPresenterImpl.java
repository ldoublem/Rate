package com.ldoublem.rate.utils.net;


import com.ldoublem.rate.baseActivity.BasePresenter;
import com.ldoublem.rate.baseActivity.BaseRequest;

/**
 * Created by Administrator on 2016/2/23.
 */
public class HttpPresenterImpl extends BasePresenter implements HttpPresenter {

    @Override
    public void GetInfo(final GetHttpResult mGetHttpResult, BaseRequest queryParameter, String url, int Type, final String tag) {

        HttpModel testModel = new HttpModelImpl(mGetHttpResult.getContext());
        testModel.start(queryParameter, url, new StringTransactionListener() {
            @Override
            public void onSuccess(String response) {

                mGetHttpResult.getResult(response, tag);
            }

            @Override
            public void onFailure(int errorCode) {
                super.onFailure(errorCode);
                mGetHttpResult.getError(errorCode, "error", tag);
            }
        }, Type);

    }


}
