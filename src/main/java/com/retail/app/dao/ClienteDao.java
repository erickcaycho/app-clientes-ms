package com.retail.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.retail.app.entity.Cliente;

public interface ClienteDao extends CrudRepository<Cliente, Long>{	
}