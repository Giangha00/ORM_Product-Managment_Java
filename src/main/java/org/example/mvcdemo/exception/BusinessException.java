package org.example.mvcdemo.exception;

/**
 * Loi nghiep vu (validate, SKU trung, category khong hop le).
 * Service nem, Servlet bat de hien thi thong bao — khong phai 500.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
