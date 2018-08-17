package com.tmlab.amalgamj;

import org.joou.UInteger;

import java.util.List;

public class Discussion extends Comment {

    public String url;
    public String root_title;
    public Asset pending_payout_value;
    public Asset total_pending_payout_value;
    public List<VoteState> active_votes;
    public List<String> replies;
    public long author_reputation;
    public Asset promoted;
    public UInteger body_length;
    public List<String> reblogged_by;
    public String first_reblogged_by;
    public TimePointSec first_reblogged_on;
}
