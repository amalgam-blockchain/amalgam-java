package com.tmlab.amalgamj;

import org.joou.UInteger;
import org.joou.ULong;
import org.joou.UShort;

public class AppliedOperation {

    public Ripemd160 trx_id;
    public UInteger block;
    public UInteger trx_in_block;
    public UShort op_in_trx;
    public ULong virtual_op;
    public TimePointSec timestamp;
    public Operation op;
}
