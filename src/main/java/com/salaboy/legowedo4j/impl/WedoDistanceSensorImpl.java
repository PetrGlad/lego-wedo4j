package com.salaboy.legowedo4j.impl;

import com.salaboy.legowedo4j.api.BlockManager;
import com.salaboy.legowedo4j.api.DistanceSensor;

/**
 * @author salaboy
 */
public class WedoDistanceSensorImpl implements DistanceSensor {

    private final BlockManager manager;

    public WedoDistanceSensorImpl(BlockManager manager) {
        this.manager = manager;
    }

    /*
     * Distance from 148 to 1, where 1 is extremely close and 148 is Infinite... 
     * 147 aprox 20 cm
     */
    public synchronized int readDistance() {
        byte[] buff = new byte[8];
        int n = manager.read(buff);
        if (n != 8) {
            return -1;
        }
        if ((buff[5] & 0xff) == 176 || (buff[5] & 0xff) == 177 || (buff[5] & 0xff) == 178 || (buff[5] & 0xff) == 179) {
            return (buff[4] & 0xff) - 69;
        } else {
            return -1;
        }
    }
}
