package com.extra.einvoicing.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author aibrahim
 *
 */
@Component
public class ApplicationSpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		setContext(context);
	}

	private static synchronized void setContext(ApplicationContext context) {
		ApplicationSpringContext.context = context;
	}

	public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
