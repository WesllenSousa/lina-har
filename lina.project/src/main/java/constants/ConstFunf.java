/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author Wesllen Sousa
 */
public class ConstFunf {
        
    public static String PROBE_PATH = "edu.mit.media.funf.probe.builtin.";
    public static String PACKAGE_PROBE = "database.funf.json";
      
    public static String QUERY_DISTINCT_PROBE = "SELECT DISTINCT probe FROM data ORDER BY probe";
    public static String QUERY_ALL_DATA = "SELECT * FROM data";
    public static String QUERY_BY_PROBE = "SELECT * FROM data WHERE ";
    public static String QUERY_BY_PROBE_TIMESTAMP = "SELECT * FROM data WHERE timestamp BETWEEN # AND $ ";
    
}
