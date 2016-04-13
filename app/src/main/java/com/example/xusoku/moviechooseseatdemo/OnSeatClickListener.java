package com.example.xusoku.moviechooseseatdemo;


public interface OnSeatClickListener
{
    boolean cancelSelect(int paramInt1, int paramInt2, boolean paramBoolean);

    boolean select(int paramInt1, int paramInt2, boolean paramBoolean);
}