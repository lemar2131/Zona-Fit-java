package lem.zona_fit.repositorios;

import lem.zona_fit.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RCliente extends JpaRepository<Cliente, Integer> {

    @Query("select c from Cliente c WHERE c.nombre like %:nombre%")
    List<Cliente> findByNombre(@Param("nombre") String nombre);



}
