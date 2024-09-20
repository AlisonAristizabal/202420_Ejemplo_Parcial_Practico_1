package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(EspecialidadMedicoService.class)
public class EspecialidadMedicoServiceTest {

    @Autowired
    private EspecialidadMedicoService especialidadMedicoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private MedicoEntity medico;
    private EspecialidadEntity especialidad;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM MedicoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM EspecialidadEntity").executeUpdate();
    }

    private void insertData() {
        // Insertamos un médico
        medico = factory.manufacturePojo(MedicoEntity.class);
        medico.setId(null);  // Asegúrate de que el ID sea null para que se genere un nuevo ID al persistir
        entityManager.persist(medico);  // Usamos persist para nuevas entidades
        
        // Insertamos una especialidad
        especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        especialidad.setId(null);  // Asegúrate de que sea nuevo y no tenga ID
        entityManager.persist(especialidad);  // Usamos persist para nuevas entidades
    }
    

    // Test para agregar correctamente una especialidad a un médico
    @Test
    void testAddEspecialidadToMedico() throws EntityNotFoundException {
        MedicoEntity result = especialidadMedicoService.addEspecialidad(medico.getId(), especialidad.getId());
        assertNotNull(result);
        assertEquals(1, result.getEspecialidades().size());
        assertEquals(especialidad.getId(), result.getEspecialidades().get(0).getId());
    }

    // Test donde se lanza una excepción porque el médico no existe
    @Test
    void testAddEspecialidadToNonExistentMedico() {
        Long nonExistentMedicoId = 999L; // ID no existente
        assertThrows(EntityNotFoundException.class, () -> {
            especialidadMedicoService.addEspecialidad(nonExistentMedicoId, especialidad.getId());
        });
    }

    // Test donde se lanza una excepción porque la especialidad no existe
    @Test
    void testAddNonExistentEspecialidadToMedico() {
        Long nonExistentEspecialidadId = 999L; // ID no existente
        assertThrows(EntityNotFoundException.class, () -> {
            especialidadMedicoService.addEspecialidad(medico.getId(), nonExistentEspecialidadId);
        });
    }
}
