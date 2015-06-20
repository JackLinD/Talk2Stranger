package com.jacklin.main.liaotian;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
	List<Fragment> fragmentList=new ArrayList<Fragment>();
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList=fragmentList;
		// TODO Auto-generated constructor stub
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentList.get(arg0);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}

}
