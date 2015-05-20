package com.salaboy.legowedo4j;

import com.salaboy.legowedo4j.api.DistanceSensor;
import com.salaboy.legowedo4j.api.Motor;
import com.salaboy.legowedo4j.impl.Util;
import com.salaboy.legowedo4j.impl.WeDoBlockManager;
import com.salaboy.legowedo4j.impl.WedoDistanceSensorImpl;
import com.salaboy.legowedo4j.impl.WedoMotorImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author salaboy
 */
public class WeDo4jMainTest {
    private DistanceSensor distanceSensor;

    private Motor motor;

    @Before
    public void beforeTest() {
        motor = new WedoMotorImpl(WeDoBlockManager.INSTANCE);
        distanceSensor = new WedoDistanceSensorImpl(WeDoBlockManager.INSTANCE);
    }

    @Test
    public void initialTest() throws InterruptedException {
        final Thread.UncaughtExceptionHandler ueh = (Thread t, Throwable e) -> {
            System.err.println("Exception in thread " + t);
            e.printStackTrace();
        };
        Thread.setDefaultUncaughtExceptionHandler(ueh);
        final ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1, r -> {
            final Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler(ueh);
            return thread;
        });
        exec.scheduleWithFixedDelay(() -> {
                    System.out.println("Distance: " + distanceSensor.readDistance());
                    assertFalse(motor.isRunning());
                    motor.start(126, Motor.Direction.FORWARD);
                    assertTrue(motor.isRunning());
                    Util.pause(1000);
                    motor.stop();
                    assertFalse(motor.isRunning());
                    motor.start(10, Motor.Direction.CONTINUE);
                    Util.pause(1000);
                    motor.start(60, Motor.Direction.REVERSE);
                    Util.pause(1000);
                    motor.stop();
                },
                0, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.SECONDS);
        motor.stop();
    }
}
