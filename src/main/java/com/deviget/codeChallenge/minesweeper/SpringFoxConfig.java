

@Configuration
public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { //TODO: add RequestHandlerSelectors.basePackage & apiInfo
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
}