package com.retail.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.app.entity.Cliente;
import com.retail.app.service.IClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


/*
 * Controller ClienteController
 * @author  Erick Caycho Ponce
 * @version 1.0
 * @since   2023-07-21
 */

@RestController
@RequestMapping("/retail")
@Api(tags = "Cliente Controller", description = "Operaciones relacionadas con clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@ApiOperation(value = "Obtener la lista de clientes con fecha estimada de fallecimiento",
            notes = "Devuelve una lista de clientes con su información y la fecha estimada de fallecimiento.")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Operación exitosa", response = String.class, responseContainer = "List"),
    		@ApiResponse(code = 500, message = "Error interno del servidor", response = String.class, responseContainer = "List")
    })
	@GetMapping("/listclientes")
	public ResponseEntity<List<String>> listar(){
		try {
            List<String> clientesConFechasFallecimiento = clienteService.getListClientesWithFechaFallecimiento();
            return new ResponseEntity<>(clientesConFechasFallecimiento, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
	@ApiOperation(value = "Crear un nuevo cliente",
            notes = "Crea un nuevo cliente con la información proporcionada.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Cliente creado exitosamente", response = Cliente.class),
			@ApiResponse(code = 400, message = "Solicitud inválida", response = Cliente.class),
			@ApiResponse(code = 500, message = "Error interno del servidor", response = Cliente.class)
    })
	@PostMapping("/creacliente")
	public ResponseEntity<Cliente> createCliente (@RequestBody Cliente cliente) {
		
		try {
            Cliente nuevoCliente = clienteService.agregarCliente(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}
	
	@ApiOperation(value = "Obtener el promedio de edad y la desviación estándar de las edades de los clientes",
            notes = "Devuelve el promedio de edad y la desviación estándar de las edades de todos los clientes registrados.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operación exitosa", response = String.class),
			@ApiResponse(code = 500, message = "Error interno del servidor", response = String.class)
    })
	@GetMapping("/kpiclientes")
	public ResponseEntity<String> getEdadPromedioYDesviacionEstandar() {
		try {
            String kpiClientes = clienteService.getEdadPromedioYDesviacionEstandar();
            return new ResponseEntity<>(kpiClientes, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Ha ocurrido un error en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
