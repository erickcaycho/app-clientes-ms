package com.retail.app.service;

import java.util.List;

import com.retail.app.entity.Cliente;

public interface IClienteService {
	
	public Cliente agregarCliente(Cliente cliente);
	public List<Cliente> findAll();
	public List<String> getListClientesWithFechaFallecimiento();
	public String getEdadPromedioYDesviacionEstandar();
}
