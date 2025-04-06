package br.com.fiap.hackaton.processor.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SQSConfig {

//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${cloud.aws.region.static}")
//    private String region;

//    @Bean
//    @Primary
//    public AmazonSQSAsync amazonSQSAsync() {
//        return AmazonSQSAsyncClientBuilder.standard()
//                .withRegion(Regions.fromName(region))
//                .withCredentials(new AWSStaticCredentialsProvider(
//                        new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }
//
//    @Bean
//    public QueueMessagingTemplate queueMessagingTemplate() {
//        return new QueueMessagingTemplate(amazonSQSAsync());
//    }
}
