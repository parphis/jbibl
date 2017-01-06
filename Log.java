
/**
 * Log the activities and the failures during the application session.
 * 
 * @author Istvan Vig
 * @version 10th or April 2015
 */

import java.io.*;
import java.util.Date;

public class Log
{
    private static void add(String level, String msg) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("jbibl.log", true)))) {
           out.println(new Date().toString() + "\t\t" + level + "\t\t" + msg);
        }catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public static void info(String msg) {
        add("::INFORMATION::", msg);
    }
    
    public static void warning(String msg) {
        add("%%WARNING%%", msg);
    }
    
    public static void error(String msg) {
        add("!!ERROR!!", msg);
    }
}
