package lem.zona_fit.servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lem.zona_fit.models.Cliente;
import lem.zona_fit.repositorios.RCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SCliente implements SClienteRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RCliente rCliente;

    @Transactional
    public List<Cliente> getClientes() {
        return rCliente.findAll();
    }

    @Transactional
    public boolean saveCliente(Cliente cliente) {
        if(cliente == null){
            return false;
        }
        rCliente.save(cliente);
        return true;
    }

    @Transactional
    public boolean updateCliente(Cliente cliente) {
        try {
            if (cliente == null) {
                return false;
            }else{
                rCliente.save(cliente);
                return true;}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Transactional
    public boolean deleteCliente(Integer id) {
        try {
            Cliente cliente = entityManager.find(Cliente.class, id);
            if (cliente != null) {
                rCliente.delete(cliente);
                return true;
            }
            return false; // Si el cliente no existe
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Cliente> findByNombre(String nombre){
        try{
            return rCliente.findByNombre(nombre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
