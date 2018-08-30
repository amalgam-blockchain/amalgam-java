package com.tmlab.amalgamj;

public class BlockHeader {

    public Ripemd160 previous;
    public TimePointSec timestamp;
    public String witness;
    public Ripemd160 transaction_merkle_root;
}
