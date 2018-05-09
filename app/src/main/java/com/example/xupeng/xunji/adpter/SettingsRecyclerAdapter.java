package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.imp.OnRecyclerSettingsSwitchClickListener;

import java.util.List;

/**
 * Created by xupeng on 2018/3/12.
 */

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> dataList;

    private OnRecyclerSettingsSwitchClickListener itemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView settingTitle;
        Switch settingSwitch;
        public ViewHolder(View view){
            super(view);
            settingTitle=(TextView)view.findViewById(R.id.setting_tv);
            settingSwitch=(Switch)view.findViewById(R.id.setting_switch);
        }
    }

    public SettingsRecyclerAdapter(Context context, List<String> list){
        this.context=context;
        dataList=list;
    }

    public void setOnItemClickListener(OnRecyclerSettingsSwitchClickListener listener){
        itemClickListener=listener;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.settingTitle.setText(dataList.get(i));
        viewHolder.settingSwitch.setChecked(false);

        if(itemClickListener!=null) {
            viewHolder.settingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemClickListener.onSwitchClickListener(isChecked);
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.settings_rv_item,
                viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
