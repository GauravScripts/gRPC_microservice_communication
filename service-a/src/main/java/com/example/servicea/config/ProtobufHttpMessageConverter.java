package com.example.servicea.config;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Minimal JSON HttpMessageConverter for Protobuf Messages using JsonFormat.
 * This converter supports types that extend com.google.protobuf.Message.
 */
public class ProtobufHttpMessageConverter extends AbstractHttpMessageConverter<Message> {

    public ProtobufHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Message.class.isAssignableFrom(clazz);
    }

    @Override
    protected Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage) throws IOException {
        try {
            Message.Builder builder = (Message.Builder) clazz.getMethod("newBuilder").invoke(null);
            String json = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
            return (Message) builder.build();
        } catch (ReflectiveOperationException e) {
            throw new IOException("Failed to build protobuf message via reflection for " + clazz.getName(), e);
        }
    }

    @Override
    protected void writeInternal(Message message, HttpOutputMessage outputMessage) throws IOException {
        String json = JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace().print(message);
        outputMessage.getBody().write(json.getBytes(StandardCharsets.UTF_8));
    }
}

