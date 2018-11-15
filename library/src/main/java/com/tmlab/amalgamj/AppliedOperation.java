package com.tmlab.amalgamj;

import org.joou.UInteger;

public class AppliedOperation {

    public Ripemd160 trx_id;
    public UInteger block;
    public UInteger trx_in_block;
    public UInteger op_in_trx;
    public UInteger virtual_op;
    public TimePointSec timestamp;
    public Operation op;
}
