package com.salaboy.legowedo4j.api;

import com.codeminders.hidapi.HIDDevice;

/**
 * @author salaboy
 */
public interface BlockManager {

    int VENDOR_ID = 1684;
    int PRODUCT_ID = 3;
    
    HIDDevice getDevice();
    
    void write(byte[] data);
    
    int read(byte[] buff);
}
