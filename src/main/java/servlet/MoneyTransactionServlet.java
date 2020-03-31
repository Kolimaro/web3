package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String recipient = req.getParameter("nameTo");
        Long count = Long.parseLong(req.getParameter("count"));

        try {
            BankClient sender = bankClientService.getClientByName(senderName);
            if (sender.getPassword().equals(senderPass)) {
                if (bankClientService.sendMoneyToClient(sender, recipient, count)) {
                    pageVariables.put("message", "The transaction was successful");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    pageVariables.put("message", "transaction rejected");
                }
            } else {
                pageVariables.put("message", "transaction rejected");
            }
        } catch (DBException e) {
            pageVariables.put("message", "transaction rejected");
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}
