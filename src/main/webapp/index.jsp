<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>KYC Upload</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<header>KYC Verification Dashboard</header>
<div class="container">
    <h2>Upload Your KYC</h2>
    <form action="submitKYC" method="post" enctype="multipart/form-data">
        <label>Full Name</label>
        <input type="text" name="name" id="name" required>

        <label>Email</label>
        <input type="email" name="email" id="email" required>

        <label>Phone</label>
        <input type="text" name="phone" id="phone" pattern="[0-9]{10}" 
           title="Enter a valid 10-digit phone number" required>

        <label>ID Proof (image/pdf)</label>
        <input type="file" name="idproof" accept=".png,.jpg,.jpeg,.pdf" required>

        <button type="submit">Submit KYC</button>
    </form>

    <hr>
    <a href="admin.jsp" class="link-btn">Go to Admin Panel</a>
    <a href="status.jsp" class="link-btn">Check KYC Status</a>
</div>
<footer>© Your Bank - KYC System</footer>
</body>
</html>
