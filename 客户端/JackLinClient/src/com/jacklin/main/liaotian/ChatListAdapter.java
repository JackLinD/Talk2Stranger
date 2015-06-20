package com.jacklin.main.liaotian;

import java.util.List;
import com.jacklin.jacklinclient.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ChatListAdapter extends BaseAdapter {
	private List<ChatListModel> mList = null;
	private Context mContext;

	public ChatListAdapter(Context context, List<ChatListModel> list) {
		mContext = context;
		mList = list;
	}

	public void updateListView(List<ChatListModel> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		final ChatListModel model=mList.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.jacklin_chatlist_item, null);
			viewHolder.iv=(ImageView) convertView.findViewById(R.id.chat_photo);
			viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.chat_title);
			viewHolder.tvTime=(TextView) convertView.findViewById(R.id.chat_time);
			viewHolder.tvContent=(TextView) convertView.findViewById(R.id.chat_content);
			viewHolder.tvUnRead=(TextView) convertView.findViewById(R.id.chat_unreadTips);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		Drawable drawable=model.getPhoto();
		if(drawable!=null){
			viewHolder.iv.setImageDrawable(drawable);
		}else{
			viewHolder.iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_photo));
		}
		
		if(model.getUnread()>0){
			viewHolder.tvUnRead.setText(model.getUnread()+"");
			viewHolder.tvUnRead.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvUnRead.setVisibility(View.GONE);
		}
		
		viewHolder.tvTime.setText(model.getTime());
		viewHolder.tvTitle.setText(model.getTitle());
		viewHolder.tvContent.setText(model.getContent());
		
		return convertView;
	}

	final static class ViewHolder {
		ImageView iv;
		TextView tvTitle, tvUnRead, tvContent, tvTime;
	}
}
