package com.redmancometh.arkshards.util;

public class PersonalRemoveResult extends RemoveResult
{
    private String username;

    public PersonalRemoveResult(String username, int amountLeft, boolean success)
    {
        super(amountLeft, success);
        this.setUsername(username);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
