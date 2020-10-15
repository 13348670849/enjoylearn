package com.enjoy;

import com.enjoy.config.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@ComponentScan("com.enjoy")
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@Import({ DataSourceConfig.class })
public class Trans2PC {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Trans2PC.class, args);

	}
}
