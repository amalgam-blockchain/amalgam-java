package com.tmlab.amalgamj;

import org.joou.UInteger;

public class LimitOrder {

    public long id;
    public TimePointSec created;
    public TimePointSec expiration;
    public String seller;
    public UInteger orderid;
    public long for_sale;
    public Price sell_price;
}
