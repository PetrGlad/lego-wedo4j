package com.salaboy.legowedo4j;

import com.salaboy.legowedo4j.api.DistanceSensor;
import com.salaboy.legowedo4j.api.Motor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import com.salaboy.legowedo4j.impl.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class WeDo4jMainTest {

    @Deployment()
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "we-do.jar")
                .addPackage("com.salaboy.legowedo4j.api")
                .addPackage("com.salaboy.legowedo4j.impl")
                .addPackage("com.codeminders.hidapi") // hidapi
                .addAsManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"));
    }

    @Inject
    private DistanceSensor distanceSensor;

    @Inject
    private Motor motor;

    @Test
    public void initialTest() throws InterruptedException {
        final ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleWithFixedDelay(() -> {
                    System.out.println("Distance: " + distanceSensor.readDistance());
                    motor.start(126, Motor.Direction.FORWARD);
                    Util.pause(1000);
                    motor.stop();
                },
                0, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
        exec.shutdown();
        motor.stop();
    }
}
