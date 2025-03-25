import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * Loads the order database using code.
 */
public class LoadData
{
	public static void main(String[] argv) throws Exception
	{
		loadData();
	}
	
	public static void loadData() throws Exception
	{		
		String url = "jdbc:sqlserver://sql04.ok.ubc.ca:1433;DatabaseName=*******;";
		String uid = "*****";
		String pw = "*******";
		
		System.out.println("Connecting to database.");

		Connection con = DriverManager.getConnection(url, uid, pw);
				
		String fileName = "data/orderdb_sql.ddl";
		
	    try
	    {
	        // Create statement
	        Statement stmt = con.createStatement();
	        
	        Scanner scanner = new Scanner(new File(fileName));
	        // Read commands separated by ;
	        scanner.useDelimiter(";");
	        while (scanner.hasNext())
	        {
	            String command = scanner.next();
	            if (command.trim().equals(""))
	                continue;
	            try
	            {
	            	stmt.execute(command);
	            }
	            catch (Exception e)
	            {	// Keep running on exception.  This is mostly for DROP TABLE if table does not exist.
	            	System.out.println(e);
	            }
	        }	 
	        try
            {
	        	BufferedImage image = ImageIO.read(new File("WebContent/img/download.jpg"));
	        	ByteArrayOutputStream b = new ByteArrayOutputStream();
	        	ImageIO.write(image, "png", b);
	        	b.flush();
	        	byte[] byteImage = b.toByteArray();
	        	b.close();
	        	PreparedStatement pstmt = con.prepareStatement("UPDATE Product SET productImage = ? WHERE ProductId = 6;");
	        	ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);
	        	pstmt.setBinaryStream(1, bais, byteImage.length);
	        	pstmt.executeUpdate();
	        	pstmt.close();
            }
            catch (Exception e)
            {	// Keep running on exception.  This is mostly for DROP TABLE if table does not exist.
            	System.out.println(e);
            }

	        scanner.close();
	        
	        System.out.println("Database loaded.");
	    }
	    catch (Exception e)
	    {
	        System.out.println(e);
	    }   
	}
}
