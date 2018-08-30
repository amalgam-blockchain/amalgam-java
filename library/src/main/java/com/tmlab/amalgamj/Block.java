package com.tmlab.amalgamj;

import java.util.List;

public class Block extends SignedBlock {

    public Ripemd160 block_id;
    public PublicKey signing_key;
    public List<Ripemd160> transaction_ids;
}
