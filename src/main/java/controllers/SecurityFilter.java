package controllers;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter",
        servletNames = {"UserServlet", "DepositServlet", "TransferToOtherUserServlet"
                , "WithdrawServlet", "WalletServlet", "TransactionServlet"}
)
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null)
            filterChain.doFilter(request, response);
        else
            response.sendError(401, "Authentication failed! Login first");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
