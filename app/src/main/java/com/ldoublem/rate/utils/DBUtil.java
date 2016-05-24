package com.ldoublem.rate.utils;

import com.ldoublem.rate.entity.Rate;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lumingmin on 16/5/23.
 */
public class DBUtil {


    public static void SaveRateEntity(FinalDb finalDb, Rate r) {

        if (getRateEntity(finalDb, r) == null) {
            finalDb.save(r);
        }

    }

    public static void UpdateRateEntity(FinalDb finalDb, float currency, String code) {


        List<Rate> Rates = finalDb.findAllByWhere(Rate.class, "code = '" + code
                + "'");
        if (Rates != null && Rates.size() > 0) {
            Rate r = Rates.get(0);
            r.setRate(currency);
            System.out.println("------------开始保存货币" + code + "----汇率：" + r.getRate());
            finalDb.update(r, "code = '" + code
                    + "'");
            Rates = finalDb.findAllByWhere(Rate.class, "code = '" + code
                    + "'");
            System.out.println("------------结束保存货币" + Rates.get(0).getCode() + "----汇率：" + Rates.get(0).getRate());


        }


    }


    public static Rate getRateEntity(FinalDb finalDb, Rate r) {
        List<Rate> Rates = finalDb.findAllByWhere(Rate.class, "code = '" + r.getCode()
                + "'");
        if (Rates != null && Rates.size() > 0) {
            return Rates.get(0);
        }
        return null;
    }

    public static List<Rate> getRateListEntity(FinalDb finalDb, String code) {
        List<Rate> Rates = new ArrayList<>();
        if (code == null) {
            Rates = finalDb.findAll(Rate.class);
        } else {
            Rates = finalDb.findAllByWhere(Rate.class, "code like '%" + code + "%' or name like '%" + code + "%'");

        }

        return Rates;
    }


    public static List<Rate> getRateUsualListEntity(FinalDb finalDb) {
        List<Rate> Rates = new ArrayList<>();
        Rates = finalDb.findAllByWhere(Rate.class, "isUsual ='1'");
        return Rates;
    }

    public static Rate getRateFromCurrencyEntity(FinalDb finalDb) {
        List<Rate> Rates = new ArrayList<>();
        Rates = finalDb.findAllByWhere(Rate.class, "isFromCurrency ='1'");

        if (Rates == null || Rates.size() == 0) {
            return null;
        }

        return Rates.get(0);
    }

    public static List<Rate> getRateSelectListEntity(FinalDb finalDb) {
        List<Rate> Rates = new ArrayList<>();
        Rates = finalDb.findAllByWhere(Rate.class, "isSelect ='1' and isFromCurrency = '0'");
        return Rates;
    }

    public static List<Rate> getRateSelectAllListEntity(FinalDb finalDb) {
        List<Rate> Rates = new ArrayList<>();
        Rates = finalDb.findAllByWhere(Rate.class, "isSelect ='1'");
        return Rates;
    }


    public static void UpdateRateisFromCurrencyEntity(FinalDb finalDb, Rate newR, Rate oldR) {

        newR.setIsFromCurrency(1);
        oldR.setIsFromCurrency(0);
        finalDb.update(newR, "code = '" + newR.getCode()
                + "'");
        finalDb.update(oldR, "code = '" + oldR.getCode()
                + "'");

    }


    public static void UpdateRateisSelectEntity(FinalDb finalDb, Rate r, boolean select) {

        r = getRateEntity(finalDb, r);
        if (r != null) {
            if (select) {
                r.setIsSelect(1);
            } else {
                r.setIsSelect(0);

            }
            finalDb.update(r, "code = '" + r.getCode()
                    + "'");
        }

    }


}
