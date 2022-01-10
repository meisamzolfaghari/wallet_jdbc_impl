package controllers;

import entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class AuthUserUtils {

    private AuthUserUtils() {
    }

    public static Optional<User> getCurrentUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session == null ? Optional.empty()
                : Optional.of((User) session.getAttribute("user"));
    }

    public static void removeUserFromCurrentSessionIfExist(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.removeAttribute("user");
    }
}
