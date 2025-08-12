KYC Verification Dashboard

A Java EE + JSP + JDBC based web application for managing KYC verification processes.


📌 Overview
The KYC Verification Dashboard is a web-based system built using Jakarta EE (Servlets + JSP), JDBC, and MySQL that enables users to submit their KYC details online and allows an admin to approve or reject the verification requests.

Key Highlights:

User-friendly KYC submission form with email and phone number validation.

Admin panel for reviewing and approving/rejecting KYC requests.

Real-time status checking for users.

Clean, responsive UI with CSS styling.

Fully integrated with MySQL database.

⚙ Tech Stack

Frontend: HTML5, CSS3, JSP

Backend: Java 21, Jakarta EE Servlets

Database: MySQL (Workbench / Server)

Server: Apache Tomcat 11.0.4

JDBC Driver: MySQL Connector/J 9.0+



🚀 Setup Instructions

1️⃣ Prerequisites

Java JDK 21+

Apache Tomcat 11+

MySQL Server & Workbench

Eclipse IDE (or any Java EE supported IDE)

MySQL Connector/J in WEB-INF/lib

2️⃣ Database Setup
Open MySQL Workbench.

Run the provided SQL script:

CREATE DATABASE kyc_db;
USE kyc_db;

CREATE TABLE kyc_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    document_path VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending'
);

Ensure credentials are:

Username: root

Password: root

3️⃣ Import Project into Eclipse
File → Import → Existing Projects into Workspace → Select Project Folder

Add MySQL Connector/J jar into WEB-INF/lib

Configure Tomcat in Eclipse.

4️⃣ Run the Application
Start MySQL Server.

Deploy project on Tomcat.

Access:

User Form: http://localhost:8080/KYCVerificationDashboard/index.html

Admin Panel: http://localhost:8080/KYCVerificationDashboard/admin.jsp

Status Check: http://localhost:8080/KYCVerificationDashboard/status.jsp

🛠 Future Enhancements

File upload for ID proofs with cloud storage (AWS S3).

OTP-based verification.

Role-based authentication.

REST API integration.


📬 Contact
👤 Aditri Sharma
📧 Email: sharmaaditri09@gmail.com
💼 GitHub: Aditri-Sharma-09

