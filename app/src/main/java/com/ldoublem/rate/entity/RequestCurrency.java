package com.ldoublem.rate.entity;


import com.ldoublem.rate.baseActivity.BaseRequest;

/**
 * 用户登录请求实体类
 */
public class RequestCurrency extends BaseRequest {
    public String fromCurrency;
    public String toCurrency;
    public String amount;
}
