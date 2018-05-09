package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.PoiKeywordSearchActivity;
import com.example.xupeng.xunji.bean.PoiAddressItem;

import java.util.List;

public class PoiKeywordSearchAdapter extends RecyclerView.Adapter<PoiKeywordSearchAdapter.ViewHolder> {

    private List<PoiAddressItem> poiAddressItemList;
    private Context mContext;

    public PoiKeywordSearchAdapter(Context context, List<PoiAddressItem> poiAddressItemList) {
        this.poiAddressItemList = poiAddressItemList;
        this.mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView address;
        TextView detailAddress;
        LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.poi_item_address);
            detailAddress = (TextView) itemView.findViewById(R.id.poi_item_address_detail);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.poi_item_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PoiAddressItem item = poiAddressItemList.get(position);
        holder.address.setText(item.getDetailAddress());
        holder.detailAddress.setText(item.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PoiKeywordSearchActivity) mContext).setDetailAddress(item.getDetailAddress());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poi_search_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return poiAddressItemList.size();
    }
}
