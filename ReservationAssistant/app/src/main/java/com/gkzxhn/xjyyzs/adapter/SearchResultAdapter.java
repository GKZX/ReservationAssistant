package com.gkzxhn.xjyyzs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;
import com.gkzxhn.xjyyzs.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:搜索结果列表适配器
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private final String TAG = "SearchResultAdapter";
    private Context context;
    private List<ApplyResult.AppliesBean> list;

    public SearchResultAdapter(Context context, List<ApplyResult.AppliesBean> beanList){
        this.context = context;
        this.list = beanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_result_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_id_card_number.setText(list.get(position).getUuid());
        holder.tv_apply_date.setText(list.get(position).getApply().getApplyDate());
        holder.tv_apply_status.setText(list.get(position).getApply().getFeedback().getIsPass());
        Log.i(TAG, list.get(position).getApply().getFeedback().getIsPass() + "---");
        holder.tv_meeting_date.setText(list.get(position).getApply().getFeedback().getMeetingTime());
        holder.tv_apply_name.setText(list.get(position).getName());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showShortToast(context, "别点啦...");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_view) CardView card_view;
        @BindView(R.id.tv_apply_name) TextView tv_apply_name;
        @BindView(R.id.tv_id_card_number) TextView tv_id_card_number;
        @BindView(R.id.tv_apply_date) TextView tv_apply_date;
        @BindView(R.id.tv_apply_status) TextView tv_apply_status;
        @BindView(R.id.tv_meeting_date) TextView tv_meeting_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
