package org.example.mvcdemo.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.*;

@WebListener
public class JpaContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce){
        JPAUtil.getEntityManager().close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
