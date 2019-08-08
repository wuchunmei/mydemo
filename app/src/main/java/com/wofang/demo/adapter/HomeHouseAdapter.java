package com.wofang.demo.adapter;

import android.content.Context;
import android.widget.ImageView;


import com.wofang.demo.R;
import com.wofang.demo.recycler.CommonRecyclerViewAdapter;
import com.wofang.demo.recycler.CommonRecyclerViewHolder;
import com.wofang.demo.recycler.CommonViewHolder;
import com.wofang.demo.bean.HouseListResult;
import com.wofang.demo.recycler.Footer;
import com.wofang.demo.utils.GlideUtils;

import java.util.List;

public class HomeHouseAdapter extends CommonRecyclerViewAdapter<HouseListResult.ListBean> {
    private Context mContext;

    public HomeHouseAdapter(Context context, List<HouseListResult.ListBean> datas, Footer footer) {
        super(context, datas,footer);
        mContext = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.home_list_item_layout;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.layout_footer;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, HouseListResult.ListBean item, int position) {
        if(null != item){
            CommonViewHolder viewHolder = helper.getHolder();
            if(null != viewHolder){
                viewHolder.setText(R.id.name_tv,item.getName());
                viewHolder.setText(R.id.des,item.getIntroduction());
                ImageView imageView = viewHolder.getView(R.id.img);
//                GlideUtils.disPlayerImageView(mContext,imageView,"http://static.wodujia.com/"+item.getProductImage(),R.mipmap.icon_img_default);
            }
        }
    }

}
