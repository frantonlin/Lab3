package com.frantonlin.scavengerhunt;

import java.util.HashMap;

/**
 * Interface for callbacks
 * Created by Franton on 10/1/15
 */
public interface InfoCallback {
    void callback(boolean success, HashMap<String, String> clueInfo);
}
