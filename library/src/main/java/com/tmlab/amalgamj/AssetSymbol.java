package com.tmlab.amalgamj;

public class AssetSymbol {

    public String name;
    public int precision;

    public final static AssetSymbol AML = new AssetSymbol("AML", 3);
    public final static AssetSymbol AMLV = new AssetSymbol("AMLV", 6);
    public final static AssetSymbol AMLD = new AssetSymbol("AMLD", 3);

    private final static AssetSymbol[] symbols = { AML, AMLV, AMLD };

    public AssetSymbol(String name, int precision) {
        this.name = name;
        this.precision = precision;
    }

    public static AssetSymbol fromString(String name) throws Exception {
        for (AssetSymbol symbol : symbols) {
            if (symbol.name.equals(name)) {
                return symbol;
            }
        }
        throw new Exception("Unknown symbol: " + name);
    }

    public boolean equals(AssetSymbol symbol) {
        if (symbol == null) {
            return false;
        }
        if (symbol == this) {
            return true;
        }
        return name.equals(symbol.name);
    }
}
