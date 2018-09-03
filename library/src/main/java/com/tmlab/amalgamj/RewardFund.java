package com.tmlab.amalgamj;

import org.joou.UShort;

import java.math.BigInteger;

public class RewardFund {
    public long id;
    public String name;
    public Asset reward_balance;
    public BigInteger recent_claims;
    public TimePointSec last_update;
    public BigInteger content_constant;
    public UShort percent_curation_rewards;
    public UShort percent_content_rewards;
    public String author_reward_curve;
    public String curation_reward_curve;
}
