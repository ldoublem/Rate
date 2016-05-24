package com.ldoublem.rate.entity;

import java.io.Serializable;

/**
 * Created by lumingmin on 16/5/23.
 */
public class HttpData implements Serializable{

    private int errNum;
    private String errMsg;

    private Object retData;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
