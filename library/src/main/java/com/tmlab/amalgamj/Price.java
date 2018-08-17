package com.tmlab.amalgamj;

public class Price {

    public Asset base;
    public Asset quote;

    public Price(Asset base, Asset quote) {
        this.base = base;
        this.quote = quote;
    }

    public boolean isNull() {
        return (base.amount == 0) && (quote.amount == 0);
    }
}
