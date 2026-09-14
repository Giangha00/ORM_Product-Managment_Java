package org.example.mvcdemo.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "product_details")
public class ProductDetail implements Serializable {

    public ProductDetail() {
    }

    @Id
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Product product;

    private String manufacturer;
    @Column(name = "warranty_months")
    private int warrantyMonths;
    private String origin;
    private String description;
    @Column(name = "technical_spec")
    private String technicalSpec;

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.id = product.getId();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnicalSpec() {
        return technicalSpec;
    }

    public void setTechnicalSpec(String technicalSpec) {
        this.technicalSpec = technicalSpec;
    }
}
