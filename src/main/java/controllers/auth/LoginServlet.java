package controllers.auth;

import entities.User;
import services.UserService;
import services.exception.UserNotFoundException;
import services.exception.UserServiceException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserEntityServiceImpl();

    // TODO: 1/10/2022 add parameter to request body instead of parameter
    // TODO: 1/10/2022 add something in return of doGet method

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");


        try {
            User user = userService.getUserDbEntityByUserName(username);

            if (checkAccess(user, password))
                login(req, user);

            else resp.sendError(401, "Invalid Password!");

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (UserNotFoundException e) {
            resp.sendError(401, e.getMessage());
        }

        super.doGet(req, resp);
    }

    private void login(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30);
    }

    private boolean checkAccess(User user, String password) {
        return new String(Base64.getDecoder().decode(user.getPasswordHash())).equals(password);
    }
}
