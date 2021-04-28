package com.algorithm.twophasecommit.utils;

import com.algorithm.twophasecommit.cliApi.ClientApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * feign自定义服务
 */
public class FeignUtils {

    public static ClientApi createFeignService(String serverAddr){
        HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(new ObjectMapper());
        ObjectFactory<HttpMessageConverters> converter = ()-> new HttpMessageConverters(jsonConverter);
        return Feign.builder()
                .encoder(new SpringEncoder(converter))
                .decoder(new SpringDecoder(converter))
                //.contract(new SpringMvcContract())
                .contract(new feign.Contract.Default())
                .target(ClientApi.class, "http://"+serverAddr+"/");
    }

}
