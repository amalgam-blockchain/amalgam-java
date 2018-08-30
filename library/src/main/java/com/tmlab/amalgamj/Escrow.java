package com.tmlab.amalgamj;

import org.joou.UInteger;

public class Escrow {

    public long id;
    public UInteger escrow_id;
    public String from;
    public String to;
    public String agent;
    public TimePointSec ratification_deadline;
    public TimePointSec escrow_expiration;
    public Asset abd_balance;
    public Asset amalgam_balance;
    public Asset pending_fee;
    public boolean to_approved;
    public boolean agent_approved;
    public boolean disputed;
}
