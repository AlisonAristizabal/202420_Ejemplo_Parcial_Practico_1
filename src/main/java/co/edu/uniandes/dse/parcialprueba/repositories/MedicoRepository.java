package co.edu.uniandes.dse.parcialprueba.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import java.util.List;


@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Long>{
    
    List<MedicoEntity> findByNombre(String nombre);
    
} 
