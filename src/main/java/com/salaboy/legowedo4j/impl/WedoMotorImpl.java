package com.salaboy.legowedo4j.impl;

import com.salaboy.legowedo4j.api.BlockManager;
import com.salaboy.legowedo4j.api.Motor;

import javax.inject.Inject;

/**
 * @author salaboy
 */
public class WedoMotorImpl implements Motor {
    @Inject
    BlockManager manager;

    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    private byte[] freshBuffer() {
        byte[] buff = new byte[8];
        final int count = manager.read(buff);
        Util.printBuffer(buff);
        if (count == 8) {
            return new byte[]{0, 64, 0, 0, buff[3], buff[4], buff[5], buff[6], buff[7]};
        } else {
            return new byte[]{0, 64, 0, 0, 0, 0, 0, 0, 0};
        }
    }

    private byte[] makeCommand(int direction, int power) {
        byte[] buffer = freshBuffer();
        buffer[2] = (byte) direction;
        buffer[3] = (byte) power;
        return buffer;
    }

    private synchronized void doCommand(int direction, int power) {
        manager.write(makeCommand(direction, power));
    }

    public synchronized void forward(int speed, long millisec) {
        doCommand(60, -speed);
        this.running = true;
        Util.pause(millisec);
        stop();
    }

    public synchronized void backward(int speed, long millisec) {
        doCommand(-60, speed);
        this.running = true;
        Util.pause(millisec);
        stop();
    }

    public synchronized void stop() {
        doCommand(0, 0);
        this.running = false;
    }

    public synchronized void start(int speed, Direction dir) {
        switch (dir) {
            case BACKWARD: {
                doCommand(64, speed);
                break;
            }
            case FORWARD: {
                doCommand(64, -speed);
                break;
            }
        }
        this.running = true;
    }
}
