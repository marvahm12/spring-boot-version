package com.greglturnquist.hackingspringboot.reactive;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository 
public interface ItemByExampleRepository extends ReactiveQueryByExampleExecutor<Item> {

    
}