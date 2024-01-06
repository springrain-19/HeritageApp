package com.rgzn.heritage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class Carousel {

    private Context mContext;
    private LinearLayout dotLinearLayout;
    private List<ImageView> mDotViewList = new ArrayList<>();

    private Handler autoScrollHandler;

    private List<Integer> originalImages = new ArrayList<>();

    private ViewPager2 viewPager2;

    private long AUTO_SCROLL_INTERVAL = 1_500;

    private boolean AUTO_SCROLL = false;


    public void setAUTO_SCROLL_INTERVAL(long AUTO_SCROLL_INTERVAL) {
        this.AUTO_SCROLL_INTERVAL = AUTO_SCROLL_INTERVAL;
    }

    public Carousel(Context mContext, LinearLayout dotLinearLayout, ViewPager2 viewPager2) {
        this.mContext = mContext;
        this.dotLinearLayout = dotLinearLayout;
        this.viewPager2 = viewPager2;

        autoScrollHandler = new Handler(Looper.getMainLooper());
    }


    public void initViews(int[] resource ) {

        //加载初始化绑定轮播图
        for (int id : resource) {
            originalImages.add(id);

            ImageView dotImageView = new ImageView(mContext);
            if (originalImages.size() == 1) {
                dotImageView.setImageResource(R.drawable.red_dot);
            } else {
                dotImageView.setImageResource(R.drawable.grey_dot);
            }

            //设置标志点的布局参数
            LinearLayout.LayoutParams dotImageLayoutParams = new LinearLayout.LayoutParams(60, 60);
            dotImageLayoutParams.setMargins(5, 0, 5, 0);

            //将布局参数绑定到标志点视图
            dotImageView.setLayoutParams(dotImageLayoutParams);

            //保存标志点便于后续动态修改
            mDotViewList.add(dotImageView);

            //将标志点的视图绑定在Layout中
            dotLinearLayout.addView(dotImageView);
        }

        originalImages.add(0, originalImages.get(originalImages.size() - 1));
        originalImages.add(originalImages.get(1));

        ImageAdapter adapter = new ImageAdapter(originalImages);
        viewPager2.setAdapter(adapter);

        viewPager2.setCurrentItem(1, false);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                // 当滑动开始时停止自动滚动
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    autoScrollHandler.removeCallbacks(autoScrollRunnable);
                }
                // 当滑动结束时重新启动自动滚动
                else if (state == ViewPager2.SCROLL_STATE_IDLE && AUTO_SCROLL) {
                    autoScrollHandler.removeCallbacks(autoScrollRunnable);
                    autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
                }
            }

            @Override
            public void onPageSelected(int position) {

                //动态设置图片的下标点位
                for (int i = 0; i < mDotViewList.size(); i++) {
                    if (i == position - 1) {
                        mDotViewList.get(i).setImageResource(R.drawable.red_dot);
                    } else {
                        mDotViewList.get(i).setImageResource(R.drawable.grey_dot);
                    }
                }
                // 在滑动到最后一个元素时，跳转到第一个元素
                if (position == originalImages.size() - 1) {
                    viewPager2.setCurrentItem(1, false);
                }
                // 在滑动到第一个元素时，跳转到最后一个元素
                else if (position == 0) {
                    viewPager2.setCurrentItem(originalImages.size() - 2, false);
                }
            }
        });

    }


    public void startAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable); // 移除之前的回调,防止多次启动的情况
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
        AUTO_SCROLL = true;
    }

    public void stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        AUTO_SCROLL = false;
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();

            if (currentItem == originalImages.size() - 2) {
                viewPager2.setCurrentItem(1);
            } else {
                viewPager2.setCurrentItem(currentItem + 1);
            }

            autoScrollHandler.postDelayed(this, AUTO_SCROLL_INTERVAL);
        }
    };
}
