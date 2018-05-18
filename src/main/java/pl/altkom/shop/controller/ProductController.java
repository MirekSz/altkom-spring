package pl.altkom.shop.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;
import pl.altkom.shop.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Inject
	ProductRepo repo;
	@Inject
	ProductService service;
	@Inject
	RestTemplate restemplate;
	@Inject
	OAuth2AuthorizedClientService clientService;

	@RequestMapping("/list")
	public String list(Model model, @RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "orderBy") String orderBy) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

			OAuth2AuthorizedClient client = clientService
					.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

			model.addAttribute("token", client.getAccessToken().getTokenValue());
			ResponseEntity<Object[]> forEntity = restemplate
					.getForEntity("https://api.github.com/repos/MIrekSz/json-hashing/events", Object[].class);
			model.addAttribute("events", forEntity.getBody());
		} catch (Exception e) {

		}
		model.addAttribute("page", page);
		model.addAttribute("orderBy ", orderBy);

		List<Product> products = repo.getAll();
		model.addAttribute("products", products);

		return "product/product-list";
	}

	@RequestMapping("/list.pdf")
	public String listAsPdf(@RequestParam(required = false, value = "page") Integer page, Model model)
			throws Exception {
		List<Product> products = repo.getAll();
		model.addAttribute("products", products);
		return "productPDFView";
	}

	@RequestMapping("/{id}/delete")
	public String delte(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) throws Exception {
		repo.delete(id);
		return "redirect:/product/list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String prepareForm(Model model) throws Exception {
		Product product = new Product();
		product.setQuantity(1);
		product.setPrice(BigDecimal.TEN);
		model.addAttribute("product", product);
		return "product/product-form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String submitForm(@ModelAttribute @Valid Product product, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			return "product/product-form";
		}

		if (product.getId() != null) {
			repo.update(product);
		} else {
			repo.insert(product);
		}
		return "redirect:/product/list";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) throws Exception {
		Product product = repo.find(id);
		model.addAttribute("product", product);
		return "product/product-form";
	}
}
