package controllers.wallet;

import controllers.AuthUserUtils;
import entities.User;
import entities.Wallet;
import services.WalletService;
import services.exception.WalletServiceException;
import services.impl.WalletEntityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "WalletServlet", urlPatterns = "/wallet")
public class WalletServlet extends HttpServlet {

    private final WalletService walletService = WalletEntityServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();

        try {

            Wallet wallet = walletService.getById(currentUser.getWalletId());

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.write("<html>" +
                    "<body>" +
                    "<h2>Deposit Transaction Created...</h2>" +
                    "<p> Username: " + currentUser.getUsername() + "</p>" +
                    "<p> Balance: " + wallet.getBalance() + "</p>" +
                    "</body>" +
                    "</html>");
        } catch (WalletServiceException e) {
            resp.sendError(504, e.getMessage());
        }

    }
}
