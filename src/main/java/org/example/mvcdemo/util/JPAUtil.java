package org.example.mvcdemo.util;

import jakarta.persistence.*;

public final class JPAUtil {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("productPU");

    private JPAUtil() {}

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void close() {
        if(EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}


