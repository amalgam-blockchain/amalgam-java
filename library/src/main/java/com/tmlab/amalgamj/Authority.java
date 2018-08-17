package com.tmlab.amalgamj;

import org.joou.UInteger;
import org.joou.UShort;

public class Authority {

    public UInteger weight_threshold;
    public ArrayHashMap<String, UShort> account_auths = new ArrayHashMap<>();
    public ArrayHashMap<PublicKey, UShort> key_auths = new ArrayHashMap<>();
}
