package net.sxkeji.blacksearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import net.sxkeji.blacksearch.R;
import net.sxkeji.blacksearch.beans.ProvincesBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 城市的RecyclerView Adapter
 * Created by zhangshixin on 4/11/2016.
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ProvincesBean.CitysEntity> mData;
    private OnItemClickListener mListener;
    private HashMap<Integer, Boolean> mSelectPos;

    public CityRecyclerAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<ProvincesBean.CitysEntity> data) {
        mData = data;
        mSelectPos = new HashMap<>();
        for (int i = 0; i < getItemCount(); i++) {
            mSelectPos.put(i, false);
        }
        notifyDataSetChanged();
    }

    public void setData(ArrayList<ProvincesBean.CitysEntity> data, HashMap<Integer, Boolean> selectPos) {
        mData = data;
        mSelectPos = selectPos;
        notifyDataSetChanged();
    }


    public void setSelectPos(int position) {
        if (mSelectPos != null && mSelectPos.size() >= position) {
            Boolean isSelect = mSelectPos.get(position);
            if (isSelect == null || isSelect) {
                mSelectPos.put(position, false);
            } else {
                mSelectPos.put(position, true);
            }
            notifyDataSetChanged();
        }
    }
    public boolean getSelectPos(int position){
        if (position <= getItemCount()) {
            return mSelectPos.get(position);
        }else {
            return false;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_selec_city, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ProvincesBean.CitysEntity cityBean = mData.get(position);
            Boolean isSelect = mSelectPos.get(position);
            ((ViewHolder) holder).tvContent.setText(cityBean.getName());
            if (mListener != null) {
                ((ViewHolder) holder).tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClick(cityBean, position);
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ProvincesBean.CitysEntity cityBean, int position);
    }
}
