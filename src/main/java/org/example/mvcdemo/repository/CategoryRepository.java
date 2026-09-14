package org.example.mvcdemo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.mvcdemo.entity.Category;
import org.example.mvcdemo.util.JPAUtil;

import java.util.List;

public class CategoryRepository {

    public List<Category> findActive() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                    "SELECT c FROM Category c WHERE c.status = true ORDER BY c.name",
                    Category.class
            ).getResultList();
        }
    }

    public List<Category> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.name",
                    Category.class
            ).getResultList();
        }
    }

    public Category findById(int id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Category.class, id);
        }
    }

    public boolean exists(int id) {
        return findById(id) != null;
    }

    public boolean existsActive(int id) {
        Category c = findById(id);
        return c != null && c.isStatus();
    }

    public boolean existsByName(String name, Integer excludeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String jpql = excludeId == null
                    ? "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = :name"
                    : "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = :name AND c.id <> :id";
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            q.setParameter("name", name.trim().toLowerCase());
            if (excludeId != null) {
                q.setParameter("id", excludeId);
            }
            return q.getSingleResult() > 0;
        }
    }

    public long countActiveProducts(int categoryId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Product p WHERE p.category.id = :cid "
                            + "AND p.deleted = false AND p.status = true",
                    Long.class
            ).setParameter("cid", categoryId).getSingleResult();
        }
    }

    public void insert(Category c) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(c);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    public void update(Category c) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Category managed = em.find(Category.class, c.getId());
                if (managed == null) {
                    tx.rollback();
                    return;
                }
                managed.setName(c.getName());
                managed.setDescription(c.getDescription());
                managed.setStatus(c.isStatus());
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }
}
