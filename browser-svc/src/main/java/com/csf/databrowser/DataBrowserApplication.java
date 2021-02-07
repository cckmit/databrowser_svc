package com.csf.databrowser;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author allen.jin
 * @date 2020/12/30
 */

@EnableApolloConfig
@SpringBootApplication
@ComponentScan(basePackages = {"com.csf.common.http.config","com.csf.databrowser","cn.afterturn.easypoi"})
public class DataBrowserApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataBrowserApplication.class);
    }
}
