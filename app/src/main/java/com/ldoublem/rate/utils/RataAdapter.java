package com.ldoublem.rate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ldoublem.rate.R;
import com.ldoublem.rate.entity.Rate;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lumingmin on 16/5/24.
 */
public class RataAdapter extends BaseAdapter {

    long amount = 0;
    public List<Rate> list = new ArrayList<>();
    private Context mContext;

    public List<Rate> getList() {
        return list;
    }

//    public void setAmount(long amount,) {
//        this.amount = amount;
//        notifyDataSetChanged();
//    }


    public void setList(List<Rate> l,long amount) {
        list.clear();
        list.addAll(l);
        this.amount = amount;
        notifyDataSetChanged();
    }


    public RataAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_rate_item, null);
            viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
            viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.riv_country = (RoundedImageView) view.findViewById(R.id.riv_country);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Rate r = (Rate) getItem(position);

        viewHolder.tv_code.setText(r.getCode());
        viewHolder.tv_name.setText(r.getName());
        float n = (r.getRate() * amount);

        viewHolder.tv_number.setText(String.valueOf(n));
        String imageUri = "flag/" + r.getCode().toLowerCase() + ".png";

        if (viewHolder.riv_country.getTag() == null || !viewHolder.riv_country.getTag().equals(imageUri)) {
            viewHolder.riv_country.setImageBitmap(BitmapUtil.getImageFromAssetsFile(mContext, imageUri));
            viewHolder.riv_country.setTag(imageUri);
        }

        return view;
    }


    final static class ViewHolder {

        TextView tv_code, tv_number, tv_name;
        RoundedImageView riv_country;
    }


}
