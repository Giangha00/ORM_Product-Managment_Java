package org.example.mvcdemo.dto;

import java.math.BigDecimal;

/**
 * Bo loc danh sach: keyword, category, status, khoang gia, sort, phan trang.
 */
public class ProductSearchDTO {

    public static final int DEFAULT_PAGE_SIZE = 5;

    private String keyword;
    private Integer categoryId;
    private Boolean status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;
    private String sortDir;
    private int page = 1;
    private int size = DEFAULT_PAGE_SIZE;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 1);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size > 0 ? size : DEFAULT_PAGE_SIZE;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.isBlank();
    }

    public boolean hasCategory() {
        return categoryId != null && categoryId > 0;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasMinPrice() {
        return minPrice != null;
    }

    public boolean hasMaxPrice() {
        return maxPrice != null;
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(sortDir);
    }
}
