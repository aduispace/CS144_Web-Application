import java.awt.Rectangle;
import java.io.*;
import java.security.*;

public class ComputeSHA {

    public static String getHash(String filename) throws Exception
    {
       
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];

        MessageDigest hasher = MessageDigest.getInstance("SHA-1");

        int Counts;
        do {
            Counts = fis.read(buffer);
            if (Counts > 0)
            {
                hasher.update(buffer, 0, Counts);
            }
        } while (Counts != -1);
               
        fis.close(); 
        byte[] b = hasher.digest();

        StringBuffer result = new StringBuffer();
        for (int i=0; i < b.length; i++)
        {
           result.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
        }
        
        return result.toString();
    }

    public static void main(String args[])
    {
        try 
        {
            String inFile = "";
            inFile = args[0];
            System.out.println(getHash(inFile));        
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }
}