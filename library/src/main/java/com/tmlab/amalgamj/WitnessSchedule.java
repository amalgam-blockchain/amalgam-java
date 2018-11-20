package com.tmlab.amalgamj;

import org.joou.UByte;
import org.joou.UInteger;

import java.math.BigInteger;
import java.util.List;

public class WitnessSchedule {

    public long id;
    public BigInteger current_virtual_time;
    public UInteger next_shuffle_block_num;
    public List<String> current_shuffled_witnesses;
    public UByte num_scheduled_witnesses;
    public UByte elected_weight;
    public UByte timeshare_weight;
    public UInteger witness_pay_normalization_factor;
    public ChainProperties median_props;
    public String majority_version;
    public UByte max_voted_witnesses;
    public UByte max_runner_witnesses;
    public UByte hardfork_required_witnesses;
}
