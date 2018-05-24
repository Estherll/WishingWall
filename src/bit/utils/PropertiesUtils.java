package bit.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @param
 * @return
 * @throws Exception
 * @author ware E-mail:
 * @version create time: 20172017�?3�?9日下�?5:11:00
 */
public class PropertiesUtils {
	
	//属�?�文件的路径   
    static String filename="wx.properties";   
    /**  
    * 采用静�?�方�?  
    */   
    private static Properties props = new Properties();   
    static {   
        try {   
            props.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(filename));   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
            System.exit(-1);   
        } catch (IOException e) {          
            System.exit(-1);   
        }   
    }   
  
    /**  
    * 读取属�?�文件中相应键的�?  
    */   
    public static String getKeyValue(String key) {   
        return props.getProperty(key);   
    }   

}
