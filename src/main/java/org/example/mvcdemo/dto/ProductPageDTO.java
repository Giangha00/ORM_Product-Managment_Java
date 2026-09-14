package org.example.mvcdemo.dto;

import org.example.mvcdemo.entity.Product;

import java.util.List;

/** Ket qua list + phan trang (logic nam o Service). */
public class ProductPageDTO {

    private final List<Product> products;
    private final int page;
    private final int totalPages;
    private final int totalItems;

    public ProductPageDTO(List<Product> products, int page, int totalPages, int totalItems) {
        this.products = products;
        this.page = page;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
