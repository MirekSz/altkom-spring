package pl.altkom.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.altkom.shop.repo.SpringDataProductRepo;
import pl.altkom.shop.service.ProductService;

public class Runner {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CoreConfig.class);
		ProductService productService = (ProductService) context.getBean("productService");
		System.out.println(productService);
		SpringDataProductRepo repo = context.getBean(SpringDataProductRepo.class);
		System.out.println("$$$$$$$$$$$");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(repo.findByName("Sanki"));
		System.out.println("$$$$$$$$$$$");
		System.out.println(repo.findByName("Sanki"));
		repo.promotion(BigDecimal.ONE);
		System.out.println("$$$$$$$$$$$");
		System.out.println(repo.findByName("Sanki"));
		System.out.println(repo.findByName("Sanki").get(0).getDetails());
	}

}
