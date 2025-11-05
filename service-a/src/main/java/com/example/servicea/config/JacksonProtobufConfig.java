package com.example.servicea.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonProtobufConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer protobufJacksonCustomizer() {
        return builder -> {
            // Ignore Protobuf internals that confuse Jackson when serializing
            builder.mixIn(com.google.protobuf.GeneratedMessageV3.class, ProtobufMessageJacksonMixIn.class);
            builder.mixIn(com.google.protobuf.UnknownFieldSet.class, ProtobufUnknownFieldSetMixIn.class);
        };
    }

    @JsonIgnoreProperties({
            "unknownFields",
            "allFields",
            "parserForType",
            "serializedSize",
            "defaultInstanceForType"
    })
    private static abstract class ProtobufMessageJacksonMixIn { }

    @JsonIgnoreProperties({
            "parserForType"
    })
    private static abstract class ProtobufUnknownFieldSetMixIn { }
}

