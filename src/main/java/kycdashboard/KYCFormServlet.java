package kycdashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
                 maxFileSize = 5 * 1024 * 1024,    // 5MB
                 maxRequestSize = 20 * 1024 * 1024) // 20MB
public class KYCFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DB settings
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/kyc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();

    	
        // Read form fields
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        
     // Email regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        // Phone regex (10 digits)
        String phoneRegex = "^[0-9]{10}$";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePattern.matcher(phone);

        if (!emailMatcher.matches()) {
            out.println("<h3 style='color:red;'>Invalid Email Format</h3>");
            return;
        }

        if (!phoneMatcher.matches()) {
            out.println("<h3 style='color:red;'>Invalid Phone Number</h3>");
            return;
        }

        Part filePart = request.getPart("idproof");
        String submittedFileName = getFileName(filePart);

        // Create uploads folder inside webapp (if not exists)
        String uploadsDir = "uploads";
        String applicationPath = request.getServletContext().getRealPath("");
        File uploadFolder = new File(applicationPath, uploadsDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        // Make unique filename to avoid collision: timestamp_originalname
        String savedFileName = System.currentTimeMillis() + "_" + submittedFileName;
        File file = new File(uploadFolder, savedFileName);

        try (InputStream is = filePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(file)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        // Save record to DB: store relative path (uploads/filename)
        String relativePath = uploadsDir + "/" + savedFileName;

        // Insert into DB
        String insertSql = "INSERT INTO kyc_applications (name, email, phone, id_proof_path, status) VALUES (?, ?, ?, ?, 'Pending')";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 PreparedStatement pst = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, phone);
                pst.setString(4, relativePath);
                int affected = pst.executeUpdate();

                if (affected > 0) {
                    try (ResultSet keys = pst.getGeneratedKeys()) {
                        if (keys.next()) {
                            int appId = keys.getInt(1);
                            response.setContentType("text/html");
                            response.getWriter().println("<html><head><link rel='stylesheet' href='style.css'></head><body>");
                            response.getWriter().println("<div class='container'><h2>KYC Submitted</h2>");
                            response.getWriter().println("<p>Application ID: <strong>" + appId + "</strong></p>");
                            response.getWriter().println("<p>Status: Pending</p>");
                            response.getWriter().println("<a href='index.jsp' class='link-btn'>Back to Home</a>");
                            response.getWriter().println("</div></body></html>");
                            return;
                        }
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save application.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    // Helper to get submitted file name
    private String getFileName(Part part) {
        String cd = part.getHeader("content-disposition");
        if (cd == null) return "unknown";
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String fileName = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                return new File(fileName).getName();
            }
        }
        return "unknown";
    }
}
