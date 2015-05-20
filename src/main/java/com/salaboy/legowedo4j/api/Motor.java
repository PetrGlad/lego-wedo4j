package com.salaboy.legowedo4j.api;

/**
 * @author salaboy
 */
public interface Motor {
    enum Direction {
        FORWARD, BACKWARD, CONTINUE, REVERSE
    };

    void start(int speed, Direction dir);
    
    void stop();
    
    boolean isRunning();
}
