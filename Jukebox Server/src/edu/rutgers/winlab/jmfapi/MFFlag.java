package edu.rutgers.winlab.jmfapi;

/**
 *
 * File: JMFAPI.java
 * Author: Francesco Bronzino
 *
 * Description: MobilityFirst flags used to request features and service Ids to the host protocol stack
 *
 */

public class MFFlag {

    static public int MF_MHOME =  0x00000001;

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    boolean isMFFlagSet(int f){
        return (value & f) != 0;
    }

    void setMFFlag(int f){
        value = value | f;
    }

    boolean isMFFlagSet(MFFlag f){
        return (value & f.getValue()) != 0;
    }

    void setMFFlag(MFFlag f){
        value = value | f.getValue();
    }

    void setMultihoming(){
        value = value | MF_MHOME;
    }

    boolean isMultihomingSet(){
        return (value & MF_MHOME) != 0;
    }

}
