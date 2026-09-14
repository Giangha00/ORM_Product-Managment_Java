package org.example.mvcdemo.service;

import org.example.mvcdemo.dto.ProductFormDTO;
import org.example.mvcdemo.dto.ProductSearchDTO;
import org.example.mvcdemo.exception.BusinessException;
import org.example.mvcdemo.entity.Category;
import org.example.mvcdemo.entity.Product;
import org.example.mvcdemo.entity.ProductDetail;
import org.example.mvcdemo.repository.CategoryRepository;
import org.example.mvcdemo.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    public List<Product> search(ProductSearchDTO criteria) {
        return productRepository.search(criteria);
    }

    public int count(ProductSearchDTO criteria) {
        return productRepository.count(criteria);
    }

    public Product findById(int id) {
        return productRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        "Khong tim thay san pham id = " + id + " (co the da bi xoa mem)."));
    }

    public int create(ProductFormDTO form) {
        validate(form, false);
        Product p = toProduct(form, false);
        return productRepository.insert(p);
    }

    public void update(ProductFormDTO form) {
        validate(form, true);
        if (productRepository.findByIdWithDetail(form.getId()).isEmpty()) {
            throw new BusinessException(
                    "Khong tim thay san pham id = " + form.getId() + " de cap nhat.");
        }
        Product p = toProduct(form, true);
        productRepository.update(p);
    }

    public boolean softDelete(int id) {
        return productRepository.softDelete(id);
    }

    public List<Category> listCategoriesForForm(Product current) {
        LinkedList<Category> categories = new LinkedList<>(categoryRepository.findActive());
        Map<Integer, Category> byId = new HashMap<>();
        for (Category c : categories) {
            byId.put(c.getId(), c);
        }
        if (current != null && current.getCategoryId() > 0
                && !byId.containsKey(current.getCategoryId())) {
            Category existing = categoryRepository.findById(current.getCategoryId());
            if (existing != null) {
                categories.addFirst(existing);
            }
        }
        return categories;
    }

    private void validate(ProductFormDTO f, boolean isEdit) {
        ArrayDeque<String> errors = new ArrayDeque<>();
        if (f.getSku() == null || f.getSku().isBlank()) {
            errors.addLast("SKU khong duoc rong.");
        } else {
            Integer excludeId = isEdit ? f.getId() : null;
            if (productRepository.existsBySku(f.getSku(), excludeId)) {
                errors.addLast("SKU da ton tai, vui long chon ma khac.");
            }
        }
        if (f.getName() == null || f.getName().isBlank()) {
            errors.addLast("Ten san pham khong duoc rong.");
        }
        if (f.getPrice() == null) {
            errors.addLast("Gia san pham khong hop le.");
        } else if (f.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.addLast("Gia san pham phai lon hon 0.");
        }
        if (f.getQuantity() == null || f.getQuantity() < 0) {
            errors.addLast("So luong san pham phai >= 0.");
        }
        int warranty = f.getWarrantyMonths() == null ? 0 : f.getWarrantyMonths();
        if (warranty < 0) {
            errors.addLast("Thoi gian bao hanh phai >= 0.");
        }
        if (f.getCategoryId() <= 0) {
            errors.addLast("Vui long chon danh muc.");
        } else if (!categoryRepository.exists(f.getCategoryId())) {
            errors.addLast("Danh muc khong hop le.");
        } else if (!isEdit && !categoryRepository.existsActive(f.getCategoryId())) {
            errors.addLast("Danh muc khong hop le hoac dang ngung hoat dong.");
        }
        if (!errors.isEmpty()) {
            throw new BusinessException(String.join(" ", errors));
        }
    }

    private Product toProduct(ProductFormDTO f, boolean isEdit) {
        Product p = new Product();
        if (isEdit) {
            p.setId(f.getId());
        }
        p.setSku(f.getSku().trim());
        p.setName(f.getName().trim());
        p.setPrice(f.getPrice());
        p.setQuantity(f.getQuantity() == null ? 0 : f.getQuantity());
        p.setDescription(blankToNull(f.getDescription()));
        p.setStatus(f.isStatus());
        p.setCategoryId(f.getCategoryId());

        ProductDetail d = new ProductDetail();
        d.setManufacturer(blankToNull(f.getManufacturer()));
        d.setWarrantyMonths(f.getWarrantyMonths() == null ? 0 : f.getWarrantyMonths());
        d.setOrigin(blankToNull(f.getOrigin()));
        d.setDescription(blankToNull(f.getDetailDescription()));
        d.setTechnicalSpec(blankToNull(f.getTechnicalSpec()));
        p.setDetail(d);
        return p;
    }

    private static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
