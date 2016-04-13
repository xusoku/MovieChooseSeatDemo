package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.xusoku.moviechooseseatdemo.OnSeatClickListener;
import com.example.xusoku.moviechooseseatdemo.R;

import java.util.ArrayList;

import model.Seat;
import model.SeatInfo;


public class SSView extends View {
	Context mContext;
	int x_offset = 0;

	/** 普通状态 */
	private Bitmap mBitMapSeatNormal = null;
	/** 已锁定 */
	private Bitmap mBitMapSeatLock = null;
	/** 已选中 */
	private Bitmap mBitMapSeatChecked = null;

	/** 缩略图画布 */
	private Canvas mCanvas = null;

	/** 是否显示缩略图 */
	private boolean isPerView = false;

	/** 每个座位的高度 - 57 */
	private int ss_seat_current_height = 57;
	/** 每个座位的宽度 */
	private int ss_seat_current_width = 57;
	/** 座位之间的间距 */
	private int L = 5;
	private double T = 1.0D;

	private double t = -1.0D;
	private double u = 1.0D;
	/** 是否可缩放 */
	private boolean isScale = false;

	/** 座位最小高度 */
	private int ss_seat_min_height = 0;
	/** 座位最大高度 */
	private int ss_seat_max_height = 0;
	/** 座位最小宽度 */
	private int ss_seat_min_width = 0;
	/** 座位最大宽度 */
	private int ss_seat_max_width = 0;

	private OnSeatClickListener mOnSeatClickListener = null;

	public static double a = 1.0E-006D;
	private int ss_between_offset = 2;
	private int ss_seat_check_size = 50;
	private SSThumView mSSThumView = null;
	private int ss_seat_thum_size_w = 120;
	private int ss_seat_thum_size_h = 90;
	private int ss_seat_rect_line = 2;
	/** 选座缩略图 */
	private Bitmap mBitMapThumView = null;
	/** 左边距 */
	private int mLeft = 0;
	/** 右边距 */
	private int mRight = 0;
	/** 上边距 */
	private int mTop = 0;
	/** 下边距 */
	private int mBottom = 0;
	/** 排数x轴偏移量 */
	private float offsetX = 0.0F;
	/** 排数y轴偏移量 */
	private float offsetY = 0.0F;
	/** 座位距离排数的距离 */
	private int p = 0;
	/** 可视座位距离顶端的距离 */
	private int q = 0;
	/** 整个view的宽度 */
	private int viewWidth = 0;
	/** 整个view的高度 */
	private int heightSize = 0;
	/** 初始化view的高度 */
	private int viewHeight = 0;
	/** 能否移动 */
	private boolean isMove = true;

	private boolean first_load_bg = true;
	private int tempX;
	private int tempY;

	GestureDetector mGestureDetector = new GestureDetector(mContext,
			new GestureListener(this));

	private ArrayList<SeatInfo> mListSeatInfos = null;
	private ArrayList<ArrayList<Integer>> mListSeatConditions = null;
	private int iMaxPay = 0;
	private int totalCountEachRow;
	private int rows;

	public SSView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public SSView(Context paramContext, AttributeSet paramAttributeSet,
				  int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.mContext = paramContext;
	}

	public void init(int row_count, int rows,
					 ArrayList<SeatInfo> list_seatInfos,
					 ArrayList<ArrayList<Integer>> list_seat_condtions,
					 SSThumView paramSSThumView, int imaxPay) {
		this.iMaxPay = imaxPay;
		this.mSSThumView = paramSSThumView;
		this.totalCountEachRow = row_count;
		this.rows = rows;
		this.mListSeatInfos = list_seatInfos;
		this.mListSeatConditions = list_seat_condtions;
		this.mBitMapSeatNormal = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.mipmap.seat_normal));
		this.mBitMapSeatLock = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.mipmap.seat_lock));
		this.mBitMapSeatChecked = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.mipmap.seat_checked));

		this.ss_seat_thum_size_w = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_w);
		this.ss_seat_thum_size_h = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_h);

		this.ss_seat_max_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_max_height);
		this.ss_seat_max_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_max_width);
		this.ss_seat_min_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_min_height);
		this.ss_seat_min_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_min_width);
		this.ss_seat_current_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_height);
		this.ss_seat_current_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_width);
		this.ss_seat_check_size = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_check_size);//30dp
		this.ss_between_offset = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_between_offset);//5dp



