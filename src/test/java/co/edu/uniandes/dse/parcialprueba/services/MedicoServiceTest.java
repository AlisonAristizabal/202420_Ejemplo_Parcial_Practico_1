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

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MedicoService.class)
public class MedicoServiceTest {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
        clearData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM MedicoEntity").executeUpdate();
    }

    // Test para la creación exitosa de un médico
    @Test
    void testCreateMedico() throws IllegalOperationException {
        MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
        newEntity.setRegistroMedico("RM123456");  // Aseguramos que el registro sea válido
        
        MedicoEntity result = medicoService.createMedico(newEntity);

        assertNotNull(result);
        MedicoEntity foundEntity = entityManager.find(MedicoEntity.class, result.getId());
        assertEquals(newEntity.getRegistroMedico(), foundEntity.getRegistroMedico());
        assertEquals(newEntity.getNombre(), foundEntity.getNombre());
        assertEquals(newEntity.getApellido(), foundEntity.getApellido());
    }

    // Test donde se lanza una excepción debido a la violación de la regla de negocio
    @Test
    void testCreateMedicoWithInvalidRegistro() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
            newEntity.setRegistroMedico("123456");  // Registro que no cumple con la regla de negocio
            medicoService.createMedico(newEntity);
        });
    }
}
