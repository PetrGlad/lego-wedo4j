package com.salaboy.legowedo4j.api;

import com.codeminders.hidapi.HIDDevice;

/**
 * @author salaboy
 */
public interface BlockManager {
    HIDDevice getDevice();
    
    void write(byte[] data);
    
    int read(byte[] buff);
}
