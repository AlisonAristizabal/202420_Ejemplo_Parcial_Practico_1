package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoService {
    
    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public MedicoEntity createMedico(MedicoEntity medico) throws IllegalOperationException{
        log.info("Inicia el proceso de creación del medico.");
        String registroMedico = medico.getRegistroMedico();
        if (registroMedico == null || !registroMedico.startsWith("RM")){
            throw new IllegalOperationException("El registro médico debe comenzar con 'RM'.");
        }
        log.info("Médico creado exitosamente.");
    
        return medicoRepository.save(medico);
    }
}
