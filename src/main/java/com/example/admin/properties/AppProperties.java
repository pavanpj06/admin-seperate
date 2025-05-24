package com.example.admin.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;



@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "admin")
public class AppProperties {

	
	Map<String,String> message=new HashMap();
}
