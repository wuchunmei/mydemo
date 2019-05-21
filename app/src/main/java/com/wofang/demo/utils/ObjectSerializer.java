package com.wofang.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer {
 
	 public static byte[] serializeObject(Serializable obj) throws IOException {
	        if (obj == null) return null;
	        ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
	        ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
	        objStream.writeObject(obj);
	        objStream.close();
	        byte[] data=serialObj.toByteArray();
	        serialObj.close();
	        return data;
	    }
	    
	    public static Object deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException {
	        if (bytes == null || bytes.length == 0) return null;
	        ByteArrayInputStream serialObj = new ByteArrayInputStream(bytes);
	        ObjectInputStream objStream = new ObjectInputStream(serialObj);
	        Object object=objStream.readObject();
	        objStream.close();
	        return object;
	    }
	    
    public static String serialize(Serializable obj) throws IOException {
        byte[] data=serializeObject(obj);
        if (data!=null){
        	return encodeBytes(data);
        }
        return null;
    }
    
    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        if (str == null || str.length() == 0) return null;
        byte[] bytes=decodeBytes(str);
        return deserializeObject(bytes);
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();
    
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + 'a'));
            strBuf.append((char) (((bytes[i]) & 0xF) + 'a'));
        }
        
        return strBuf.toString();
    }
    
    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i+=2) {
            char c = str.charAt(i);
            bytes[i/2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i+1);
            bytes[i/2] += (c - 'a');
        }
        return bytes;
    }
}