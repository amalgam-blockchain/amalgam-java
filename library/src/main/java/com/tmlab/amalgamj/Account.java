package com.tmlab.amalgamj;

import org.joou.UByte;
import org.joou.UInteger;
import org.joou.UShort;

import java.math.BigInteger;
import java.util.List;

public class Account {

    public long id;
    public String name;
    public Authority owner;
    public Authority active;
    public Authority posting;
    public PublicKey memo_key;
    public String json_metadata;
    public String proxy;
    public TimePointSec last_owner_update;
    public TimePointSec last_account_update;
    public TimePointSec created;
    public boolean mined;
    public String recovery_account;
    public TimePointSec last_account_recovery;
    public boolean can_vote;
    public Asset balance;
    public Asset savings_balance;
    public Asset abd_balance;
    public BigInteger abd_seconds;
    public TimePointSec abd_seconds_last_update;
    public TimePointSec abd_last_interest_payment;
    public Asset savings_abd_balance;
    public BigInteger savings_abd_seconds;
    public TimePointSec savings_abd_seconds_last_update;
    public TimePointSec savings_abd_last_interest_payment;
    public UByte savings_withdraw_requests;
    public Asset vesting_shares;
    public Asset delegated_vesting_shares;
    public Asset received_vesting_shares;
    public Asset vesting_withdraw_rate;
    public TimePointSec next_vesting_withdrawal;
    public long withdrawn;
    public long to_withdraw;
    public UShort withdraw_routes;
    public List<Long> proxied_vsf_votes;
    public UShort witnesses_voted_for;
    public long average_bandwidth;
    public long lifetime_bandwidth;
    public TimePointSec last_bandwidth_update;
    public long average_market_bandwidth;
    public long lifetime_market_bandwidth;
    public TimePointSec last_market_bandwidth_update;
}
