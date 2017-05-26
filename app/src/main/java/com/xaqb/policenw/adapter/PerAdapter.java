package com.xaqb.policenw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.policenw.R;
import com.xaqb.policenw.entity.Person;

import java.util.List;

/**
 * Created by lenovo on 2017/3/15.
 */

public class PerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Person> mPeople;
    public PerAdapter(Context context, List<Person> people) {
        mContext = context;
        mPeople = people;
    }

    @Override
    public int getCount() {
        return mPeople.size();
    }

    @Override
    public Object getItem(int i) {
        return mPeople.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    ViewHolder holder;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.per_list,null);

            holder.tvName = (TextView) view.findViewById(R.id.tv_name_per_list);
            holder.tvSix = (TextView) view.findViewById(R.id.tv_six_per_list);
            holder.tvNum = (TextView) view.findViewById(R.id.tv_num_per_list);
            holder.tvIde = (TextView) view.findViewById(R.id.tv_ide_per_list);
            holder.tvCom = (TextView) view.findViewById(R.id.tv_com_per_list);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(mPeople.get(i).getName());
        holder.tvSix.setText(mPeople.get(i).getSix());
        holder.tvNum.setText(mPeople.get(i).getNum());
        holder.tvIde.setText(mPeople.get(i).getIde());
        holder.tvCom.setText(mPeople.get(i).getCom());
        return view;
    }
}

class ViewHolder {
    TextView tvName;
    TextView tvSix;
    TextView tvNum;
    TextView tvIde;
    TextView tvCom;
}
