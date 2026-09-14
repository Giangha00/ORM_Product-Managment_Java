package org.example.mvcdemo.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mvcdemo.dto.ProductFormDTO;
import org.example.mvcdemo.dto.ProductPageDTO;
import org.example.mvcdemo.dto.ProductSearchDTO;
import org.example.mvcdemo.exception.BusinessException;
import org.example.mvcdemo.entity.Product;
import org.example.mvcdemo.service.CategoryService;
import org.example.mvcdemo.service.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet(
        name = "ProductServlet",
        urlPatterns = {"/products", "/products/create", "/products/edit", "/products/delete", "/products/detail"},
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "pageTitle", value = "Quan ly san pham"),
                @WebInitParam(name = "defaultAction", value = "list")
        }
)
public class ProductServlet extends HttpServlet {

    private static final String VIEW_LIST = "/WEB-INF/views/product/list.jsp";
    private static final String VIEW_FORM = "/WEB-INF/views/product/form.jsp";
    private static final String VIEW_DETAIL = "/WEB-INF/views/product/detail.jsp";

    private static final TreeMap<String, String> SORT_OPTIONS = new TreeMap<>();

    static {
        SORT_OPTIONS.put("createdAt", "Ngay tao");
        SORT_OPTIONS.put("name", "Ten");
        SORT_OPTIONS.put("price", "Gia");
    }

