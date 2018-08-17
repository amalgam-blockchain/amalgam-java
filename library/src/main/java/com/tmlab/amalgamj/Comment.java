package com.tmlab.amalgamj;

import org.joou.UByte;
import org.joou.UInteger;
import org.joou.UShort;

import java.math.BigInteger;

public class Comment {

    public long id;
    public String category;
    public String parent_author;
    public String parent_permlink;
    public String author;
    public String permlink;
    public String title;
    public String body;
    public String json_metadata;
    public TimePointSec last_update;
    public TimePointSec created;
    public TimePointSec active;
    public TimePointSec last_payout;
    public UByte depth;
    public UInteger children;
    public long net_rshares;
    public long abs_rshares;
    public long vote_rshares;
    public long children_abs_rshares;
    public TimePointSec cashout_time;
    public TimePointSec max_cashout_time;
    public BigInteger total_vote_weight;
    public UShort reward_weight;
    public Asset total_payout_value;
    public Asset curator_payout_value;
    public long author_rewards;
    public UInteger net_votes;
    public long root_comment;
    public Asset max_accepted_payout;
    public UShort percent_amalgam_dollars;
    public boolean allow_replies;
    public boolean allow_votes;
    public boolean allow_curation_rewards;
}
