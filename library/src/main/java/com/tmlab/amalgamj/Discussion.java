package com.tmlab.amalgamj;

import org.joou.UInteger;

import java.util.List;

public class Discussion extends Comment {

    public Asset pending_payout_value;
    public Asset total_pending_payout_value;
    public List<VoteState> active_votes;
    public List<String> replies;
    public long author_reputation;
    public Asset promoted;
}
