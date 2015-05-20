package com.salaboy.legowedo4j.impl;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceNotFoundException;
import com.codeminders.hidapi.HIDManager;
import com.salaboy.legowedo4j.api.BlockManager;

import java.io.IOException;

/**
 * @author salaboy
 */
public class WeDoBlockManager implements BlockManager {

    final static int VENDOR_ID = 1684;
    final static int PRODUCT_ID = 3;

    private HIDDevice dev;
    public static String arch = "pc";

    static {
        if (arch.equals("pc")) {
            ClassPathLibraryLoader.loadNativeHIDLibrary();
        } else if (arch.equals("arm7")) {
            System.loadLibrary("hidapi-jni");
        }
    }

    public static final BlockManager INSTANCE = new WeDoBlockManager();

    public WeDoBlockManager() {
        try {
            dev = HIDManager.getInstance().openById(VENDOR_ID, PRODUCT_ID, null);
        } catch (HIDDeviceNotFoundException ex) {
            throw new IllegalStateException("No device found", ex);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot open device", ex);
        }
    }

    public HIDDevice getDevice() {
        return this.dev;
    }

    public synchronized void write(byte[] data) {
        try {
            dev.write(data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized int read(byte[] buff) {
        try {
            return dev.read(buff);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
