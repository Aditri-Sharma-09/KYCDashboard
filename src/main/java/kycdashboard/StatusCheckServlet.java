package kycdashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class StatusCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/kyc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appIdStr = request.getParameter("applicationId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (appIdStr == null || appIdStr.isBlank()) {
            out.println("<p>Provide an application ID.</p>");
            return;
        }

        try {
            int appId = Integer.parseInt(appIdStr);

            String selectSql = "SELECT name, status FROM kyc_applications WHERE application_id = ?";
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 PreparedStatement pst = con.prepareStatement(selectSql)) {

                pst.setInt(1, appId);
                try (ResultSet rs = pst.executeQuery()) {
                    out.println("<html><head><link rel='stylesheet' href='style.css'></head><body>");
                    out.println("<div class='container'>");
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String status = rs.getString("status");
                        out.println("<h2>KYC Status</h2>");
                        out.println("<p><strong>Application ID:</strong> " + appId + "</p>");
                        out.println("<p><strong>Name:</strong> " + name + "</p>");
                        out.println("<p><strong>Status:</strong> <span class='status-badge status-" + status.toLowerCase() + "'>" + status + "</span></p>");
                    } else {
                        out.println("<h2>No record found for Application ID: " + appId + "</h2>");
                    }
                    out.println("<a href='index.jsp' class='link-btn'>Back to Home</a>");
                    out.println("</div></body></html>");
                }
            }
        } catch (NumberFormatException nfe) {
            out.println("<p>Application ID must be a number.</p>");
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<p>Error checking status: " + e.getMessage() + "</p>");
        }
    }
}
