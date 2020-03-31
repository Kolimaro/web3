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

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        try {
            if (new BankClientService().addClient(new BankClient(req.getParameter("name"),
                    req.getParameter("password"), Long.parseLong(req.getParameter("money"))))) {
                pageVariables.put("message", "Add client successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                pageVariables.put("message", "Client not add");
            }
        } catch (DBException e) {
            pageVariables.put("message", "Client not add");
        }


        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("name", request.getParameter("name"));
        pageVariables.put("password", request.getParameter("password"));
        pageVariables.put("money", request.getParameter("money"));
        return pageVariables;
    }
}
