package com.wofang.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wofang.demo.mydemo.R;
import com.wofang.demo.utils.CommonUtil;
import com.wofang.demo.utils.GlideUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 自定义轮播图
 * @author wcm
 * @date 2019/05/16
 */
public class ADBannerView extends RelativeLayout {

    /**
     * 圆点指示器的位置，文字在左，圆点在右
     */
    public static final int INDEX_POSITION_RIGHT = 0x100;
    /**
     * 圆点指示器的位置，文字在上，圆点在下
     */
    public static final int INDEX_POSITION_BOTTOM = 0x101;

    private static final int HANDLE_UPDATE_INDEX = 0;

    private Context mContext;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private OnItemClickListener mOnItemClickListener;
    //装载ImageView控件的list
    private List<ImageView> ivList;
    //圆点指示器控件
    private List<View> vList;
    //控制圆点View的形状，未选中的颜色
    private GradientDrawable gradientDrawable;
    //控制圆点View的形状，选中的颜色
    private GradientDrawable gradientDrawableSelected;
    //选中圆点的颜色值，默认为#FF3333
    private int indexColorResId;
    //图片对应的文字描述
    private TextView tvText;
    //自动滑动的定时器
    private ScheduledExecutorService scheduledExecutorService;
    //当前加载到第几页
    private int currentIndex = 0;
    //默认背景图
    private int defaultImageResId;
    private Drawable defaultImageDrawable = null;
    //自动轮播的时间间隔(s)
    private int intervalTime = 5;
    //轮播图需要的数据列表
    private List<String> mDataList;
    //圆点指示器的位置，提供两种布局
    private int indexPosition = INDEX_POSITION_RIGHT;

    public ADBannerView(Context context) {
        this(context, null);
    }

