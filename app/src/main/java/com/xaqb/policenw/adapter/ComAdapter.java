package com.xaqb.policenw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.policenw.entity.Company;

import java.util.List;
import com.xaqb.policenw.R;


/**
 * Created by lenovo on 2017/3/15.
 */

public class ComAdapter extends BaseAdapter{

    private Context mContext;
    private List<Company> mCompany;
    public ComAdapter(Context context, List<Company> company) {
        mContext = context;
        mCompany = company;
    }

    @Override
    public int getCount() {
        return mCompany.size();
    }

    @Override
    public Object getItem(int i) {
        return mCompany.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    MyViewHolder holder;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            holder = new MyViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.com_list,null);
            holder.tvCom = (TextView) view.findViewById(R.id.tv_com_com_list);
            holder.tvPer = (TextView) view.findViewById(R.id.tv_per_com_list);
            holder.tvNum = (TextView) view.findViewById(R.id.tv_num_com_list);
            holder.tvComs = (TextView) view.findViewById(R.id.tv_coms_com_list);
            holder.tvPlice = (TextView) view.findViewById(R.id.tv_ide_per_list);
            view.setTag(holder);
        }else{
            holder = (MyViewHolder) view.getTag();
        }
        holder.tvCom.setText(mCompany.get(i).getCom());
        holder.tvPer.setText(mCompany.get(i).getPer());
        holder.tvNum.setText(mCompany.get(i).getNum());
        holder.tvComs.setText(mCompany.get(i).getComs());
        holder.tvPlice.setText(mCompany.get(i).getAddress());
        return view;
    }
}

class MyViewHolder {
    TextView tvCom;
    TextView tvPer;
    TextView tvNum;
    TextView tvComs;
    TextView tvPlice;
}
