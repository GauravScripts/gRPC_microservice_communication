package com.example.servicea;


import io.grpc.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import com.google.protobuf.MessageLite;

public class LoggingClientInterceptor implements ClientInterceptor {

    private static final Logger log = Logger.getLogger(LoggingClientInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {
            @Override
            public void sendMessage(ReqT message) {
                // Serialize the message to get raw bytes
                byte[] binaryPayload = new byte[0];
                try {
                    if (message instanceof MessageLite ml) {
                        binaryPayload = ml.toByteArray();
                    } else {
                        try (InputStream in = method.getRequestMarshaller().stream(message)) {
                            binaryPayload = in.readAllBytes();
                        }
                    }
                } catch (IOException e) {
                    log.warning("Failed to serialize gRPC request to bytes: " + e.getMessage());
                }
                log.info("\uD83D\uDCE1 gRPC OUTGOING BINARY (hex): " + bytesToHex(binaryPayload));
                super.sendMessage(message);
            }
        };
    }

    // Helper: Convert byte[] to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}