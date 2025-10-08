package br.com.tarefamanager.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tarefaManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tarefa Manager API")
                        .description("API para gerenciamento de tarefas e subtarefas, incluindo controle de status e relacionamento com usuários.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Gustavo Nunes")
                                .email("gustavo.nunes@email.com")
                                .url("https://github.com/gununes031"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação completa no GitHub")
                        .url("https://github.com/gununes031/TarefaManager"));
    }
}
