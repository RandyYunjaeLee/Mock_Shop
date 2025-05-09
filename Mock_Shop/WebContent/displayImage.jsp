<%@ page trimDirectiveWhitespaces="true" import="java.sql.*,java.io.*" %><%@ include file="jdbc.jsp" %><%

// Indicate that we are sending a JPG picture
response.setContentType("image/jpeg");  

// Get the image id
String id = request.getParameter("id");

if (id == null)
	return;

int idVal = -1;
try{
	idVal = Integer.parseInt(id);
}
catch(Exception e)
{	out.println("Invalid image id: "+id+" Error: "+e);
	return; 
}

// TODO: Modify SQL to retrieve productImage given productId
String sql = "SELECT productImage FROM product WHERE productId = (?)";
String url = "jdbc:sqlserver://Somewebsite.com:1433;DatabaseName=db_fill-in;";
String uid = "fill-in";
String pw = "fill-in";
try ( Connection con = DriverManager.getConnection(url, uid, pw);)
{
	
	PreparedStatement stmt = con.prepareStatement(sql);
	stmt.setInt(1,idVal);
	ResultSet rst = stmt.executeQuery();		

	int BUFFER_SIZE = 10000;
	byte[] data = new byte[BUFFER_SIZE];

	rst.next();
	
		// Copy stream of bytes from database to output stream for JSP/Servlet
		InputStream istream = rst.getBinaryStream(1);
		OutputStream ostream = response.getOutputStream();

		int count;
		while ( (count = istream.read(data, 0, BUFFER_SIZE)) != -1)
			ostream.write(data, 0, count);

		ostream.flush();
		istream.close();
		con.close();
	
} 
catch (SQLException ex) {
	out.println(ex);
}
%>