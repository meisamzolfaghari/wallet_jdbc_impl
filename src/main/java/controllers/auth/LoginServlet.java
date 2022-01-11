package controllers.auth;

import controllers.AuthUserUtils;
import entities.User;
import services.UserService;
import services.exception.EntityNotFoundException;
import services.exception.UserServiceException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;

@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    // TODO: 1/10/2022 add parameter to request body instead of parameter

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AuthUserUtils.removeUserFromCurrentSessionIfExist(req);

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = userService.getUserDbEntityByUserName(username);

            if (checkAccess(user, password))
                login(req, user);

            else resp.sendError(401, "Invalid Password!");

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        }

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        writer.write("<html>" +
                "<body>" +
                "<h2>Login Successfully...</h2>" +
                "<p> Welcome Back Dear " + username + "</p>" +
                "</body>" +
                "</html>");
    }

    private void login(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(1800);
    }

    private boolean checkAccess(User user, String password) {
        return Base64.getEncoder().encodeToString(password.getBytes()).equals(user.getPasswordHash());
    }
}
