package com.embedded_system.Dao;

import com.embedded_system.Entity.Customer;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by onion on 4/11/2017.
 */
@Transactional
public interface CustomerDao extends CrudRepository<Customer,Long> {
}
