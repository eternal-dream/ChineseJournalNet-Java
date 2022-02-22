package com.cqvip.innocence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Innocence
 */
@SpringBootApplication(exclude = SolrRepositoriesAutoConfiguration.class)
@MapperScan("com.cqvip.innocence.project.**.mapper")//配置后可以不用在Mapper接口上写@Mapper注解
@EnableOpenApi
@EnableTransactionManagement
public class InnocenceApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(InnocenceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(InnocenceApplication.class, args);
    }

}
