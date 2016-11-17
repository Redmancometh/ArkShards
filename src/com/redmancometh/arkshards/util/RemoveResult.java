package com.redmancometh.arkshards.util;

public class RemoveResult
{
    private String userName;
    private int amountLeft;
    private boolean success;

    public RemoveResult(int amountLeft, boolean success)
    {
        this.amountLeft = amountLeft;
        this.success = success;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getAmountLeft()
    {
        return amountLeft;
    }

    public void setAmountLeft(int amountLeft)
    {
        this.amountLeft = amountLeft;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

}
