package com.retail.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.app.dao.ClienteDao;
import com.retail.app.entity.Cliente;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	private ClienteDao clienteDao;
	
	@Override
	public Cliente agregarCliente(Cliente cliente) {		
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Override
	public List<String> getListClientesWithFechaFallecimiento() {
		List<String> clientesConFechasFallecimiento = new ArrayList<>();
		
		List<Cliente> clientes = (List<Cliente>) clienteDao.findAll();
		
		for (Cliente cliente : clientes) {
			int edadActual = cliente.getEdad();
			Date fecNacimiento = cliente.getFecNacimiento();		          
            String fechaFallecimientoSimuladaStr = getFallecimientoEstimado(fecNacimiento,edadActual);
            
            try {
            	ObjectMapper objectMapper = new ObjectMapper();
                String clienteConFechaFallecimientoJSON = objectMapper.writeValueAsString(cliente);
                clienteConFechaFallecimientoJSON = clienteConFechaFallecimientoJSON.replace("}", ", \"fecProbableFallecimiento\": \"" + fechaFallecimientoSimuladaStr + "\"}");
                clientesConFechasFallecimiento.add(clienteConFechaFallecimientoJSON);
			} catch (Exception e) {
				e.printStackTrace();
			}       
		}
		
		return clientesConFechasFallecimiento;
	}
	
	@Override
	public String getEdadPromedioYDesviacionEstandar() {
		List<Cliente> clientes = (List<Cliente>) clienteDao.findAll();
		
		int totalClientes = clientes.size();
        double sumaEdades = clientes.stream()
                .mapToInt(Cliente::getEdad)
                .sum();
        
        double edadPromedio = sumaEdades / totalClientes;

        double sumaDiferenciasAlCuadrado = clientes.stream()
                .mapToDouble(cliente -> Math.pow(cliente.getEdad() - edadPromedio, 2))
                .sum();

        double desviacionEstandar = Math.sqrt(sumaDiferenciasAlCuadrado / totalClientes);

        return String.format("{\"edadPromedio\": %f, \"desviacionEstandar\": %f}", edadPromedio, desviacionEstandar);
        
	}
		
    private String getFallecimientoEstimado(Date fecNacimiento, int edadActual) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fechaNacimientoStr=formatDate(fecNacimiento);
		LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);
        LocalDate fechaFallecimientoEstimada = fechaNacimiento.plusYears(edadActual + getRandomValue(70, 90)); // Rango de edad estimada para el fallecimiento: entre 70 y 90 años

        return fechaFallecimientoEstimada.format(formatter);
    }

    private int getRandomValue(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
    
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
