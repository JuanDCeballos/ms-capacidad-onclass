package co.onclass.config;

import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.capacidadbootcamp.gateways.CapacidadBootcampRepository;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public CapacidadRepository capacidadRepository() {
            return Mockito.mock(CapacidadRepository.class);
        }

        @Bean
        public TecnologiaGateway tecnologiaGateway() {
            return Mockito.mock(TecnologiaGateway.class);
        }

        @Bean
        public CapacidadBootcampRepository capacidadBootcampRepository() {
            return Mockito.mock(CapacidadBootcampRepository.class);
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}