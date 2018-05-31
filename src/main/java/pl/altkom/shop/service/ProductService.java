package pl.altkom.shop.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import pl.altkom.shop.lib.ProductChangeEvent;
import pl.altkom.shop.repo.ProductRepo;

public class ProductService {
	@Autowired
	Logger log;

	@Autowired
	private ProductRepo repo;

	@Autowired
	ObjectProvider<Logger> loggerLazy;

	@EventListener
	void onChange(ProductChangeEvent event) {
		loggerLazy.getObject();
		log.info(event);
	}
}
