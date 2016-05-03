package net.sxkeji.blacksearch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.sxkeji.blacksearch.R;
import net.sxkeji.blacksearch.beans.ProvincesBean;

import java.util.ArrayList;

/**
 * 省份的RecyclerView Adapter
 * Created by zhangshixin on 4/11/2016.
 */
public class ProvinceRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ProvincesBean> mData;
    private OnItemClickListener mListener;
    private int  mSelectPos;

    public ProvinceRecyclerAdapter(Context context , int  defaultSelectPos) {
        mContext = context;
        mSelectPos = defaultSelectPos;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<ProvincesBean> data) {
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    public void changeSelecPos(int  selectPos){
        mSelectPos = selectPos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_selec_provice, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ProvincesBean provinceBean = mData.get(position);
            final TextView tvProvinceName = ((ViewHolder) holder).tvName;
            if (position == mSelectPos){
//                holder.setIsRecyclable(false);
                tvProvinceName.setBackgroundColor(Color.parseColor("#dedede"));
            }else {
                tvProvinceName.setBackgroundColor(Color.WHITE);
            }
            tvProvinceName.setText(provinceBean.getName());
            if (mListener != null) {
                tvProvinceName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClick(provinceBean, position);
                    }
                });
            }

        }

    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }

        public TextView getTvName() {
            return tvName;
        }

        public void setTvName(TextView tvName) {
            this.tvName = tvName;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ProvincesBean provinceBean, int position);
    }
}
