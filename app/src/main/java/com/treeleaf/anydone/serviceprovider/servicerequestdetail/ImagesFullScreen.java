package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.TouchImageView;

import java.util.ArrayList;
import java.util.Objects;

public class ImagesFullScreen extends DialogFragment {
    private static final String TAG = "ImagesFullScreen";
    private ArrayList<String> images;
    private ViewPager viewPager;
    private TextView lblCount;
    private int selectedPosition = 0;

    public static ImagesFullScreen newInstance() {
        return new ImagesFullScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_image_slider, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        lblCount = v.findViewById(R.id.lbl_count);

        assert getArguments() != null;
        images = getArguments().getStringArrayList("images");
        selectedPosition = getArguments().getInt("position");

        GlobalUtils.showLog(TAG, "position: " + selectedPosition);
        GlobalUtils.showLog(TAG, "images size: " + images.size());

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }


    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    displayMetaInfo(position);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            };

    @SuppressLint("SetTextI18n")
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        public MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(getActivity())
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.layout_image_fullscreen_preview,
                    container, false);

            TouchImageView imageViewPreview = view.findViewById(R.id.image_preview);

            String imageUrl = images.get(position);

            GlobalUtils.showLog(TAG, "imageUrl:  " + imageUrl);
            Glide.with(getActivity()).load(imageUrl)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

}

