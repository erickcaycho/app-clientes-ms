package com.retail.app.service;

import com.retail.app.dao.ClienteDao;
import com.retail.app.entity.Cliente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ClienteServiceImplTest {

	@Mock
	private ClienteDao clienteDao;
	
	@InjectMocks
	private ClienteServiceImpl clienteService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testGetEdadPromedioYDesviacionEstandar() throws ParseException {
		
		List<Cliente> clientes = new ArrayList<>();

		clientes.add(new Cliente(1L, "Erick", "Caycho", "Ponce", 30, setFecNacimiento("1993-03-15")));
        clientes.add(new Cliente(2L, "Isabel", "Baltazar", "Molina", 25, setFecNacimiento("1998-07-20")));
        clientes.add(new Cliente(3L, "Xiomara", "Caycho", "Ponce", 40, setFecNacimiento("1983-09-10")));

        when(clienteDao.findAll()).thenReturn(clientes);
         
        String resultado = clienteService.getEdadPromedioYDesviacionEstandar();

        assertEquals("{\"edadPromedio\": 31.666667, \"desviacionEstandar\": 6.236096}", resultado);
	}
	
	@Test
    void testListClientes() throws ParseException {
		
		List<Cliente> clientes = new ArrayList<>();
		
		clientes.add(new Cliente(1L, "Juan", "Perez", "Gomez", 30, setFecNacimiento("1990-05-10")));
        clientes.add(new Cliente(2L, "Maria", "Lopez", "Garcia", 25, setFecNacimiento("1985-08-25")));
        clientes.add(new Cliente(3L, "Carlos", "Gutierrez", "Sanchez", 40, setFecNacimiento("1982-03-15")));
        clientes.add(new Cliente(4L, "Laura", "Gonzalez", "Martinez", 28, setFecNacimiento("1995-11-30")));
        
        when(clienteDao.findAll()).thenReturn(clientes);
        List<Cliente> resultado = clienteService.findAll();
        assertEquals(clientes, resultado);
	}
	
    @Test
    void testGuardarCliente() throws ParseException {

        Cliente cliente = new Cliente(1L, "Juan", "Perez", "Gomez", 30, setFecNacimiento("1990-05-10"));
        when(clienteDao.save(cliente)).thenReturn(cliente);

        Cliente savedCliente = clienteService.agregarCliente(cliente);

        assertEquals(cliente, savedCliente);
        verify(clienteDao, times(1)).save(cliente);
    }
    
	private Date setFecNacimiento(String fecNacimiento) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(fecNacimiento);
    }
	
    @Test
    void testGetListClientesWithFechaFallecimientos() throws ParseException {
   
        List<Cliente> clientes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        clientes.add(new Cliente(1L, "Juan", "Perez", "Gomez", 30, sdf.parse("1990-05-10")));
         
        when(clienteDao.findAll()).thenReturn(clientes);

        List<String> result = clienteService.getListClientesWithFechaFallecimiento();

        assertEquals(clientes.size(), result.size());
        verify(clienteDao, times(1)).findAll();
    }    
}
