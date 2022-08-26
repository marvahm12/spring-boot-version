/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.hackingspringboot.reactive;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.WebClientConfigurer;
import org.springframework.hateoas.server.core.TypeReferences.CollectionModelType;
import org.springframework.hateoas.server.core.TypeReferences.EntityModelType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Greg Turnquist
 */
// tag::register[]
@SpringBootTest()
@EnableHypermediaSupport(type = HAL) 
@AutoConfigureWebTestClient
public class ApiItemControllerTest {

	@Autowired WebTestClient webTestClient;

	@Autowired ItemRepository repository;

	@Autowired HypermediaWebTestClientConfigurer webClientConfigurer; 

	@BeforeEach
	void setUp() {
		this.webTestClient = this.webTestClient.mutateWith(webClientConfigurer); 
	}
	// end::register[]


	// tag::add-inventory-without-role[]
	@Test
	@WithMockUser(username = "alice", roles = { "SOME_OTHER_ROLE" })
	void addingInventoryWithoutProperRoleFails() {
		this.webTestClient 
				.post().uri("/api/items/add") 
				.contentType(MediaType.APPLICATION_JSON) 
				.bodyValue("{" + 
						"\"name\": \"iPhone X\", " + 
						"\"description\": \"upgrade\", " + 
						"\"price\": 999.99" + 
						"}") 
				.exchange() 
				.expectStatus().isForbidden(); 
	}
	// end::add-inventory-without-role[]

	// tag::add-inventory-with-role[]
	@Test
	@WithMockUser(username = "bob", roles = { "INVENTORY" }) 
	void addingInventoryWithProperRoleSucceeds() {
		this.webTestClient 
				.post().uri("/api/items/add") 
				.contentType(MediaType.APPLICATION_JSON) 
				.bodyValue("{" + 
						"\"name\": \"iPhone X\", " + 
						"\"description\": \"upgrade\", " + 
						"\"price\": 999.99" + 
						"}") 
				.exchange() 
				.expectStatus().isCreated();

		this.repository.findByName("iPhone X") 
				.as(StepVerifier::create) 
				.expectNextMatches(item -> { 
					assertThat(item.getDescription()).isEqualTo("upgrade");
					assertThat(item.getPrice()).isEqualTo(999.99);
					return true; 
				}) 
				.verifyComplete(); 
	}
	// end::add-inventory-with-role[]




}
