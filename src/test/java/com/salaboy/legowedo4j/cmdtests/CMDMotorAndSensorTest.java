package com.salaboy.legowedo4j.cmdtests;

import com.salaboy.legowedo4j.api.DistanceSensor;
import com.salaboy.legowedo4j.api.Motor;
import com.salaboy.legowedo4j.impl.WeDoBlockManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.salaboy.legowedo4j.impl.WedoDistanceSensorImpl;
import com.salaboy.legowedo4j.impl.WedoMotorImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * @author salaboy
 */
public class CMDMotorAndSensorTest {

    static boolean readSensors = true;
    static long defaultLatency = 100;

    public static void main(String[] args) throws Exception {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("t", true, "sensors latency");
        options.addOption("arch", true, "architecture");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);

        String sensorLatency = cmd.getOptionValue("t");
        if (sensorLatency == null) {
            System.out.println(" The Default Latency will be used: " + defaultLatency);
        } else {
            System.out.println(" The Latency will be set to: " + sensorLatency);
            defaultLatency = new Long(sensorLatency);
        }
        String arch = cmd.getOptionValue("arch");
        if (arch == null) {
            System.out.println(" The Default Arch will be used: arm7");
        } else {
            System.out.println(" The Arch will be set to: " + arch);
            WeDoBlockManager.arch = arch;
        }

        System.out.println("Starting Motor and Sensor CMD Test ...");

        final Motor motor = new WedoMotorImpl(WeDoBlockManager.INSTANCE);
        final DistanceSensor distanceSensor = new WedoDistanceSensorImpl(WeDoBlockManager.INSTANCE);

        final Thread t = new Thread() {
            @Override
            public void run() {
                while (readSensors) {
                    System.out.println("Starting Motor ...");
                    motor.start(120, Motor.Direction.FORWARD);
                    try {
                        Thread.sleep(defaultLatency);
                        int readDistance = distanceSensor.readDistance();
                        System.out.println("Distance = " + readDistance);
                        Thread.sleep(defaultLatency);
                        System.out.println("Stopping Motor ...");
                        motor.stop();
                        Thread.sleep(defaultLatency);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CMDMotorAndSensorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        t.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook is running !");
                readSensors = false;
                motor.stop();
            }
        });
    }
}
