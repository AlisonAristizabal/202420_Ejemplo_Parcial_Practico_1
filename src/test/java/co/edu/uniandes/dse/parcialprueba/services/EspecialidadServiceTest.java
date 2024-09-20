package co.edu.uniandes.dse.parcialprueba.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(EspecialidadService.class)
public class EspecialidadServiceTest {

   @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
        clearData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM EspecialidadEntity").executeUpdate();
    }

    // Test para la creación exitosa de una especialidad
    @Test
    void testCreateEspecialidad() throws IllegalOperationException {
        EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
        newEntity.setDescripcion("Descripción válida de más de 10 caracteres.");
        
        EspecialidadEntity result = especialidadService.createEspecialidad(newEntity);

        assertNotNull(result);
        EspecialidadEntity foundEntity = entityManager.find(EspecialidadEntity.class, result.getId());
        assertEquals(newEntity.getDescripcion(), foundEntity.getDescripcion());
        assertEquals(newEntity.getNombre(), foundEntity.getNombre());
    }

    // Test donde se lanza una excepción debido a la violación de la regla de negocio
    @Test
    void testCreateEspecialidadWithInvalidDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            EspecialidadEntity newEntity = factory.manufacturePojo(EspecialidadEntity.class);
            newEntity.setDescripcion("Corto");  // Descripción con menos de 10 caracteres
            especialidadService.createEspecialidad(newEntity);
        });
    }
}
