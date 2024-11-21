package com.rudsi.servlets;

import com.rudsi.models.Request;
import com.rudsi.utils.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApprovalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdStr != null && action != null) {
            try {
                int requestId = Integer.parseInt(requestIdStr);
                Request req = RequestUtils.fetchRequestById(requestId);

                if (req != null) {
                    if ("approve".equalsIgnoreCase(action)) {
                        req.setStatus("Approved");
                    } else if ("reject".equalsIgnoreCase(action)) {
                        req.setStatus("Rejected");
                    }
                    RequestUtils.updateRequestStatus(req);
                    response.sendRedirect("pendingRequests.jsp");
                } else {
                    response.sendRedirect("error.jsp");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}