//		ss_seat_current_width=90    ss_seat_current_height=90    mLeft=45    mRight=45    L=15    offsetX=0.0    offsetY=0.0    p=0    q=0
		mLeft=45;
		mRight=45;
		L=15;

		mTop=100;
//		ss_seat_current_width=90    ss_seat_current_height=90    mLeft=45    mRight=45    L=15    offsetX=-336.0    offsetY=0.0    p=336    q=0

		invalidate();
	}

	public static Bitmap getBitmapFromDrawable(
			BitmapDrawable paramBitmapDrawable) {
		return paramBitmapDrawable.getBitmap();
	}

	/**
	 *
	 * @param seatNum
	 *            每排的座位顺序号
	 * @param rowNum
	 *            排号
	 * @param paramBitmap
	 * @param paramCanvas1
	 * @param paramCanvas2
	 * @param paramPaint
	 */
	private void drawSeat(int seatNum, int rowNum, Bitmap paramBitmap,
						  Canvas paramCanvas1, Canvas paramCanvas2, Paint paramPaint) {
		if (paramBitmap == null) {// 走道
			paramCanvas1.drawRect(getSeatRect(seatNum, rowNum), paramPaint);
			if (this.isPerView) {
				paramCanvas2.drawRect(getPreSeatRect(seatNum, rowNum), paramPaint);
			}
		} else {
			paramCanvas1.drawBitmap(paramBitmap, null, getSeatRect(seatNum, rowNum),
					paramPaint);
			if (this.isPerView) {
				paramCanvas2.drawBitmap(paramBitmap, null, getPreSeatRect(seatNum, rowNum),
						paramPaint);
			}
		}
	}

	/**
	 *
	 * @param seatNum
	 *            每排的座位号
	 * @param rowNum
	 *            排号
	 * @return
	 */
	private Rect getSeatRect(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(this.mLeft + seatNum * this.ss_seat_current_width + this.L,
					this.mTop + rowNum * this.ss_seat_current_height + this.L, this.mLeft + (seatNum + 1)
					* this.ss_seat_current_width - this.L, this.mTop + (rowNum + 1) * this.ss_seat_current_height
					- this.L);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	private Rect getPreSeatRect(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(
					5 + (int) (this.T * (this.mLeft + seatNum * this.ss_seat_current_width + this.L)),
					5 + (int) (this.T * ( rowNum * this.ss_seat_current_height + this.L)),
					5 + (int) (this.T * (this.mLeft + (seatNum + 1) * this.ss_seat_current_width - this.L)),
					5 + (int) (this.T * ( (rowNum + 1) * this.ss_seat_current_height - this.L)));
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	//缩略图显示区域 perRegion
	private Rect perRegionRect(int offsetX, int offsetY) {
		int viewWidth;
		int viewHeight;
		try {
			if (getMeasuredWidth() < this.viewWidth) {
				viewWidth = getMeasuredWidth();
			} else {
				viewWidth = this.viewWidth;
			}
			if (getMeasuredHeight() < this.viewHeight) {
				viewHeight = getMeasuredHeight();
			} else {
				viewHeight = this.viewHeight;
			}
			return new Rect((int) (5.0D + this.T * offsetX),
					(int) (5.0D + this.T * offsetY), (int) (5.0D + this.T
					* offsetX + viewWidth * this.T), (int) (5.0D + this.T
					* offsetY + (viewHeight-mTop) * this.T));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Rect();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		viewWidth=this.mLeft + this.ss_seat_current_width * this.totalCountEachRow + this.mRight;

		this.heightSize=heightSize;
//		this.viewHeight = this.ss_seat_current_height * this.rows+mTop;
//
////		Log.e("viewWidth","viewWidth="+viewWidth+"   widthSize="+widthSize);
//		Log.e("viewHeight","viewHeight="+viewHeight+"   heightSize="+heightSize);


		if(viewWidth>widthSize) {
			offsetX = -viewWidth / 2f + widthSize / 2;
			p = viewWidth / 2 - +widthSize / 2;
		}

	}
	@Override
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		// Log.i("TAG", "onDraw()...");
		if (this.totalCountEachRow == 0 || this.rows == 0) {
			return;
		}
		if (this.offsetX + this.viewWidth < 0.0f || this.offsetY + this.viewHeight < 0.0f) {
			this.offsetX = 0.0f;
			this.offsetY = 0.0f;
			this.p = 0;
			this.q = 0;
		}


		Paint localPaint2 = new Paint();
		if (this.ss_seat_current_width != 0 && this.ss_seat_current_height != 0) {

			this.mBitMapThumView = Bitmap.createBitmap(this.ss_seat_thum_size_w,
					this.ss_seat_thum_size_h, Bitmap.Config.ARGB_8888);
			this.mCanvas = new Canvas();
			this.mCanvas.setBitmap(this.mBitMapThumView);
			this.mCanvas.save();

			Paint localPaint1 = new Paint();
			localPaint1.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.CLEAR));
			this.mCanvas.drawPaint(localPaint1);

			double d1 = (this.ss_seat_thum_size_w - 10.0D)
					/ (this.ss_seat_current_width * this.totalCountEachRow + this.mLeft + this.mRight); // -
			// v0/v2
			double d2 = (this.ss_seat_thum_size_h - 10.0D)
					/ (this.ss_seat_current_height * this.rows);
			if (d1 <= d2) {
				this.T = d1;
			} else {
				this.T = d2;
			}


			if(this.isPerView){
				localPaint2.setColor(Color.WHITE);
				if(first_load_bg){
					first_load_bg = false;
					tempX = 5+(int) ((this.viewWidth) * this.T);
					tempY = 5 + (int) ((this.viewHeight-mTop) * this.T);
				}
				this.mCanvas.drawRect(5.0F, 5.0F,  tempX,
						tempY, localPaint2);
			}
		}
		Log.e("LLL", "ss_seat_current_width=" + ss_seat_current_width + "    ss_seat_current_height=" + ss_seat_current_height
				+ "    mLeft=" + mLeft + "    mRight=" + mRight + "    mTop=" + mTop + "    mBottom=" + mBottom +
				"      L=" + L + "    offsetX=" + offsetX + "    offsetY=" + offsetY
				+ "    p=" + p + "    q=" + q);
		paramCanvas.translate(this.offsetX, this.offsetY);
		this.viewWidth = this.mLeft + this.ss_seat_current_width * this.totalCountEachRow + this.mRight;
		this.viewHeight = this.ss_seat_current_height * this.rows+mTop;

		Log.e("viewWidth====","viewWidth="+viewWidth+"   viewHeight="+viewHeight);

		if(viewHeight<=heightSize){
			this.offsetY = 0.0f;
			this.q = 0;
		}

		//居中线的画笔
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLUE);
		PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
		paint.setPathEffect(effects);

		Paint paintText = new Paint();
		paintText.setAntiAlias(true);
		paintText.setStrokeWidth(2f);
		paintText.setStyle(Paint.Style.FILL);
		paintText.setTextSize(Math.min(2.0f * L, 40.0f));//设置字体大小
		float textCenterSize=paintText.measureText("银幕中央") / 2;

		paramCanvas.drawLine((viewWidth) / 2, textCenterSize, (viewWidth) / 2, viewHeight, paint);

		paintText.setColor(Color.BLACK);
		paintText.setAlpha(200);
		RectF rectF=new RectF((viewWidth) / 2 - textCenterSize-10, 0, (viewWidth) / 2 + textCenterSize+10, textCenterSize);
		paramCanvas.drawRoundRect(rectF,5,5, paintText);
		paintText.setColor(Color.WHITE);
		paintText.setAlpha(255);
		paramCanvas.drawText("银幕中央", (viewWidth) / 2 - textCenterSize, textCenterSize/3*2, paintText);

		this.mLeft = (int) Math.round(this.ss_seat_current_width / 2.0D);
		localPaint2.setTextAlign(Paint.Align.CENTER);
		localPaint2.setAntiAlias(true);
		localPaint2.setColor(Color.WHITE);

		for (int i2 = 0; i2 < this.mListSeatConditions.size(); i2++) {
			ArrayList<Integer> localArrayList = (ArrayList<Integer>) this.mListSeatConditions
					.get(i2);


			for (int i3 = 0; i3 < this.mListSeatInfos.get(i2).getSeatList()
					.size(); i3++) {

				Seat localSeat = this.mListSeatInfos.get(i2).getSeat(i3);
				switch (((Integer) localArrayList.get(i3)).intValue()) {
					case 0: //  - 走道
						localPaint2.setColor(0);
						drawSeat(i3, i2, null, paramCanvas, this.mCanvas, localPaint2);
						localPaint2.setColor(Color.WHITE);
						break;
					case 1:// 可选
						drawSeat(i3, i2, this.mBitMapSeatNormal, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					case 2:// 不可选
						drawSeat(i3, i2, this.mBitMapSeatLock, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					case 3: // 一已点击的状态
						drawSeat(i3, i2, this.mBitMapSeatChecked, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					default:
						break;
				}
			}

		}

		// 画排数
		localPaint2.setTextSize(0.4F * this.ss_seat_current_height);
		for (int i1 = 0; i1 < this.mListSeatInfos.size(); i1++) {
			localPaint2.setColor(Color.GRAY);
			paramCanvas.drawRect(new Rect((int) Math.abs(this.offsetX), this.mTop
					+ i1 * this.ss_seat_current_height, (int) Math.abs(this.offsetX) + this.ss_seat_current_width / 2,
					this.mTop + (i1 + 1) * this.ss_seat_current_height), localPaint2);
			localPaint2.setColor(-1);

			paramCanvas
					.drawText(((SeatInfo) this.mListSeatInfos.get(i1))
							.getDesc(), (int) Math.abs(this.offsetX) + this.ss_seat_current_width / 2
							/ 2, this.mTop + i1 * this.ss_seat_current_height + this.ss_seat_current_height / 2 + this.mBottom
							/ 2, localPaint2);
		}


		if (this.isPerView) {
			// 画缩略图的黄色框
			localPaint2.setColor(Color.YELLOW);
			localPaint2.setStyle(Paint.Style.STROKE);
			localPaint2.setStrokeWidth(this.ss_seat_rect_line);
			this.mCanvas.drawRect(
					perRegionRect((int) Math.abs(this.offsetX), (int) Math.abs(this.offsetY)),
					localPaint2);
			localPaint2.setStyle(Paint.Style.FILL);
			// paramCanvas.restore();
			this.mCanvas.restore();
		}

		if (this.mSSThumView != null) {
			this.mSSThumView.setBitmap(mBitMapThumView);
			this.mSSThumView.invalidate();
		}

	}

	public void setXOffset(int x_offset) {
		this.x_offset = x_offset;
	}


	/**
	 */
	public static int getmLeft(SSView mSsView) {
		return mSsView.mLeft;
	}


	/**
	 * @param mSsView
	 * @return
	 */
	public static float getoffsetY(SSView mSsView) {
		return mSsView.offsetY;
	}

	/**
	 * 获取排数x轴偏移量
	 *
	 * @param mSsView
	 * @return
	 */
	public static float getOffsetX(SSView mSsView) {
		return mSsView.offsetX;
	}

	/**
	 * 获取整个view的高度
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getViewHeight(SSView mSsView) {
		return mSsView.viewHeight;
	}

	/**
	 * 获取可视座位距离顶端的距离
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getq(SSView mSsView) {
		return mSsView.q;
	}

	/**
	 * 获取整个view的宽度
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getViewWidth(SSView mSsView) {
		return mSsView.viewWidth;
	}

	/**
	 * 获取座位距离排数的横向距离
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getp(SSView mSsView) {
		return mSsView.p;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getmTop(SSView mSsView) {
		return mSsView.mTop;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getRows(SSView mSsView) {
		return mSsView.rows;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getmRight(SSView mSsView) {
		return mSsView.mRight;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int n(SSView mSsView) {
		return mSsView.totalCountEachRow;
	}

	/**
	 * 修改可见座位距离顶端的距离
	 *
	 * @param mSsView
	 * @return
	 */
	public static int l(SSView mSsView, int paramInt) {
		mSsView.q = mSsView.q + paramInt;
		return mSsView.q;
	}


	/**
	 * 修改座位距离排数的距离
	 *
	 * @param mSsView
	 * @param paramInt
	 * @return
	 */
	public static int k(SSView mSsView, int paramInt) {
		mSsView.p = mSsView.p + paramInt;
		return mSsView.p;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getSeatHeight(SSView mSsView) {
		return mSsView.ss_seat_current_height;
	}

	/**
	 * 设置可视座位距离顶端的距离
	 *
	 * @param mSsView
	 * @param paramInt
	 * @return
	 */
	public static int j(SSView mSsView, int paramInt) {
		mSsView.q = paramInt;
		return mSsView.q;
	}

	/**
	 * 设置座位距离排数的横向距离
	 *
	 * @param mSsView
	 * @return
	 */
	public static int i(SSView mSsView, int paramInt) {
		mSsView.p = paramInt;
		return mSsView.p;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static boolean getIsPerView(SSView mSsView) {
		return mSsView.isPerView;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getViewHeight(SSView mSsView, int paramInt) {
		return mSsView.viewHeight;
	}


	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getViewWidth(SSView mSsView, int paramInt) {
		return mSsView.viewWidth;
	}

	/**
	 * 获取最大支付座位数
	 *
	 * @param mSsView
	 * @return
	 */
	public static int getImaxPay(SSView mSsView) {
		return mSsView.iMaxPay;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static boolean setIsPerView(SSView mSsView, boolean param) {
		mSsView.isPerView = param;
		return mSsView.isPerView;
	}

	/**
	 * 设置排数x轴偏移量
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float setoffsetX(SSView mSsView, float param) {
		mSsView.offsetX = param;
		return mSsView.offsetX;
	}

	/**
	 * 计算是第几列
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int getCurrentColum(SSView mSsView, int param) {
		try {
			int i1 = (param + mSsView.p - mSsView.mLeft) / mSsView.ss_seat_current_width;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @param param1
	 * @param param2
	 * @return
	 */
//	public static Rect a(SSView mSsView, int param1, int param2) {
//		return mSsView.f(param1, param2);
//	}

//	private Rect f(int paramInt1, int paramInt2) {
//		try {
//			int v1 = this.ss_seat_current_width * paramInt1 + this.mLeft - this.p - this.L;
//			int v2 = this.ss_seat_current_height * paramInt2 + this.mTop - this.q - this.L;
//			int v3 = (paramInt1 + 1) * this.ss_seat_current_width + this.mLeft - this.p + this.L;
//			int v4 = (this.mTop + 1) * this.ss_seat_current_height + this.mTop - this.q + this.L;
//			return new Rect(v1, v2, v3, v4);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new Rect();
//		}
//	}

	/**
	 * 是否可以移动和点击
	 *
	 * @param mSsView
	 * @return
	 */
	public static boolean isMoveAndClick(SSView mSsView) {
		return mSsView.isMove;
	}


	/**
	 * 修改排数x轴的
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float fixOffsetX(SSView mSsView, float param) {
		mSsView.offsetX = mSsView.offsetX - param;
		return mSsView.offsetX;
	}

	/**
	 * 设置每个座位的高度
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float setSeatHeight(SSView mSsView, int param) {
		mSsView.ss_seat_current_height = param;
		return mSsView.ss_seat_current_height;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static ArrayList setDataList(SSView mSsView) {
		return mSsView.mListSeatInfos;
	}

	/**
	 * 修改排数y轴的偏移量
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float fixOffsetY(SSView mSsView, float param) {
		mSsView.offsetY = mSsView.offsetY - param;
		return mSsView.offsetY;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int setCurrentSeatWidth(SSView mSsView, int param) {
		mSsView.ss_seat_current_width = param;
		return mSsView.ss_seat_current_width;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static OnSeatClickListener getSeatClickListener(SSView mSsView) {
		return mSsView.mOnSeatClickListener;
	}

	/**
	 * 设置排数y轴偏移量
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float setOffsetY(SSView mSsView, float param) {
		mSsView.offsetY = param;
//		if(param<0){
//			mSsView.offsetY-=mSsView.mTop;
//			mSsView.q=(int)Math.abs(mSsView.offsetY);
//		}
		return mSsView.offsetY;
	}

	/**
	 * 计算是第几排
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int getCurrentRow(SSView mSsView, int param) {
		try {
			int i1 = (param + mSsView.q - mSsView.mTop) / mSsView.ss_seat_current_height;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	/**
	 * new added
	 *
	 * @param mSsView
	 * @return
	 */
	public static ArrayList getSeatList(SSView mSsView) {
		return mSsView.mListSeatConditions;
	}


	/**
	 * new added
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int e(SSView mSsView, int param)  {
		mSsView.mLeft = param;
		return mSsView.mLeft;
	}


	/**
	 * new added
	 *
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int setmRight(SSView mSsView, int param) {
		mSsView.mRight = param;
		return mSsView.mRight;
	}

	/**
	 * 设置按钮点击事件
	 *
	 * @param paramOnSeatClickLinstener
	 */
	public void setOnSeatClickListener(
			OnSeatClickListener paramOnSeatClickLinstener) {
		this.mOnSeatClickListener = paramOnSeatClickLinstener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getPointerCount() == 1) {
			if (this.isScale) {
				this.isScale = false;
				this.isMove = false;
				this.t = -1.0D;
				this.u = 1.0D;
			}else{
				this.isMove = true;
			}

			// Toast.makeText(mContext, "单点触控", Toast.LENGTH_SHORT).show();
			while (this.ss_seat_current_width < this.ss_seat_min_width || this.ss_seat_current_height < this.ss_seat_min_height) {
				this.ss_seat_current_width++;
				this.ss_seat_current_height++;
				this.mLeft = (int) Math.round(this.ss_seat_current_width / 2.0D);
				this.mRight = this.mLeft;
				this.L = (int) Math.round(this.ss_seat_current_width / this.ss_seat_check_size * this.ss_between_offset);
				// 滑到最左和最上
				SSView.i(this, 0);
				SSView.setoffsetX(this, 0.0F);
				SSView.j(this, 0);
				SSView.setOffsetY(this, 0.0F);
				invalidate();
			}
			while ((this.ss_seat_current_width > this.ss_seat_max_width) || (this.ss_seat_current_height > this.ss_seat_max_height)) {
				this.ss_seat_current_width--;
				this.ss_seat_current_height--;
				this.mLeft = (int) Math.round(this.ss_seat_current_width / 2.0D);
				this.mRight = this.mLeft;
				this.L = (int) Math.round(this.ss_seat_current_width / this.ss_seat_check_size * this.ss_between_offset);
				invalidate();
			}

			// 移动功能-点击事件
			this.mGestureDetector.onTouchEvent(event);
		} else {
			// Toast.makeText(mContext, "多点触控", Toast.LENGTH_SHORT).show();
			this.isScale = true;
			a(event);

		}

		return true;
	}



	/**
	 * 获取两点的直线距离
	 *
	 * @param paramMotionEvent
	 * @return
	 */
	private double getTwoPoiniterDistance(MotionEvent paramMotionEvent) {
		float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
		float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
		return Math.sqrt(f1 * f1 + f2 * f2);
	}

	private void a(MotionEvent paramMotionEvent) {
		double d1 = getTwoPoiniterDistance(paramMotionEvent);
		if (this.t < 0.0D) {
			this.t = d1;
		} else {
			try {
				this.u = (d1 / this.t);
				this.t = d1;
				if ((this.isScale) && (Math.round(this.u * this.ss_seat_current_width) > 0L)
						&& (Math.round(this.u * this.ss_seat_current_height) > 0L)) {
					this.ss_seat_current_width = (int) Math.round(this.u * this.ss_seat_current_width);
					this.ss_seat_current_height = (int) Math.round(this.u * this.ss_seat_current_height);
					this.mLeft = (int) Math.round(this.ss_seat_current_width / 2.0D);
					this.mRight = this.mLeft;
					this.L = (int) Math.round(this.u * this.L);
					if (this.L <= 0)
						this.L = 1;
				}
				Log.e("L","="+L);
				invalidate();
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}

	}

}
