package com.embedded_system.Dao;

import com.embedded_system.Entity.Logger;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by onion on 4/11/2017.
 */
@Transactional
public interface LoggerDao extends CrudRepository<Logger,Long> {
}
