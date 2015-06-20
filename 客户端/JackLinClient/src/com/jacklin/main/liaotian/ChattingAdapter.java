package com.jacklin.main.liaotian;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacklin.db.CacheUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.PhotoUtils;

@SuppressLint("InflateParams")
public class ChattingAdapter extends BaseAdapter {
	private List<ChattingModel> mList=null;
	private Context mContext;
	
	public ChattingAdapter(Context context,List<ChattingModel> list) {
		mContext=context;
		mList=list;
	}
	
	public void updateListView(List<ChattingModel> list){
		mList=list;
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

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		final ChattingModel model=mList.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			if(model.isSend()){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.jacklin_chatting_send_item, null);
				viewHolder.iv=(ImageView) convertView.findViewById(R.id.chatting_send_photo);
				viewHolder.tvTime=(TextView) convertView.findViewById(R.id.chatting_send_time);
				viewHolder.tvMsg=(TextView) convertView.findViewById(R.id.chatting_send_msg);
			}else{
				convertView=LayoutInflater.from(mContext).inflate(R.layout.jacklin_chatting_receive_item, null);
				viewHolder.iv=(ImageView) convertView.findViewById(R.id.chatting_receive_photo);
				viewHolder.tvTime=(TextView) convertView.findViewById(R.id.chatting_receive_time);
				viewHolder.tvMsg=(TextView) convertView.findViewById(R.id.chatting_receive_msg);
			}
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		if(model.isSend()){
			Drawable me=PhotoUtils.StringToDrawable(CacheUtils.GetPhoto());
			if(me !=null){
				viewHolder.iv.setImageDrawable(me);
			}
		}else{
			Drawable friend = model.getPhoto();
			if(friend != null)
			{
				viewHolder.iv.setImageDrawable(friend);
			}
		}
		
		if(model.isShowTime()){
			String time=model.getTime();
			viewHolder.tvTime.setVisibility(View.VISIBLE);
			viewHolder.tvTime.setText(time);
		}else{
			viewHolder.tvTime.setVisibility(View.GONE);
		}
		
		viewHolder.tvMsg.setText(model.getMsg());
		return null;
	}

	
	final static class ViewHolder{
		ImageView iv;
		TextView tvTime,tvMsg;
	}
}
