package view;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;


class GestureListener extends GestureDetector.SimpleOnGestureListener {
	private SSView mSsView;

	GestureListener(SSView paramSSView) {
		mSsView = paramSSView;
	}

	public boolean onDoubleTap(MotionEvent paramMotionEvent) {
		return super.onDoubleTap(paramMotionEvent);
	}

	public boolean onDoubleTapEvent(MotionEvent paramMotionEvent) {
		return super.onDoubleTapEvent(paramMotionEvent);
	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onFling(MotionEvent paramMotionEvent1,
						   MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public boolean onScroll(MotionEvent paramMotionEvent1,
							MotionEvent paramMotionEvent2, float x_scroll_distance, float y_scroll_distance) {
		//是否可以移动和点击
		if(!SSView.isMoveAndClick(mSsView)){
			return false;
		}
		//显示缩略图
		SSView.setIsPerView(mSsView, true);
		boolean bool1 = true;
		boolean bool2 = true;
		if ((SSView.getViewWidth(mSsView) < mSsView.getMeasuredWidth())
				&& (0.0F == SSView.getOffsetX(mSsView))){
			bool1 = false;
		}

		if ((SSView.getViewHeight(mSsView) < mSsView.getMeasuredHeight())
				&& (0.0F == SSView.getoffsetY(mSsView))){
			bool2  = false;
		}

		if(bool1){
			int k = Math.round(x_scroll_distance);
			//修改排数x轴的偏移量
			SSView.fixOffsetX(mSsView, (float) k);
//			Log.i("TAG", SSView.v(mSsView)+"");
			//修改座位距离排数的横向距离
			SSView.k(mSsView, k);
//			Log.i("TAG", SSView.r(mSsView)+"");
			if (SSView.getp(mSsView) < 0) {
				//滑到最左
				SSView.i(mSsView, 0);
				SSView.setoffsetX(mSsView, 0.0F);
			}

			if(SSView.getp(mSsView) + mSsView.getMeasuredWidth() > SSView.getViewWidth(mSsView)){
				//滑到最右
				SSView.i(mSsView, SSView.getViewWidth(mSsView) - mSsView.getMeasuredWidth());
				SSView.setoffsetX(mSsView, (float) (mSsView.getMeasuredWidth() - SSView.getViewWidth(mSsView)));
			}
		}

		if(bool2){
			//上负下正- 往下滑则减

			int j = Math.round(y_scroll_distance);
			//修改排数y轴的偏移量
			SSView.fixOffsetY(mSsView, (float) j);
			//修改可视座位距离顶端的距离
			SSView.l(mSsView, j);
//			Log.i("TAG", SSView.t(mSsView)+"");

			if (SSView.getq(mSsView) < 0){
				//滑到顶
				SSView.j(mSsView, 0);
				SSView.setOffsetY(mSsView, 0.0F);
			}

			if (SSView.getq(mSsView) + mSsView.getMeasuredHeight() > SSView
					.getViewHeight(mSsView)){
				//滑到底
				SSView.j(mSsView, SSView.getViewHeight(mSsView) - mSsView.getMeasuredHeight());
				SSView.setOffsetY(mSsView, (float) (mSsView.getMeasuredHeight() - SSView.getViewHeight(mSsView)));
			}
//			Log.e("y_scroll_distance","y_scroll_distance="+y_scroll_distance
//					+"   j="+j
//					+"  SSView.t(mSsView)="+SSView.t(mSsView)
//					+"  mSsView.getMeasuredHeight()="+mSsView.getMeasuredHeight()
//					+"   SSView.getViewHeight(mSsView)="+SSView.getViewHeight(mSsView));
		}

		mSsView.invalidate();

//		Log.i("GestureDetector", "onScroll----------------------");
		return false;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
//		Log.i("GestureDetector", "onSingleTapUp");
//		if(!SSView.a(mSsView)){
//			return false;
//		}
		//列数
		int i = SSView.getCurrentColum(mSsView, (int) paramMotionEvent.getX());
		//排数
		int j = SSView.getCurrentRow(mSsView, (int) paramMotionEvent.getY());

		if((j>=0 && j< SSView.getSeatList(mSsView).size())){
			if(i>=0 && i<((ArrayList<Integer>)(SSView.getSeatList(mSsView).get(j))).size()){
				Log.i("TAG", "排数："+ j + "列数："+i);
				ArrayList<Integer> localArrayList = (ArrayList<Integer>) SSView.getSeatList(mSsView).get(j);
				switch (localArrayList.get(i).intValue()) {
					case 3://已选中
						localArrayList.set(i, Integer.valueOf(1));
						if(SSView.getSeatClickListener(mSsView)!=null){
							SSView.getSeatClickListener(mSsView).cancelSelect(i, j, false);
						}

						break;
					case 1://可选
						localArrayList.set(i, Integer.valueOf(3));
						if(SSView.getSeatClickListener(mSsView)!=null){
							SSView.getSeatClickListener(mSsView).select(i, j, false);
						}
						break;
					default:
						break;
				}

			}
		}

		//显示缩略图
		SSView.setIsPerView(mSsView,true);
		mSsView.invalidate();
		return false;
	}
}