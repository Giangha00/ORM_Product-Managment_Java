package org.example.mvcdemo.controller;

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
 * CONTROLLER - huy session va dua nguoi dung ve trang dang nhap.
 *
 * ==========================================================================
 * VONG DOI SERVLET - servlet nay minh hoa them 2 diem:
 *   - loadOnStartup = 2 : container goi constructor + init() NGAY LUC DEPLOY,
 *                         khong doi request dau tien. So nho hon = nap truoc.
 *   - Vong doi SESSION (HttpSession) KHAC vong doi servlet: servlet chi co 1
 *     instance song suot doi ung dung, con session sinh/huy theo tung nguoi dung.
 *     session.invalidate() o day ket thuc vong doi cua MOT session, hoan toan
 *     khong dung gi den vong doi cua servlet.
 * ==========================================================================
 */
@WebServlet(
        name = "LogoutServlet",
        urlPatterns = {"/logout"},
        loadOnStartup = 2,
        initParams = {
                @WebInitParam(name = "redirectAfterLogout", value = "/login")
        }
)
public class LogoutServlet extends HttpServlet {

    private String redirectAfterLogout;
    private volatile long logoutCount;

    public LogoutServlet() {
        super();
        System.out.println("[LIFECYCLE] LogoutServlet (1) CONSTRUCTOR - instance vua duoc tao");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log("[LIFECYCLE] (2a) init(ServletConfig) - servlet name = " + config.getServletName());
    }

    @Override
    public void init() throws ServletException {
        this.redirectAfterLogout = getInitParameter("redirectAfterLogout");
        log("[LIFECYCLE] (2b) init() - redirectAfterLogout = " + redirectAfterLogout
                + " (chay ngay luc deploy vi loadOnStartup = 2)");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("[LIFECYCLE] (3) service() - " + req.getMethod() + " " + req.getRequestURI());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("[LIFECYCLE] (3a) doGet() - huy session");
        invalidateSession(req);
        resp.sendRedirect(req.getContextPath() + redirectAfterLogout);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("[LIFECYCLE] (3b) doPost() - huy session");
        doGet(req, resp);
    }

    private void invalidateSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            log("[LIFECYCLE] huy HttpSession id = " + session.getId());
            session.invalidate();
            logoutCount++;
        }
    }

    @Override
    public void destroy() {
        log("[LIFECYCLE] (4) destroy() - tong so lan dang xuat = " + logoutCount);
        super.destroy();
    }

    @Override
    public String getServletInfo() {
        return "LogoutServlet v1.0 - huy HttpSession va redirect ve trang dang nhap";
    }
}
