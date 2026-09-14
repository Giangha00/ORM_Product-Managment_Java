package org.example.mvcdemo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mvcdemo.exception.BusinessException;
import org.example.mvcdemo.service.CategoryService;

import java.io.IOException;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/categories"})
public class CategoryServlet extends HttpServlet {

    private static final String VIEW_LIST = "/WEB-INF/views/category/list.jsp";

    private CategoryService categoryService;

    @Override
    public void init() {
        this.categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher(VIEW_LIST).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                boolean status = !"false".equalsIgnoreCase(req.getParameter("status"));
                categoryService.update(id, name, description, status);
                req.getSession().setAttribute("message", "Cap nhat danh muc thanh cong.");
            } else {
                categoryService.create(req.getParameter("name"), req.getParameter("description"));
                req.getSession().setAttribute("message", "Them danh muc thanh cong.");
            }
        } catch (BusinessException e) {
            req.getSession().setAttribute("error", e.getMessage());
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Id danh muc khong hop le.");
        }
        resp.sendRedirect(req.getContextPath() + "/categories");
    }
}
