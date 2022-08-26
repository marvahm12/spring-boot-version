package com.greglturnquist.hackingspringboot.reactive;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

  private InventoryService inventoryService;

	public HomeController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	// end::1[]

	@GetMapping
	Mono<Rendering> home() {
		// tag::2[]
		return Mono.just(Rendering.view("home.html") //
			.modelAttribute("items", this.inventoryService.getInventory()) //
			.modelAttribute("cart", this.inventoryService.getCart("My Cart") //
				.defaultIfEmpty(new Cart("My Cart")))
			.build());
		// end::2[]
	}

	@PostMapping("/add/{id}")
	Mono<String> addToCart(@PathVariable String id) {
		return this.inventoryService.addItemToCart("My Cart", id)
			.thenReturn("redirect:/");
	}

	@DeleteMapping("/remove/{id}")
	Mono<String> removeFromCart(@PathVariable String id) {
		return this.inventoryService.removeOneFromCart("My Cart", id)
			.thenReturn("redirect:/");
	}

	@PostMapping
	Mono<String> createItem(@ModelAttribute Item newItem) {
		return this.inventoryService.saveItem(newItem) //
			.thenReturn("redirect:/");
	}

	@DeleteMapping("/delete/{id}")
	Mono<String> deleteItem(@PathVariable String id) {
		return this.inventoryService.deleteItem(id) //
			.thenReturn("redirect:/");
	}
}
