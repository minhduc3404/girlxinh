package com.max.app.girlxinh.bus;

import com.squareup.otto.Bus;

/**
 * Created by Forev on 4/2/2016.
 */
public class BusProvider {
    private static Bus BUS = new Bus();

    public static Bus getInstance()
    {
        return BUS;
    }

    public BusProvider(){}
}