    public ADBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.adbanner_layout, this, true);
        mViewPager = (ViewPager) findViewById(R.id.vp_banner);
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ADBannerView, defStyle, 0);
        if (a != null) {
            defaultImageDrawable = a.getDrawable(R.styleable.ADBannerView_defaultImageDrawable);
            intervalTime = a.getInteger(R.styleable.ADBannerView_intervalTime, 5);
            indexPosition = a.getInteger(R.styleable.ADBannerView_indexPosition, INDEX_POSITION_RIGHT);
            ColorStateList indexColorList = a.getColorStateList(R.styleable.ADBannerView_indexColor);
            if (indexColorList != null) {
                indexColorResId = indexColorList.getColorForState(getDrawableState(), 0);
            }
            a.recycle();
        }
    }

    /**
     * 设置图片加载之前默认显示的图片
     *
     * @param defaultImageResId
     * @return BannerM
     */
    public ADBannerView setDefaultImageResId(int defaultImageResId) {
        this.defaultImageResId = defaultImageResId;
        return this;
    }

    /**
     * 设置图片加载之前默认显示的图片
     *
     * @param defaultImageDrawable
     * @return BannerM
     */
    public ADBannerView setDefaultImageDrawable(Drawable defaultImageDrawable) {
        this.defaultImageDrawable = defaultImageDrawable;
        return this;
    }

    /**
     * 设置轮播的时间间隔，单位为s，默认为5s
     *
     * @param intervalTime
     * @return BannerM
     */
    public ADBannerView setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
        return this;
    }

    /**
     * 设置圆点指示器的位置
     * #BannerM.INDEX_POSITION_RIGHT or #BannerM.INDEX_POSITION_BOTTOM
     *
     * @param indexPosition
     * @return BannerM
     */
    public ADBannerView setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
        return this;
    }

    /**
     * 选中圆点的颜色值，默认为#FF3333
     *
     * @param indexColor
     * @return BannerM
     */
    public ADBannerView setIndexColor(int indexColor) {
        this.indexColorResId = indexColor;
        return this;
    }

    /**
     * 设置轮播图需要的数据列表
     *
     * @param urls
     * @return BannerM
     */
    public ADBannerView setBannerBeanList(List<String> urls) {
        this.mDataList = urls;
        return this;
    }

    /**
     * 设置图片的点击事件
     * @param listener
     */
    public ADBannerView setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public void show() {
        if (mDataList == null || mDataList.size() == 0) {
            throw new NullPointerException("bannerBeanList == null");
        }
        initImageViewList();
        initIndexList();
        mViewPager.addOnPageChangeListener(new OnPageChangeListener());
        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        //定时器开始工作
        startPlay();
    }

    /**
     * 初始化圆点指示器，根据indexPosition来加载不同的布局
     */
    private void initIndexList() {
        int count = mDataList.size();
        vList = new ArrayList<>(count);
        LinearLayout llIndex;
        if (indexPosition == INDEX_POSITION_RIGHT) {
            ViewStub vsIndexRight = (ViewStub) findViewById(R.id.vs_index_right);
            vsIndexRight.inflate();
            llIndex = (LinearLayout) findViewById(R.id.ll_index_right);
            tvText = (TextView) findViewById(R.id.tv_text);
        } else {
            ViewStub vsIndexBottom = (ViewStub) findViewById(R.id.vs_index_bottom);
            vsIndexBottom.inflate();
            llIndex = (LinearLayout) findViewById(R.id.ll_index_bottom);
            tvText = (TextView) findViewById(R.id.tv_text);
        }
        //默认第一张图片的文字描述
//        tvText.setText(mDataList.get(0).getText());
        //使用GradientDrawable构造圆形控件
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(mContext.getResources().getColor(R.color.common_33000000));
        gradientDrawableSelected = new GradientDrawable();
        gradientDrawableSelected.setShape(GradientDrawable.OVAL);
        if (indexColorResId != 0) {
            gradientDrawableSelected.setColor(indexColorResId);
        } else {
            gradientDrawableSelected.setColor(mContext.getResources().getColor(R.color.common_ff6600));
        }

        for (int i = 0; i < count; i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(CommonUtil.dp2px(mContext, 8), CommonUtil.dp2px(mContext, 8));
            lpView.rightMargin = CommonUtil.dp2px(mContext, 4);
            view.setLayoutParams(lpView);
            if (0 == i) {
                view.setBackgroundDrawable(gradientDrawableSelected);
            } else {
                view.setBackgroundDrawable(gradientDrawable);
            }
            view.bringToFront();
            vList.add(view);
            llIndex.addView(view);
        }
    }

    /**
     * 初始化ImageView，使用Picasso下载图片，只在SdCard中缓存
     */
    private void initImageViewList() {
        int count = mDataList.size();
        ivList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivList.add(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getPosition(imageView));
                }
            });
            if (defaultImageResId != 0) {
                GlideUtils.disPlayerImageView(mContext,imageView,mDataList.get(i),R.mipmap.icon_img_default);
            } else if (defaultImageDrawable != null) {
                GlideUtils.disPlayerImageView(mContext,imageView,mDataList.get(i),defaultImageDrawable);
            } else {
                Glide.with(mContext).load(mDataList.get(i)).into(imageView);
            }
        }
    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                currentIndex++;
                handler.obtainMessage(HANDLE_UPDATE_INDEX).sendToTarget();
            }
        }, intervalTime, intervalTime, TimeUnit.SECONDS);
    }

    /**
     * 获取点击图片的位置
     * @param item
     * @return int
     */
    private int getPosition(ImageView item) {
        for (int i = 0; i < ivList.size(); i++) {
            if (item == ivList.get(i)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_UPDATE_INDEX:
                    mViewPager.setCurrentItem(currentIndex);
                    break;
                default:
                    break;
            }
        }
    };

    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentIndex = position;
            for (int i = 0; i < mDataList.size(); i++) {
                if (position % ivList.size() == i) {
                    vList.get(i).setBackgroundDrawable(gradientDrawableSelected);
                } else {
                    vList.get(i).setBackgroundDrawable(gradientDrawable);
                }
//                tvText.setText(mDataList.get(position % ivList.size()).getText());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(View container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(View container, int position) {
            position %= ivList.size();
            if (position<0){
                position = ivList.size()+position;
            }
            ImageView imageView = ivList.get(position);
            ViewParent vp =imageView.getParent();
            if (vp!=null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(imageView);
            }
            ((ViewPager) container).addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}