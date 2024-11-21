<%@ page import="java.util.List" %>
    <%@ page import="com.rudsi.utils.SoftwareNamesUtil" %>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Request Software Access</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        max-width: 500px;
                        margin: 0 auto;
                        padding: 20px;
                    }

                    form {
                        display: flex;
                        flex-direction: column;
                    }

                    label {
                        margin-top: 10px;
                    }

                    select,
                    textarea,
                    input[type="submit"] {
                        margin-bottom: 15px;
                        padding: 8px;
                    }

                    input[type="submit"] {
                        background-color: #4CAF50;
                        color: white;
                        border: none;
                        cursor: pointer;
                    }
                </style>
            </head>

            <body>
                <h1>Request Software Access</h1>

                <form action="RequestServlet" method="post">
                    <label for="softwareName">Software Name:</label>
                    <select id="softwareName" name="softwareName" required>
                        <option value="" disabled selected>Select a Software</option>

                        <% List<String> softwareNames = null;
                            try {
                            softwareNames = SoftwareNamesUtil.getSoftwareNames();
                            if (softwareNames != null && !softwareNames.isEmpty()) {
                            for (String software : softwareNames) {
                            %>
                            <option value="<%= software %>">
                                <%= software %>
                            </option>
                            <% } } else { %>
                                <option value="" disabled>No Software Available</option>
                                <% } } catch (Exception e) {
                                    out.println("<option value='' disabled>Error fetching software names</option>");
                                    }
                                    %>
                    </select>
                    <label for="accessType">Access Type:</label>
                    <select id="accessType" name="accessType" required>
                        <option value="" disabled selected>Select Access Type</option>
                        <option value="Read">Read</option>
                        <option value="Write">Write</option>
                        <option value="Admin">Admin</option>
                    </select>

                    <label for="reason">Reason for Request:</label>
                    <textarea id="reason" name="reason" rows="4" required></textarea>

                    <input type="submit" value="Submit Request">
                </form>
            </body>

            </html>