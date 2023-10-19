package com.xorshop.repository;
import org.springframework.data.repository.CrudRepository;

import com.xorshop.common.entity.Currency;
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
