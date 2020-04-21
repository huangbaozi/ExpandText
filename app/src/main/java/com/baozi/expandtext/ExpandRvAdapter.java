package com.baozi.expandtext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandRvAdapter extends RecyclerView.Adapter<ExpandRvAdapter.ExpandViewHolder> {
    private ArrayList<ExpandBean> list = new ArrayList<>();
    private int width;

    public ExpandRvAdapter(Context context){
        width=WindowUtils.getScreenWidth(context);
    }

    public void setList(ArrayList<ExpandBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expand_rv_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ExpandViewHolder holder, final int position) {
        ExpandBean bean = list.get(position);
        if (bean != null) {
            holder.titleTv.setTag(bean.getId());
            holder.titleTv.setText(String.valueOf(bean.getId()));
            holder.contentTv.setShowWidth(width);
            holder.contentTv.setOpen(bean.isOpen());
            holder.contentTv.setOnExpandCallback(new ExpandText.OnExpandCallback() {
                @Override
                public void expandClick(boolean isOpen) {
                    list.get(position).setOpen(isOpen);
                }
            });
            holder.contentTv.setContent(bean.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExpandViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.content_tv)
        ExpandText contentTv;
        public ExpandViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
