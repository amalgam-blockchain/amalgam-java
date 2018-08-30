package com.tmlab.amalgamj;

public class AccountRecoveryRequest {

    public long id;
    public String account_to_recover;
    public Authority new_owner_authority;
    public TimePointSec expires;
}
