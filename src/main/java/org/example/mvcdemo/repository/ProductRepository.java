package org.example.mvcdemo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.mvcdemo.dto.ProductSearchDTO;
import org.example.mvcdemo.entity.Category;
import org.example.mvcdemo.entity.Product;
import org.example.mvcdemo.entity.ProductDetail;
import org.example.mvcdemo.util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {

    public int insert(Product p) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                p.setCategory(em.getReference(Category.class, p.getCategoryId()));
                p.setDeleted(false);
                if (p.getDetail() == null) {
                    p.setDetail(new ProductDetail());
                }
                em.persist(p);
                tx.commit();
                return p.getId();
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    public boolean update(Product incoming) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Product managed = em.find(Product.class, incoming.getId());
                if (managed == null || managed.isDeleted()) {
                    tx.rollback();
                    return false;
                }
                managed.setSku(incoming.getSku());
                managed.setName(incoming.getName());
                managed.setPrice(incoming.getPrice());
                managed.setQuantity(incoming.getQuantity());
                managed.setDescription(incoming.getDescription());
                managed.setStatus(incoming.isStatus());
                managed.setCategory(em.getReference(Category.class, incoming.getCategoryId()));

                ProductDetail src = incoming.getDetail();
                if (src != null) {
                    if (managed.getDetail() == null) {
                        managed.setDetail(new ProductDetail());
                    }
                    ProductDetail dest = managed.getDetail();
                    dest.setManufacturer(src.getManufacturer());
                    dest.setWarrantyMonths(src.getWarrantyMonths());
                    dest.setOrigin(src.getOrigin());
                    dest.setDescription(src.getDescription());
                    dest.setTechnicalSpec(src.getTechnicalSpec());
                }
                tx.commit();
                return true;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    public boolean softDelete(int id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Product managed = em.find(Product.class, id);
                if (managed == null || managed.isDeleted()) {
                    tx.rollback();
                    return false;
                }
                managed.setDeleted(true);
                tx.commit();
                return true;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    public Optional<Product> findByIdWithDetail(int id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Product> q = em.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category LEFT JOIN FETCH p.detail "
                            + "WHERE p.id = :id AND p.deleted = false",
                    Product.class);
            q.setParameter("id", id);
            List<Product> list = q.getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        }
    }

    public boolean existsBySku(String sku, Integer excludeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String jpql = excludeId == null
                    ? "SELECT COUNT(p) FROM Product p WHERE LOWER(p.sku) = :sku AND p.deleted = false"
                    : "SELECT COUNT(p) FROM Product p WHERE LOWER(p.sku) = :sku AND p.deleted = false AND p.id <> :id";
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            q.setParameter("sku", sku.trim().toLowerCase());
            if (excludeId != null) {
                q.setParameter("id", excludeId);
            }
            return q.getSingleResult() > 0;
        }
    }

    public List<Product> search(ProductSearchDTO criteria) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> p = cq.from(Product.class);
            p.fetch("category", JoinType.LEFT);
            cq.select(p).distinct(true).where(filters(cb, p, criteria));
            cq.orderBy(order(cb, p, criteria));
            return em.createQuery(cq)
                    .setFirstResult(criteria.getOffset())
                    .setMaxResults(criteria.getSize())
                    .getResultList();
        }
    }

    public int count(ProductSearchDTO criteria) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Product> p = cq.from(Product.class);
            cq.select(cb.count(p)).where(filters(cb, p, criteria));
            Long total = em.createQuery(cq).getSingleResult();
            return total == null ? 0 : total.intValue();
        }
    }

    private Predicate[] filters(CriteriaBuilder cb, Root<Product> p, ProductSearchDTO c) {
        List<Predicate> preds = new ArrayList<>();
        preds.add(cb.isFalse(p.get("deleted")));
        if (c.hasKeyword()) {
            String like = "%" + c.getKeyword().trim().toLowerCase() + "%";
            preds.add(cb.or(
                    cb.like(cb.lower(p.get("name")), like),
                    cb.like(cb.lower(cb.coalesce(p.get("sku"), "")), like)
            ));
        }
        if (c.hasCategory()) {
            preds.add(cb.equal(p.get("category").get("id"), c.getCategoryId()));
        }
        if (c.hasStatus()) {
            preds.add(cb.equal(p.get("status"), c.getStatus()));
        }
        if (c.hasMinPrice()) {
            preds.add(cb.ge(p.get("price"), c.getMinPrice()));
        }
        if (c.hasMaxPrice()) {
            preds.add(cb.le(p.get("price"), c.getMaxPrice()));
        }
        return preds.toArray(Predicate[]::new);
    }

    private Order order(CriteriaBuilder cb, Root<Product> p, ProductSearchDTO c) {
        boolean asc = c.isAscending();
        String sortBy = c.getSortBy() == null ? "" : c.getSortBy();
        return switch (sortBy) {
            case "name" -> asc ? cb.asc(p.get("name")) : cb.desc(p.get("name"));
            case "price" -> asc ? cb.asc(p.get("price")) : cb.desc(p.get("price"));
            case "createdAt" -> asc ? cb.asc(p.get("createdAt")) : cb.desc(p.get("createdAt"));
            default -> cb.desc(p.get("id"));
        };
    }
}
