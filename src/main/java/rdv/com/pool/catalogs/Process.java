/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.catalogs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author rbn
 */
public abstract class Process {

    private static final ConcurrentHashMap<Integer, Status> statusMap = init();

    public enum Status {
        WAITING,
        BUSY,
        IDLE,
        FREE;
    }

    private static ConcurrentHashMap<Integer, Status> init() {
        ConcurrentHashMap<Integer, Status> sts = new ConcurrentHashMap<>();
        for (Status st : Status.values()) {
            sts.put(st.ordinal(), st);
        }
        return sts;
    }

    public static Status getNext(Status status) {
        if (statusMap.contains(status.ordinal() + 1)) {
            return statusMap.get(status.ordinal() + 1);
        }
        return null;
    }

}
