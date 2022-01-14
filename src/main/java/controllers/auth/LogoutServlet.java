package controllers.auth;

import controllers.AuthUserUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LogoutServlet", urlPatterns = "/auth/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AuthUserUtils.removeUserFromCurrentSessionIfExist(req);

        PrintWriter writer = resp.getWriter();
        writer.write(" Logout Successfully... ");
        writer.flush();
    }

}
