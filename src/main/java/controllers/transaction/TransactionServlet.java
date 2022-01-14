package controllers.transaction;

import com.google.gson.Gson;
import controllers.AuthUserUtils;
import controllers.transaction.dto.TransactionDTO;
import entities.Transaction;
import entities.User;
import services.TransactionService;
import services.UserService;
import services.exception.EntityNotFoundException;
import services.exception.TransactionServiceException;
import services.exception.UserServiceException;
import services.impl.TransactionEntityServiceImpl;
import services.impl.UserEntityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name = "TransactionServlet", urlPatterns = "/transaction")
public class TransactionServlet extends HttpServlet {

    private final TransactionService transactionService = TransactionEntityServiceImpl.getInstance();
    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();

        String transactionId = req.getParameter("transactionId");

        try {

            Transaction transaction = transactionService.getById(transactionId);

            if (transactionIsNotForCurrentUser(currentUser, transaction)) {
                resp.sendError(403, "Transaction is not yours!");
                return;
            }

            // TODO: 1/10/2022 this part can be better (maybe separate deposit and withdraw table and entity)
            String senderUsername = getUsernameByWalletId(transaction.getSenderWalletId());
            String receiverUsername = getUsernameByWalletId(transaction.getReceiverWalletId());

            TransactionDTO transactionDTO = new TransactionDTO(transaction.getId(), transaction.getTransactionDate(),
                    transaction.getStatus(), transaction.getType(), senderUsername, receiverUsername);

            // TODO: 1/10/2022 change them all and create related html pages. not here! 
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            writer.print(new Gson().toJson(transactionDTO));
            writer.flush();

        } catch (TransactionServiceException | UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        }
    }

    private String getUsernameByWalletId(Long walletId) throws UserServiceException {
        if (walletId != null) {
            try {
                return userService.getUserByWalletId(walletId).getUsername();
            } catch (EntityNotFoundException e) {
                return "-";
            }
        }
        return "-";
    }

    private boolean transactionIsNotForCurrentUser(User currentUser, Transaction transaction) {
        return !(Objects.equals(currentUser.getWalletId(), transaction.getSenderWalletId())
                || Objects.equals(currentUser.getWalletId(), transaction.getReceiverWalletId()));
    }
}
