package com.greglturnquist.hackingspringboot.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CartRepository extends ReactiveCrudRepository<Cart, String> {

}