package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EspecialidadMedicoService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public MedicoEntity addEspecialidad(Long medicoId, Long especialidadId) throws EntityNotFoundException{
        Optional<MedicoEntity> medicoOpt= medicoRepository.findById(medicoId);
        if (medicoOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el medico con ID: " + medicoId);
        }

        Optional<EspecialidadEntity> especialidadOpt= especialidadRepository.findById(especialidadId);
        if (especialidadOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la especialidad con ID: " + especialidadId);
        }
        MedicoEntity medico = medicoOpt.get();
        EspecialidadEntity especialidad = especialidadOpt.get();
        medico.getEspecialidades().add(especialidad);
        medicoRepository.save(medico);
        return medico;
    }
}
