package com.salaboy.legowedo4j.api;

/**
 * @author salaboy
 */
public interface Motor {

    enum Direction {
        FORWARD, BACKWARD
    };

    void forward(int speed, long millisec);

    void backward(int speed, long millisec);

    void start(int speed, Direction dir);
    
    void stop();
    
    boolean isRunning();
}
