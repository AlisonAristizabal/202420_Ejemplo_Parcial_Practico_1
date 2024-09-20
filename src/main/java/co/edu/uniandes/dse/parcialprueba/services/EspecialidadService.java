package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public EspecialidadEntity createEspecialidad(EspecialidadEntity especialidad) throws IllegalOperationException{
        log.info("Inicia el proceso de creación de las especialidad.");
        String descripcion = especialidad.getDescripcion();
        if (descripcion == null || descripcion.length()<10){
            throw new IllegalOperationException("El registro médico debe tener al menos 10 caracteres.");
        }
        log.info("Especialidad creado exitosamente.");
    
        return especialidadRepository.save(especialidad);
    }
}
