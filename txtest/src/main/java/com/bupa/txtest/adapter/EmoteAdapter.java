package com.bupa.txtest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bupa.txtest.R;
import com.bupa.txtest.base.BaseArrayListAdapter;
import com.bupa.txtest.event.FaceText;
import com.bupa.txtest.utils.UIUtils;

import java.util.List;

/**
 * 作者: l on 2017/2/19 14:13
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class EmoteAdapter extends BaseArrayListAdapter {

    public EmoteAdapter(Context context, List<FaceText> datas) {
        super(context, datas);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_face_text, null);
            holder = new ViewHolder();
            holder.mIvImage = (ImageView) convertView
                    .findViewById(R.id.v_face_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FaceText faceText = (FaceText) getItem(position);
        String key = faceText.text.substring(1);
        Drawable drawable = UIUtils.getContext().getResources().getDrawable(UIUtils.getContext().getResources().getIdentifier(key, "drawable", UIUtils.getContext().getPackageName()));
        holder.mIvImage.setImageDrawable(drawable);
        return convertView;
    }

    class ViewHolder {
        ImageView mIvImage;
    }
}
