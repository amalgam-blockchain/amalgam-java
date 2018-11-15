package com.tmlab.amalgamj;

import org.joou.UInteger;

import java.util.List;

public class HardforkProperties {

    public long id;
    public List<TimePointSec> processed_hardforks;
    public UInteger last_hardfork;
    public String current_hardfork_version;
    public String next_hardfork;
    public TimePointSec next_hardfork_time;
}
