package controllers.auth;

import com.google.gson.Gson;
import controllers.auth.dto.RegisterRequestDTO;
import controllers.auth.dto.RegisterResponseDTO;
import entities.User;
import services.UserService;
import services.dto.UserDetails;
import services.exception.UserServiceException;
import services.exception.notValidUserToCreateException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();

        RegisterRequestDTO registerRequestDTO = gson.fromJson(req.getReader(), RegisterRequestDTO.class);

        if (notValidRegisterDTO(registerRequestDTO)) {
            resp.sendError(403, "username or password must be provided!");
            return;
        }

        String passwordHash = Base64.getEncoder().encodeToString(registerRequestDTO.getPassword().getBytes());

        try {

            UserDetails userDetails = userService.createSignUp(
                    new User(registerRequestDTO.getUsername(), passwordHash, registerRequestDTO.getEmail()));

            RegisterResponseDTO registerResponseDTO =
                    new RegisterResponseDTO(userDetails.getUserName(), userDetails.getEmail());

            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            writer.write(gson.toJson(registerResponseDTO));
            writer.flush();

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (notValidUserToCreateException e) {
            resp.sendError(400, e.getMessage());
        }
    }

    private boolean notValidRegisterDTO(RegisterRequestDTO registerRequestDTO) {
        return registerRequestDTO.getUsername() == null || registerRequestDTO.getPassword() == null;
    }

}
