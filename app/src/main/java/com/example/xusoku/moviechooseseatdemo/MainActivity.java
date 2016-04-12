package com.example.xusoku.moviechooseseatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.xusoku.mylibrary.cinema.SelectMovieSeatActivity;

import java.util.ArrayList;

import model.Seat;
import model.SeatInfo;
import view.SSThumView;
import view.SSView;

public class MainActivity extends AppCompatActivity {
    private static final int ROW = 8;
    private static final int EACH_ROW_COUNT = 12;
    private SSView mSSView;
    private SSThumView mSSThumView;
    private ArrayList<SeatInfo> list_seatInfos = new ArrayList<SeatInfo>();
    private ArrayList<ArrayList<Integer>> list_seat_conditions = new ArrayList<ArrayList<Integer>>();
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        findViewById(R.id.doClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SelectMovieSeatActivity.class));
            }
        });
    }


    private void init() {
        mSSView = (SSView) this.findViewById(R.id.mSSView);
        mSSView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//为了显示虚线
        mSSThumView = (SSThumView) this.findViewById(R.id.ss_ssthumview);
//		mSSView.setXOffset(20);
        setSeatInfo();
        mSSView.init(EACH_ROW_COUNT, ROW, list_seatInfos, list_seat_conditions, mSSThumView, 5);
        mSSView.setOnSeatClickListener(new OnSeatClickListener() {

            @Override
            public boolean select(int column_num, int row_num, boolean paramBoolean) {
                int[] temp = getRealRowAndColumn(row_num, column_num);
                String desc = "您选择了第" + temp[0] + "排" + " 第" + temp[1] + "列";
                showTextToast(desc.toString());
                return false;
            }

            @Override
            public boolean cancelSelect(int column_num, int row_num, boolean paramBoolean) {
                int[] temp = getRealRowAndColumn(row_num, column_num);
                String desc = "您取消了第" + temp[0] + "排" + " 第" + temp[1] + "列";
                showTextToast(desc.toString());
                return false;
            }

            @Override
            public void a() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setSeatInfo() {
        for (int i = 0; i < ROW; i++)
        {//行
            SeatInfo mSeatInfo = new SeatInfo();
            ArrayList<Seat> mSeatList = new ArrayList<Seat>();
            ArrayList<Integer> mConditionList = new ArrayList<Integer>();
            for (int j = 0; j < EACH_ROW_COUNT; j++)
                {//每排座位数
                    Seat mSeat = new Seat();
                    if (i==3||i==2&&j==2||i==4&&j==2||j==5||i==1&&j==0) {
                        mSeat.setN("Z");
                        mConditionList.add(0);
                    } else {
                        mSeat.setN(String.valueOf(j - 2));
                        if (j > 10) {
                            mConditionList.add(2);
                        } else {
                            mConditionList.add(1);
                        }
                    }
                    mSeat.setDamagedFlg("");
                    mSeat.setLoveInd("0");
                    mSeatList.add(mSeat);
                }
//            if(i==3){
//                mSeatInfo.setDesc(String.valueOf(0));
//                mSeatInfo.setRow(String.valueOf(-1));
//            }else{
                mSeatInfo.setDesc(getDesc(i,3));
                mSeatInfo.setRow(String.valueOf(i + 1));
//            }

            mSeatInfo.setSeatList(mSeatList);
            list_seatInfos.add(mSeatInfo);
            list_seat_conditions.add(mConditionList);
        }


    }

    private String getDesc(int row,int setN){
        int a=0;
        if(setN!=-1){
            if(row==setN){
                a=0;
                return a+"";
            }
            a=0;
            int tempcount=0;
            if(list_seatInfos.size()>0)
                for (int z = 0; z < row; z++) {
                    if(list_seatInfos.get(z).getDesc().equals("0"))
                        tempcount++;
                }
            a=row-tempcount+1;
        }


        return  ""+a;
    }

    private int [] getRealRowAndColumn(int row,int column){
        int [] a={row+1,column+1};
        int rowSize=list_seat_conditions.size();
        int temp=list_seat_conditions.get(row).get(column).intValue();
//        if(temp==0) {
//            for (int i = 0; i < rowSize; i++) {
//                int columnSize = list_seat_conditions.get(row).size();
//
//                for (int j = 0; j < columnSize; j++) {
//                    int temp1 = list_seat_conditions.get(row).get(j).intValue();
//
//                    if(column>j){
//                        int tempcount=0;
//                        for (int z = 0; z < column; z++) {
//                            int temp2 = list_seat_conditions.get(row).get(z).intValue();
//                            if(temp2==0){
//                                tempcount++;
//                            }
//                        }
//                        a[1]=column-tempcount;
//                    }else {
//                        a[1] = column + 1;
//                    }
//                }
//            }
//        }



        //计算行
        int tempcount=0;
        for (int z = 0; z < row; z++) {
            if(list_seatInfos.get(z).getDesc().equals("0"))
                tempcount++;
        }
        a[0]=row-tempcount+1;

        //计算列
        tempcount=0;
        for (int z = 0; z < column; z++) {
            int temp2 = list_seat_conditions.get(row).get(z).intValue();
            if(temp2==0){
                tempcount++;
            }
        }
        a[1]=column-tempcount+1;
        return a;
    }
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }



}
