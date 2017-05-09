package com.project.june.thought.adapter.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.june.thought.activity.detail.ReadingPageActivity;
import com.project.june.thought.model.ReadingBannerListVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 2017/4/7.
 */

public class ReadingPagerAdapter extends PagerAdapter {

    private List<ReadingBannerListVo.DataBean> images = new ArrayList<>(0);

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(container.getContext()).load(images.get(position).getCover()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadingPageActivity.startThis(container.getContext(), images.get(position).getId(), images.get(position).getTitle(), images.get(position).getBottom_text(), images.get(position).getCover(), images.get(position).getBgcolor());
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setImages(List<ReadingBannerListVo.DataBean> list) {
        images.addAll(list);
        notifyDataSetChanged();
    }
}
