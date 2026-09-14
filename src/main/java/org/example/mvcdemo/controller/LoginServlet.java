package org.example.mvcdemo.controller;

import org.example.mvcdemo.entity.User;
import org.example.mvcdemo.repository.UserRepository;
import org.example.mvcdemo.util.PasswordUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CONTROLLER - xu ly dang nhap.
 * GET  /login  -> hien thi form (forward toi login.jsp)
 * POST /login  -> kiem tra username/password, tao session, redirect toi /products
 *
 * Servlet KHONG chua HTML va KHONG chua SQL: no goi DAO (Model layer) roi chon
 * View (JSP) de forward/redirect toi - dung nguyen tac tach biet cua MVC.
 *
 * ==========================================================================
 * VONG DOI SERVLET (SERVLET LIFECYCLE) - day du 5 giai doan
 * ==========================================================================
 *   (1) LOAD & INSTANTIATE : container nap file .class va goi constructor
 *                            KHONG doi so. Luc nay CHUA co ServletConfig.
 *   (2) INIT               : container goi init(ServletConfig) -> init()
 *                            DUNG 1 LAN duy nhat trong ca doi servlet.
 *                            Noi cap phat tai nguyen dung chung (DAO, pool...)
 *                            va doc tham so cau hinh tu &lt;init-param&gt;.
 *   (3) SERVICE            : moi request -> service(req, resp). HttpServlet
 *                            xem method HTTP roi dispatch sang doGet/doPost/...
 *                            Chay DA LUONG: nhieu thread cung 1 instance servlet.
 *   (4) DESTROY            : container goi destroy() 1 lan truoc khi go servlet
 *                            (undeploy / shutdown) -> giai phong tai nguyen.
 *   (5) GARBAGE COLLECTION : sau destroy(), instance duoc GC thu hoi.
 * ==========================================================================
 */
@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/login"},
        initParams = {
                @WebInitParam(name = "sessionTimeoutMinutes", value = "30")
        }
)
public class LoginServlet extends HttpServlet {

    /** Tai nguyen dung chung - KHOI TAO trong init(), KHONG phai o field. */
    private UserRepository userRepository;

    /** Doc tu &lt;init-param&gt; trong giai doan init(). */
    private int sessionTimeoutMinutes;

    /** Dem so request da phuc vu - minh hoa 1 instance dung cho MOI request. */
    private volatile long requestCount;

    public LoginServlet() {
        super();
        System.out.println("[LIFECYCLE] LoginServlet (1) CONSTRUCTOR - instance vua duoc tao");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log("[LIFECYCLE] (2a) init(ServletConfig) - servlet name = " + config.getServletName());
    }

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
        this.sessionTimeoutMinutes = Integer.parseInt(getInitParameter("sessionTimeoutMinutes"));
        log("[LIFECYCLE] (2b) init() - da tao UserRepository, sessionTimeout = "
                + sessionTimeoutMinutes + " phut, context = " + getServletContext().getContextPath());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        requestCount++;
        log("[LIFECYCLE] (3) service() - " + req.getMethod() + " " + req.getRequestURI()
                + " | request thu " + requestCount + " | thread = " + Thread.currentThread().getName());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("[LIFECYCLE] (3a) doGet() - hien thi form dang nhap");
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loggedUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("[LIFECYCLE] (3b) doPost() - kiem tra thong tin dang nhap");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userRepository.findByUsername(username);

        if (user != null && PasswordUtil.matches(password, user.getPassword())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setMaxInactiveInterval(sessionTimeoutMinutes * 60);
            resp.sendRedirect(req.getContextPath() + "/products");
        } else {
            req.setAttribute("error", "Sai ten dang nhap hoac mat khau!");
            req.setAttribute("username", username);
            RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
            rd.forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        log("[LIFECYCLE] (4) destroy() - da phuc vu tong cong " + requestCount
                + " request, giai phong UserRepository");
        this.userRepository = null;
        super.destroy();
    }

    @Override
    public String getServletInfo() {
        return "LoginServlet v1.0 - xu ly dang nhap bang BCrypt + HttpSession";
    }
}
