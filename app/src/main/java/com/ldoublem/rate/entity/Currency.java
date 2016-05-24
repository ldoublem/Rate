package com.ldoublem.rate.entity;

/**
 * Created by lumingmin on 16/5/23.
 */
public class Currency {

    private int errNum;
    private String errMsg;
    private String fromCurrency;
    private Currency retData;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

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

    public Currency getRetData() {
        return retData;
    }

    public void setRetData(Currency retData) {
        this.retData = retData;
    }

    public String toCurrency;
    public float currency;

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public float getCurrency() {
        return currency;
    }

    public void setCurrency(float currency) {
        this.currency = currency;
    }
}
