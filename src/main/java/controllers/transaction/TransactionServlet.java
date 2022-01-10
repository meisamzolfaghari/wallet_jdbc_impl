package controllers.transaction;

import controllers.AuthUserUtils;
import entities.Transaction;
import entities.User;
import services.TransactionService;
import services.UserService;
import services.exception.TransactionServiceException;
import services.exception.UserNotFoundException;
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
            String senderUsername = getSenderUsername(transaction);
            String receiverUsername = getReceiverUsername(transaction);

            // TODO: 1/10/2022 change them all and create related html pages. not here! 
            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.write("<html>" +
                    "<body>" +
                    "<h2>Deposit Transaction Created...</h2>" +
                    "<p> Transaction Amount: " + transaction.getAmount() + "</p>" +
                    "<p> Transaction Date: " + transaction.getTransactionDate() + "</p>" +
                    "<p> Transaction Status: " + transaction.getStatus() + "</p>" +
                    "<p> Transaction Type: " + transaction.getType() + "</p>" +
                    "<p> Transaction Sender Username: " + senderUsername + "</p>" +
                    "<p> Transaction Receiver Username: " + receiverUsername + "</p>" +
                    "</body>" +
                    "</html>");

        } catch (TransactionServiceException | UserServiceException e) {
            resp.sendError(504, e.getMessage());
        }
    }

    private String getReceiverUsername(Transaction transaction) throws UserServiceException {
        if (transaction.getReceiverWalletId() != null) {
            try {
                return userService.getUserByWalletId(transaction.getReceiverWalletId()).getUsername();
            } catch (UserNotFoundException e) {
                return "-";
            }
        }
        return "-";
    }

    private String getSenderUsername(Transaction transaction) throws UserServiceException {
        if (transaction.getSenderWalletId() != null) {
            try {
                return userService.getUserByWalletId(transaction.getReceiverWalletId()).getUsername();
            } catch (UserNotFoundException e) {
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
