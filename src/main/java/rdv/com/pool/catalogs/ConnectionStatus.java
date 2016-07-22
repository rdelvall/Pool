/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.catalogs;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rbn
 */
public enum ConnectionStatus {
    INITIALYZING,
    BUSY,
    IDLE,
    FREE;
    private Map<Integer, ConnectionStatus> map;

    private void init() {
        map = new HashMap<Integer, ConnectionStatus>();
        for (ConnectionStatus status : ConnectionStatus.values()) {
            map.put(status.ordinal(), status);
        }
    }

    /**
     *
     * @return
     */
    public ConnectionStatus getNext() {
        if (map == null) {
            init();
        }
        if (map.containsKey(this.ordinal() + 1)) {
            return map.get(this.ordinal() + 1);
        }
        return null;
    }
    
}
