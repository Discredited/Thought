package com.project.xujun.juneutils.listview;

import android.util.SparseArray;
import android.view.View;

/**
 * Adapter中的ITEM
 *
 * @author Administrator
 */
public class JuneViewHodler {

    /**
     * ImageView view = AbViewHolder.get(convertView, R.id.imageView);
     *
     * @param convertView
     * @param id
     * @return
     */
    public static <T extends View> T get(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
