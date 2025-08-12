<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Check KYC Status</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<header>Check KYC Status</header>
<div class="container">
    <h2>Enter Application ID to check status</h2>
    <form action="checkStatus" method="post">
        <label>Application ID</label>
        <input type="text" name="applicationId" required>
        <button type="submit">Check Status</button>
    </form>

    <a href="index.jsp" class="link-btn">Back to Home</a>
</div>
<footer>KYC Status Checker</footer>
</body>
</html>
