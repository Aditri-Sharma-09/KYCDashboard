<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Panel - KYC</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<header>Admin Panel</header>
<div class="container">
    <h2>Pending KYC Applications</h2>

    <table>
        <tr><th>Application ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Uploaded File</th><th>Action</th></tr>

        <%
            // DB connection settings (make sure match servlets)
            String url = "jdbc:mysql://localhost:3306/kyc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String pass = "root";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                PreparedStatement ps = con.prepareStatement("SELECT application_id, name, email, phone, id_proof_path, status FROM kyc_applications ORDER BY created_at DESC");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("application_id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String path = rs.getString("id_proof_path");
                    String status = rs.getString("status");
        %>
        <tr>
            <td><%= id %></td>
            <td><%= name %></td>
            <td><%= email %></td>
            <td><%= phone %></td>
            <td>
                <% if (path != null) { %>
                    <a href="<%= request.getContextPath() + "/" + path %>" target="_blank">View</a>
                <% } else { %>
                    N/A
                <% } %>
            </td>
            <td>
                <form action="adminPanel" method="post" style="display:inline;">
                    <input type="hidden" name="applicationId" value="<%= id %>">
                    <button name="action" value="approve">Approve</button>
                    <button name="action" value="reject">Reject</button>
                </form>
                <span class="status-badge status-<%= status.toLowerCase() %>"><%= status %></span>
            </td>
        </tr>
        <%  }
                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                out.println("<tr><td colspan='6'>Error fetching records: " + e.getMessage() + "</td></tr>");
            }
        %>
    </table>

    <a href="index.jsp" class="link-btn">Back to Home</a>
</div>
<footer>Admin — KYC System</footer>
</body>
</html>
>