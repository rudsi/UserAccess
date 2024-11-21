<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Create Software</title>
    </head>

    <body>
        <h1>Create New Software</h1>
        <form action="SoftwareServlet" method="post">
            <label>Software Name:</label>
            <input type="text" name="softwareName" required><br>
        
            <label>Description:</label>
            <textarea name="description" required></textarea><br>
        
            <label>Access Levels:</label><br>
            <input type="radio" name="accessLevels" value="Read" required> Read<br>
            <input type="radio" name="accessLevels" value="Write" required> Write<br>
            <input type="radio" name="accessLevels" value="Admin" required> Admin<br>
        
            <button type="submit">Submit</button>
        </form>
        <a href="pendingRequests.jsp">
            <button type="button">View Pending Requests</button>
        </a>

    </body>

</html>