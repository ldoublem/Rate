package com.ldoublem.rate.baseActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

public abstract class AdapterBase<T> extends BaseAdapter {

	public final List<T> mList = new LinkedList<T>();
	public Context mContext;
	public LayoutInflater mInflater;

	public AdapterBase() {
	}

	public AdapterBase(Context baseContext) {
		this.mContext = baseContext;
		mInflater = LayoutInflater.from(baseContext);
	}

	public List<T> getList() {
		return mList;
	}

	public void setList(List<T> list)
	{
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToTopList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(0, list);
		notifyDataSetChanged();
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position > mList.size() - 1) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getExView(position, convertView, parent);
	}

	protected abstract View getExView(int position, View convertView,
			ViewGroup parent);

}
