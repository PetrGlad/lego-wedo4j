package com.salaboy.legowedo4j.cmdtests;

import com.salaboy.legowedo4j.api.DistanceSensor;
import com.salaboy.legowedo4j.impl.WeDoBlockManager;
import com.salaboy.legowedo4j.impl.WedoDistanceSensorImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author salaboy
 */
public class CMDSensorTest {

    static boolean readSensors = true;
    static long defaultLatency = 100;

    public static void main(String[] args) throws Exception {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("t", true, "sensors latency");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);

        String sensorLatency = cmd.getOptionValue("t");
        if (sensorLatency == null) {
            System.out.println(" The Default Latency will be used: " + defaultLatency);
        } else {
            System.out.println(" The Latency will be set to: " + sensorLatency);
            defaultLatency = new Long(sensorLatency);
        }

        System.out.println("Starting Sensor CMD Test ...");

        final DistanceSensor distanceSensor = new WedoDistanceSensorImpl(WeDoBlockManager.INSTANCE);

        final Thread t = new Thread() {
            @Override
            public void run() {
                while (readSensors) {
                    int readDistance = distanceSensor.readDistance();
                    System.out.println(" Distance = " + readDistance);
                    try {
                        Thread.sleep(defaultLatency);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CMDSensorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        t.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook is running.");
                readSensors = false;
            }
        });
    }
}
