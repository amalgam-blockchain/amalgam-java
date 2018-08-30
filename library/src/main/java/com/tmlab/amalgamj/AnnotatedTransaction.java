package com.tmlab.amalgamj;

import org.joou.UInteger;

public class AnnotatedTransaction extends Transaction {

    public Ripemd160 transaction_id;
    public UInteger block_num;
    public UInteger transaction_num;
}
