package com.greglturnquist.hackingspringboot.reactive;

import reactor.core.publisher.Flux;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
Flux<Item> findByNameContaining(String partialName);

  @Query("{ 'name' : ?0, 'age' : ?1 }")
  Flux<Item> findItemsForCustomerMonthlyReport(String name, int age);

 // @Query(sort = "{ 'age' : -1 }")
 // Flux<Item> findSortedStuffForWeeklyReport();

  // search by name
  Flux<Item> findByNameContainingIgnoreCase(String partialName);

  // search by description
  Flux<Item> findByDescriptionContainingIgnoreCase(String partialName);

  // search by name AND description
  Flux<Item> findByNameContainingAndDescriptionContainingAllIgnoreCase(
    String partialName,
    String partialDesc
  );

  // search by name OR description
  Flux<Item> findByNameContainingOrDescriptionContainingAllIgnoreCase(
    String partialName,
    String partialDesc
  );
}