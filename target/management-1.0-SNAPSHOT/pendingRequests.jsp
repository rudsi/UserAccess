<%@ page import="java.util.List" %> <!-- Import List class for use in JSP -->
    <%@ page import="com.rudsi.models.Request" %> <!-- Import your Request class -->
        <%@ page import="com.rudsi.utils.RequestUtils" %> <!-- Import the RequestUtils utility class -->

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Pending Requests</title>
            </head>

            <body>
                <h1>Pending Requests</h1>

                <% List<Request> pendingRequests = RequestUtils.fetchPendingRequests();%>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Employee Name</th>
                                <th>Software Name</th>
                                <th>Access Type</th>
                                <th>Reason</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (pendingRequests !=null && !pendingRequests.isEmpty()) { 
                                   for (Request req : pendingRequests) { %>
                                <tr>
                                    <td>
                                        <%= req.getEmployeeName() %>
                                    </td>
                                    <td>
                                        <%= req.getSoftwareName() %>
                                    </td>
                                    <td>
                                        <%= req.getAccessType() %>
                                    </td>
                                    <td>
                                        <%= req.getReason() %>
                                    </td>
                                    <td>
                                        <a
                                            href="ApprovalServlet?requestId=<%= req.getRequestId() %>&action=approve">Approve</a>
                                        |
                                        <a
                                            href="ApprovalServlet?requestId=<%= req.getRequestId() %>&action=reject">Reject</a>
                                    </td>
                                </tr>
                                <% } } else { %>
                                    <tr>
                                        <td colspan="5">No pending requests</td>
                                    </tr>
                                    <% } %>
                        </tbody>
                    </table>

            </body>

            </html>