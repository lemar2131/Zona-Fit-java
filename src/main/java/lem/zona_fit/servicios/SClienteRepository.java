package lem.zona_fit.servicios;

import lem.zona_fit.models.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SClienteRepository {

    List<Cliente> getClientes();

    boolean saveCliente(Cliente cliente);

    boolean updateCliente(Cliente cliente);

    boolean deleteCliente(Integer id);

    List<Cliente> findByNombre(String nombre);


}
