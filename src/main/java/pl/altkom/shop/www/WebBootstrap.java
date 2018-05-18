package pl.altkom.shop.www;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import pl.altkom.shop.CoreConfig;
import pl.altkom.shop.lib.Profiles;

public class WebBootstrap implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.getEnvironment().setActiveProfiles(Profiles.WEB);
		ctx.register(CoreConfig.class);
		ctx.setServletContext(container);

		container.addListener(new ContextLoaderListener(ctx));
		container.addListener(RequestContextListener.class);

		Dynamic securityFiler = container.addFilter("springSecurityFilterChain",
				new DelegatingFilterProxy("springSecurityFilterChain"));
		securityFiler.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");

		utf8(container);
	}

	private void utf8(ServletContext container) {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		container.addFilter("characterEncoding", characterEncodingFilter)
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	}
}
