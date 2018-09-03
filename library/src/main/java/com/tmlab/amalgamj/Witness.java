package com.tmlab.amalgamj;

import org.joou.UInteger;
import org.joou.ULong;

import java.math.BigInteger;

public class Witness {

    public long id;
    public String owner;
    public TimePointSec created;
    public String url;
    public UInteger total_missed;
    public ULong last_aslot;
    public ULong last_confirmed_block_num;
    public ULong pow_worker;
    public PublicKey signing_key;
    public ChainProperties props;
    public Price abd_exchange_rate;
    public TimePointSec last_abd_exchange_update;
    public long votes;
    public BigInteger virtual_last_update;
    public BigInteger virtual_position;
    public BigInteger virtual_scheduled_time;
    public String last_work;
    public String running_version;
    public String hardfork_version_vote;
    public TimePointSec hardfork_time_vote;
}