    private ProductService productService;
    private CategoryService categoryService;
    private String defaultAction;
    private final AtomicLong requestCount = new AtomicLong();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log("[LIFECYCLE] ProductServlet init - " + config.getServletName());
    }

    @Override
    public void init() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        this.defaultAction = getInitParameter("defaultAction");
        getServletContext().setAttribute("appPageTitle", getInitParameter("pageTitle"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        requestCount.incrementAndGet();
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            switch (resolveAction(req)) {
                case "create", "new" -> showForm(req, resp, new Product(), new ProductFormDTO());
                case "edit" -> showEditForm(req, resp);
                case "detail" -> showDetail(req, resp);
                case "delete" -> handleDelete(req, resp);
                default -> listProducts(req, resp);
            }
        } catch (BusinessException e) {
            req.setAttribute("error", e.getMessage());
            listProducts(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Da xay ra loi: " + e.getMessage());
            req.setAttribute("products", List.of());
            req.setAttribute("categories", List.of());
            req.setAttribute("page", 1);
            req.setAttribute("totalPages", 1);
            req.setAttribute("totalItems", 0);
            req.setAttribute("sortOptions", SORT_OPTIONS);
            req.getRequestDispatcher(VIEW_LIST).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            switch (resolveAction(req)) {
                case "create", "insert" -> saveProduct(req, resp, false);
                case "edit", "update" -> saveProduct(req, resp, true);
                case "delete" -> handleDelete(req, resp);
                default -> resp.sendRedirect(req.getContextPath() + "/products");
            }
        } catch (BusinessException e) {
            ProductFormDTO form = parseForm(req, true);
            req.setAttribute("error", e.getMessage());
            showForm(req, resp, productService.toViewProduct(form), form);
        } catch (Exception e) {
            ProductFormDTO form = parseForm(req, true);
            req.setAttribute("error", "Du lieu khong hop le: " + e.getMessage());
            showForm(req, resp, productService.toViewProduct(form), form);
        }
    }

    @Override
    public void destroy() {
        this.productService = null;
        this.categoryService = null;
        getServletContext().removeAttribute("appPageTitle");
        super.destroy();
    }

    private String resolveAction(HttpServletRequest req) {
        String path = req.getServletPath();
        if (path.endsWith("/create")) {
            return "create";
        }
        if (path.endsWith("/edit")) {
            return "edit";
        }
        if (path.endsWith("/detail")) {
            return "detail";
        }
        if (path.endsWith("/delete")) {
            return "delete";
        }
        String action = req.getParameter("action");
        if (action == null || action.isBlank()) {
            return defaultAction;
        }
        return action;
    }

    private void listProducts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ProductSearchDTO criteria = buildCriteria(req);
        ProductPageDTO pageData = productService.searchPage(criteria);
        req.setAttribute("products", pageData.getProducts());
        req.setAttribute("categories", categoryService.findActive());
        req.setAttribute("keyword", criteria.getKeyword());
        req.setAttribute("categoryId", criteria.getCategoryId());
        req.setAttribute("statusFilter", criteria.getStatus());
        req.setAttribute("minPrice", criteria.getMinPrice());
        req.setAttribute("maxPrice", criteria.getMaxPrice());
        req.setAttribute("sortBy", criteria.getSortBy());
        req.setAttribute("sortDir", criteria.getSortDir());
        req.setAttribute("sortOptions", SORT_OPTIONS);
        req.setAttribute("page", pageData.getPage());
        req.setAttribute("totalPages", pageData.getTotalPages());
        req.setAttribute("totalItems", pageData.getTotalItems());
        req.getRequestDispatcher(VIEW_LIST).forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Id san pham khong hop le.");
            listProducts(req, resp);
            return;
        }
        Product product = productService.findById(id);
        showForm(req, resp, product, productService.toForm(product));
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Id san pham khong hop le.");
            listProducts(req, resp);
            return;
        }
        Product product = productService.findById(id);
        req.setAttribute("product", product);
        req.getRequestDispatcher(VIEW_DETAIL).forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Product product, ProductFormDTO form)
            throws ServletException, IOException {
        req.setAttribute("product", product);
        req.setAttribute("form", form);
        req.setAttribute("categories", productService.listCategoriesForForm(product));
        req.getRequestDispatcher(VIEW_FORM).forward(req, resp);
    }

    private void saveProduct(HttpServletRequest req, HttpServletResponse resp, boolean isEdit)
            throws ServletException, IOException {
        ProductFormDTO form = parseForm(req, isEdit);
        if (isEdit) {
            productService.update(form);
            req.getSession().setAttribute("message", "Cap nhat san pham thanh cong.");
        } else {
            productService.create(form);
            req.getSession().setAttribute("message", "Them san pham thanh cong.");
        }
        resp.sendRedirect(req.getContextPath() + "/products");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            productService.softDelete(Integer.parseInt(req.getParameter("id")));
            req.getSession().setAttribute("message", "Da xoa mem san pham thanh cong.");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("message", "Id san pham khong hop le.");
        } catch (BusinessException e) {
            req.getSession().setAttribute("message", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/products");
    }

    private ProductSearchDTO buildCriteria(HttpServletRequest req) {
        ProductSearchDTO c = new ProductSearchDTO();
        String keyword = req.getParameter("keyword");
        if (keyword != null && !keyword.isBlank()) {
            c.setKeyword(keyword.trim());
        }
        c.setCategoryId(parseIntOrNull(req.getParameter("categoryId")));
        String status = req.getParameter("status");
        if ("true".equalsIgnoreCase(status) || "false".equalsIgnoreCase(status)) {
            c.setStatus(Boolean.parseBoolean(status));
        }
        c.setMinPrice(parseDecimal(req.getParameter("minPrice")));
        c.setMaxPrice(parseDecimal(req.getParameter("maxPrice")));
        String sortBy = req.getParameter("sortBy");
        if (sortBy != null && SORT_OPTIONS.containsKey(sortBy)) {
            c.setSortBy(sortBy);
        }
        c.setSortDir(req.getParameter("sortDir"));
        Integer page = parseIntOrNull(req.getParameter("page"));
        if (page != null) {
            c.setPage(page);
        }
        return c;
    }

    private ProductFormDTO parseForm(HttpServletRequest req, boolean withId) {
        ProductFormDTO f = new ProductFormDTO();
        if (withId) {
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id != null) {
                f.setId(id);
            }
        }
        f.setSku(req.getParameter("sku"));
        f.setName(req.getParameter("name"));
        f.setDescription(req.getParameter("description"));
        f.setStatus(!"false".equalsIgnoreCase(req.getParameter("status")));
        f.setPrice(parseDecimal(req.getParameter("price")));
        f.setQuantity(parseIntOrNull(req.getParameter("quantity")));
        Integer cat = parseIntOrNull(req.getParameter("categoryId"));
        f.setCategoryId(cat == null ? 0 : cat);
        f.setManufacturer(req.getParameter("manufacturer"));
        f.setWarrantyMonths(parseIntOrNull(req.getParameter("warrantyMonths")));
        f.setOrigin(req.getParameter("origin"));
        f.setDetailDescription(req.getParameter("detailDescription"));
        f.setTechnicalSpec(req.getParameter("technicalSpec"));
        return f;
    }

    private static Integer parseIntOrNull(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static BigDecimal parseDecimal(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
