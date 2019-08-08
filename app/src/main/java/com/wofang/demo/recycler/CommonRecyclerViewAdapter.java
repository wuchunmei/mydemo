package com.wofang.demo.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wofang.demo.R;
import com.wofang.demo.utils.ClickUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcm on 19/5/15.
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder>{
    protected Context mContext;
    private LayoutInflater mInflater;
    private List<T> mDatas = new ArrayList<>();
    private static final int NORMAL_TYPW = 0, FOOTER_TYPE = 1;
    private Footer footer;

    public CommonRecyclerViewAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public CommonRecyclerViewAdapter(Context context, List<T> datas,Footer footer){
        this(context);
        setDatas(datas);
        this.footer = footer;
    }

    public void clearDatas(){
        this.mDatas.clear();
    }
    
    public void setDatas(List<T> datas){
        this.mDatas = datas;
    }
    
    public void appendDatas(List<T> datas) {
        if(null == datas) {
            return;
        }
        int size = mDatas.size();
        this.mDatas.addAll(datas);
        notifyItemRangeInserted(size, datas.size());
    }
    
    public void removeItem(int postion) {
        if(null != mDatas && postion < mDatas.size()) {
            mDatas.remove(postion);
            notifyItemRemoved(postion);
        }
    }

    public void movedItem(int form, int to) {
        if(null != mDatas && !mDatas.isEmpty()) {
            if (form < 0 || form >= getItemCount() || to < 0 || to >= getItemCount()) {
                return;
            }
            T item = mDatas.get(form);
            mDatas.remove(form);
            mDatas.add(to-1, item);
            notifyItemMoved(form, to);
        }
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CommonRecyclerViewHolder holder = null;
        switch (viewType){
            case NORMAL_TYPW:
                holder = CommonRecyclerViewHolder.get(this.mContext, viewGroup, getItemLayoutId(viewType));
                break;
            case FOOTER_TYPE:
                holder = CommonRecyclerViewHolder.get(this.mContext,viewGroup, R.layout.layout_footer);
                break;
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(final CommonRecyclerViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case NORMAL_TYPW:
                onBindItemHolder(holder, getItem(position), position);
                break;
            case FOOTER_TYPE:
                onBindFooterHolder(holder,footer);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDatas.size()){
            return NORMAL_TYPW;
        }else {
            return FOOTER_TYPE;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return null != this.mDatas ? this.mDatas.size()+(footer == null ? 0 : 1) : 0;
    }

    public T getItem(int position) {
        return (null != mDatas && position < mDatas.size()) ? mDatas.get(position) : null;
    }

    // 刷新列表数据
    public void reflushList(List<T> list) {
        this.mDatas = list;
        this.notifyDataSetChanged();
    }

    // 刷新底部
    public void reflushFooterData(Footer footerData) {
        this.footer = footerData;
        this.notifyItemChanged(getItemCount() - 1);
    }

    public abstract int getItemLayoutId(int viewType);

    public abstract int getFooterLayout(int viewType);

    public abstract void onBindItemHolder(final CommonRecyclerViewHolder helper, T item, int position);

    private void onBindFooterHolder(final CommonRecyclerViewHolder helper,Footer footer){
        CommonViewHolder viewHolder = helper.getHolder();
        LinearLayout layout = viewHolder.getView(R.id.load_layout);
        ProgressBar progressBar = viewHolder.getView(R.id.footer_progressbar);
        if (footer != null) {
            if (footer.isShowFooter()) {
                layout.setVisibility(View.VISIBLE);
                if (footer.isShowProgressBar()){
                    progressBar.setVisibility(View.VISIBLE);
                } else{
                    progressBar.setVisibility(View.GONE);
                }
                viewHolder.setText(R.id.footer_tip, footer.getTitle());
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 防止连续点击
                        if (ClickUtil.isFastClick()) {
                            return;
                        }
                        Toast.makeText(mContext, "发送通讯", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                layout.setVisibility(View.GONE);
            }

        }
    }
}
