package com.xaqb.policenw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xaqb.policenw.R;
import com.xaqb.policenw.entity.Coms;

import java.util.List;

/**
 * Created by lenovo on 2017/3/22.
 */

public class ComsAdapter extends BaseAdapter {
    private Context context;
    private List<Coms> coms;
    public ComsAdapter(Context context, List<Coms> coms) {
        this.context = context;
        this.coms = coms;
    }

    @Override
    public int getCount() {
        return coms.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
     ViewHolders holders;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null==view){
            holders = new ViewHolders();
            view = LayoutInflater.from(context).inflate(R.layout.list_coms,null);
            holders.txt = (TextView) view.findViewById(R.id.textView_list_coms);
            holders.txtcode = (TextView) view.findViewById(R.id.textView_list_comscode);
            view.setTag(holders);
        }else {
            holders = (ViewHolders) view.getTag();
        }
        holders.txt.setText(coms.get(i).getBcname());
        holders.txtcode.setText(coms.get(i).getBccode());
        return view;
    }
}

class ViewHolders{
    TextView txt;
    TextView txtcode;
}