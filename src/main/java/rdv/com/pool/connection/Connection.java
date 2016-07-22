/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.connection;

/**
 *
 * @author rbn
 */
public interface Connection {
    
    void retrieve(StringBuilder msg);
    
    void clear();
    
    int getId();
}
