package com.jacklin.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacklin.jacklinclient.R;
import com.jacklin.main.liaotian.ChatListFragment;
import com.jacklin.main.liaotian.FragmentAdapter;
import com.jacklin.main.liaotian.FriendFragment;

@SuppressLint("InflateParams")
public class LiaotianFragment extends Fragment implements OnClickListener {
	private TextView tv1, tv2;
	private Fragment fm1, fm2;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private ViewPager mViewPager;
	private FragmentAdapter mFragmentAdapter;
	private int currIndex;// 当前页卡编号
	private int bmpW;// 横线图片宽度
	private int offset;// 图片移动的偏移量
	private ImageView image;
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.jacklin_liaotian_fragment, null);
		initView();
		initImage();
		return v;
	}

	public void initView() {
		tv1 = (TextView) v.findViewById(R.id.tva);
		tv2 = (TextView) v.findViewById(R.id.tvb);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		fm1 = new ChatListFragment();
		fm2 = new FriendFragment();
		mViewPager = (ViewPager) v.findViewById(R.id.content1);
		mFragmentList.add(fm1);
		mFragmentList.add(fm2);
		mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(),
				mFragmentList);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int one = offset * 2 + bmpW;// 两个相邻页面的偏移量

			@Override
			public void onPageSelected(int arg0) {
				Animation animation = new TranslateAnimation(currIndex * one,
						arg0 * one, 0, 0);// 平移动画
				currIndex = arg0;
				animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
				animation.setDuration(200);// 动画持续时间0.2秒
				image.startAnimation(animation);// 是用ImageView来显示动画的
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	public void initImage() {
		image = (ImageView) v.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.chat_topbar_line).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 2 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tva:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tvb:
			mViewPager.setCurrentItem(1);
			break;
		}

	}
}
