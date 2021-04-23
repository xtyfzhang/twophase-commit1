package com.algorithm.twophasecommit.utils;

import com.algorithm.twophasecommit.cliApi.ClientApi;
import feign.Feign;

/**
 * feign自定义服务
 */
public class FeignUtils {

    public static ClientApi createFeignService(String url, String port){

        return Feign.builder()

              //  .decoder(new GsonDecoder())

                .target(ClientApi.class, "https://api.github.com");
    }

}
