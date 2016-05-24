package com.ldoublem.rate.entity;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by lumingmin on 16/5/22.
 */
@Table(name = "Rate")
public class Rate implements Serializable {

    @Id(column = "id")
    private int id;
    private String code;
    private String name;
    private float rate;
    private String firstName;

    private int isSelect=0;//0  表示不是第一次首页显示的 1反之

    private int isFromCurrency=0;//1 待转化币种的简称，这里为人民币
    private int isUsual=0;// 添加栏目里面常用字段

    public int getIsUsual() {
        return isUsual;
    }

    public void setIsUsual(int isUsual) {
        this.isUsual = isUsual;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getIsFromCurrency() {
        return isFromCurrency;
    }

    public void setIsFromCurrency(int isFromCurrency) {
        this.isFromCurrency = isFromCurrency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
