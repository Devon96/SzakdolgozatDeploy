package hu.utazo.utazo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef="auditor")
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditor(){
        return new AuditorAwareImpl();
    }
}
