package pl.altkom.shop.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class CustomScopeRegisteringBeanFactoryPostProcessor
		implements BeanFactoryPostProcessor, ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactory.registerScope("thread", new SimpleThreadScope());
	}

	@Bean
	@Primary
	public RequestContext crate() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return (RequestContext) applicationContext.getBean("threadRequestContextImpl");
		}
		return (RequestContext) applicationContext.getBean("requestContextImpl");

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
