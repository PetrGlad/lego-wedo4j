package com.salaboy.legowedo4j.impl;

import com.salaboy.legowedo4j.api.BlockManager;
import com.salaboy.legowedo4j.api.Tilt;
import com.salaboy.legowedo4j.api.TiltSensor;

import javax.inject.Inject;

/**
 * @author salaboy
 */
public class WedoTiltSensorImpl implements TiltSensor {

    @Inject
    private BlockManager manager;

    public WedoTiltSensorImpl() {
    }

    public synchronized Tilt readTilt() {
        int tilt = getTilt();
        if (tilt > 10 && tilt < 40) {
            return Tilt.BACK;
        } else if (tilt > 60 && tilt < 90) {
            return Tilt.RIGHT;
        } else if (tilt > 170 && tilt < 190) {
            return Tilt.FORWARD;
        } else if (tilt > 220 && tilt < 240) {
            return Tilt.LEFT;
        } else if (tilt > 120 && tilt < 140) {
            return Tilt.NO_TILT;
        } else {
            return Tilt.NO_TILT;
        }
    }

    private int getTilt() {
        byte[] buff = new byte[8];
        int n = manager.read(buff);
        if (n != 8) {
            throw new IllegalStateException("Wrong data length " + n);
        }
        if ((buff[3] & 0xff) == 38 || (buff[3] & 0xff) == 39) {
            return (buff[2] & 0xff);
        } else {
            return -1;
        }
    }
}
