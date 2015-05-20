package com.salaboy.legowedo4j.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    public static void printBuffer(byte[] buff) {
        assert buff.length >= 0;
        final StringBuilder sb = new StringBuilder("##" + (buff[0] & 0xff));
        for (int i = 1; i < buff.length; i++)
            sb.append(",").append(buff[i] & 0xff);
        System.out.println(sb);
    }

    public static void pause(long msec) {
        assert msec >= 0;
        try {
            Thread.sleep(msec);
        } catch (InterruptedException ex) {
            Logger.getLogger(WedoMotorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
