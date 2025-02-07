package Request;

import java.security.MessageDigest;

public class PasswordUtils {
	
	 // Phương thức hash mật khẩu
    public static String hashPassword(String password) {

       try {
		
    	   MessageDigest md = MessageDigest.getInstance("SHA-1");
    	   md.update(password.getBytes());
    	   byte [] rbt = md.digest();
    	   StringBuilder sb = new StringBuilder();
    	   
    	   for (byte b : rbt) {
    		   sb.append(String.format("%02x", b));
    	   }
    	   
    	   return sb.toString();
    	   
		} catch (Exception e) {
			e.printStackTrace();
		}
       return null;
       
       
       
    }
}
