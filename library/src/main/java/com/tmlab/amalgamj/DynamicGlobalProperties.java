package com.tmlab.amalgamj;

import org.joou.UByte;
import org.joou.UInteger;
import org.joou.UShort;

import java.math.BigInteger;

public class DynamicGlobalProperties {

    public long id;
    public UInteger head_block_number;
    public Ripemd160 head_block_id;
    public TimePointSec time;
    public String current_witness;
    public Asset virtual_supply;
    public Asset current_supply;
    public Asset confidential_supply;
    public Asset current_abd_supply;
    public Asset confidential_abd_supply;
    public Asset total_vesting_fund_amalgam;
    public Asset total_vesting_shares;
    public UShort abd_interest_rate;
    public UShort abd_print_rate;
    public UInteger maximum_block_size;
    public BigInteger current_aslot;
    public BigInteger recent_slots_filled;
    public UByte participation_count;
    public UInteger last_irreversible_block_num;
    public UInteger delegation_return_period;
    public UInteger abd_stop_percent;
    public UInteger abd_start_percent;

    public Price getVestingSharePrice() {
        if ((total_vesting_fund_amalgam.amount == 0) || (total_vesting_shares.amount == 0)) {
            return new Price(new Asset(1000, AssetSymbol.AML), new Asset(1000000, AssetSymbol.AMLV));
        }
        return new Price(total_vesting_shares, total_vesting_fund_amalgam);
    }
}
