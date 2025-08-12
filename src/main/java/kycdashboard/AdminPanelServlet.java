package kycdashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

public class AdminPanelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/kyc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action"); // approve or reject
        String idStr = request.getParameter("applicationId");

        if (idStr == null || action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters.");
            return;
        }

        int appId;
        try {
            appId = Integer.parseInt(idStr);
        } catch (NumberFormatException nfe) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid application ID.");
            return;
        }

        String status = action.equalsIgnoreCase("approve") ? "Approved" : "Rejected";

        String updateSql = "UPDATE kyc_applications SET status = ? WHERE application_id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 PreparedStatement pst = con.prepareStatement(updateSql)) {

                pst.setString(1, status);
                pst.setInt(2, appId);
                int updated = pst.executeUpdate();

                if (updated > 0) {
                    // After updating redirect back to admin.jsp to see changes
                    response.sendRedirect("admin.jsp");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Application not found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
