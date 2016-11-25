/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.waycube.wrjy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import cn.waycube.wrjy.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
@SuppressLint("ResourceAsColor")
public class TabLineIndicator extends HorizontalScrollView {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";
	Context  context;
	private int pos = 0;
	private boolean isclick=false;
	
	public void setClick(boolean isClick){
		this.isclick=isClick;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
		setCurrentItem(pos);
	}
	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			if(isclick)
				return;
			TabView tabView = (TabView) view;
			final int oldSelected = mSelectedTabIndex;
			final int newSelected = tabView.getIndex();
//			if (oldSelected != newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
//			}
			setCurrentItem(newSelected);
			setPos(newSelected);
		}
	};

	private final IcsLinearLayout mTabLayout;

	// private ViewPager mViewPager;
	// private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;

	public TabLineIndicator(Context context) {
		this(context, null);
	}

	int height = 0;

	public TabLineIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		mTabLayout.setGravity(Gravity.CENTER);
		Drawable backDrawable = mTabLayout.getBackground();
		if (backDrawable != null)
			height = backDrawable.getIntrinsicHeight();
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.6f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();
		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	@SuppressLint("ResourceAsColor")
	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};

		for (int i = 0; i < mTabLayout.getChildCount(); i++) {
			if (position == i) {
//				((TextView) tabView).setTextColor(getResources().getColor(R.color.blue));
				((TextView) tabView).setTextColor(android.graphics.Color.parseColor("#81B932"));//坐在这里是修改字体的颜色
				((TextView) tabView).setTextSize(16);
				mTabLayout.getChildAt(i).setBackgroundResource(R.mipmap.green_xiahuaxia);//这里是修改下面那个线
			} else{
				((TextView) mTabLayout.getChildAt(i)).setTextColor(android.graphics.Color.parseColor("#000000"));
				((TextView) mTabLayout.getChildAt(i)).setTextSize(16);
				mTabLayout.getChildAt(i).setBackgroundResource(R.mipmap.lab_backgruond);
			}
		}

		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	@SuppressLint("ResourceAsColor")
	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		tabView.setPadding(20, 0, 20, 0);
		tabView.setTextSize(20f);
		tabView.setTextColor(getResources().getColor(R.color.red));
		tabView.setGravity(Gravity.CENTER);
		tabView.setTypeface(Typeface.MONOSPACE);
		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		params.gravity = Gravity.CENTER_VERTICAL;
		mTabLayout.addView(tabView, params);
	}

	ArrayList<HashMap<String, String>> mList;

	public void addAdapter(ArrayList<HashMap<String, String>> mlist) {
		mTabLayout.removeAllViews();
		this.mList = mlist;
		final int count = mList.size();
		for (int i = 0; i < count; i++) {
			CharSequence title = mList.get(i).get("name");
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			// if (iconAdapter != null) {
			// iconResId = iconAdapter.getIconResId(i);
			// }
			addTab(i, title, iconResId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	// @Override
	// public void setViewPager(ViewPager view, int initialPosition) {
	// setViewPager(view);
	// setCurrentItem(initialPosition);
	// }

	public void setCurrentItem(int item) {
		if (mList == null) {
			return;
		}
		mSelectedTabIndex = item;

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	// @Override
	// public void setOnPageChangeListener(OnPageChangeListener listener) {
	// mListener = listener;
	// }

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context);
//			setBackgroundResource(R.drawable.tab_indicator);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (isInEditMode()) {
            return;
		}
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		if (isInEditMode()) {
            return;
		}
	}

}
