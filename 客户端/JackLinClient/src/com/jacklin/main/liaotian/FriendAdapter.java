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

import com.jacklin.jacklinclient.R;

@SuppressLint("InflateParams")
public class FriendAdapter extends BaseAdapter {
	private List<FriendModel> mList=null;
	private Context mContext;
	
	public FriendAdapter(Context context,List<FriendModel> list){
		mContext=context;
		mList=list;
	}
	public void updateListView(List<FriendModel> list){
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		final FriendModel contactsModel=mList.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.jacklin_friend_item, null);
			viewHolder.iv=(ImageView) convertView.findViewById(R.id.contacts_photo);
			viewHolder.tvName=(TextView) convertView.findViewById(R.id.contacts_friend);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		Drawable drawable=contactsModel.getPhoto();
		if(drawable!=null){
			viewHolder.iv.setImageDrawable(drawable);
		}else{
			viewHolder.iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_photo));
		}
		viewHolder.tvName.setText(contactsModel.getName());
		
		return convertView;
	}

	final static class ViewHolder{
		ImageView iv;
		TextView tvName;
	}
}
