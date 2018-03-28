package com.oreilly.security.initializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * not needed for spring boot
 */
public class AppWebAppInitializer // implements WebApplicationInitializer
{
    //@Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();

        context.setConfigLocation("com.oreilly.security.config");

        servletContext.addListener(new ContextLoaderListener(context));

        final ServletRegistration.Dynamic dispatcher = servletContext
                .addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);
    }
}
