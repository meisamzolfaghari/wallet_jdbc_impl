package controllers.auth;

import com.google.gson.Gson;
import controllers.AuthUserUtils;
import controllers.auth.dto.LoginDTO;
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
import java.util.Base64;

@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    // TODO: 1/17/2022 use jsp and basic authentication

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AuthUserUtils.removeUserFromCurrentSessionIfExist(req);

        LoginDTO loginDTO = new Gson().fromJson(req.getReader(), LoginDTO.class);

        try {
            User user = userService.getUserDbEntityByUserName(loginDTO.getUsername());

            if (checkAccess(user, loginDTO.getPassword()))
                login(req, user);

            else resp.sendError(401, "Username or Password is Invalid!");

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        }

        PrintWriter writer = resp.getWriter();
        writer.write(" Welcome Back Dear " + loginDTO.getUsername());
        writer.flush();
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
