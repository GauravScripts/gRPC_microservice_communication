package org.example.serviceb.grpc;

import io.grpc.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
public class LoggingServerInterceptor implements ServerInterceptor {
    private static final Logger log = Logger.getLogger(LoggingServerInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall<ReqT, RespT> interceptedCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendMessage(RespT message) {
                // Serialize response to bytes for logging
                try (InputStream is = call.getMethodDescriptor().getResponseMarshaller().stream(message)) {
                    byte[] binary = is.readAllBytes();
                    log.info("\uD83D\uDCE8 gRPC OUTGOING RESPONSE BINARY (hex): " + bytesToHex(binary));
                } catch (IOException e) {
                    log.warning("Failed to log outgoing response bytes: " + e.getMessage());
                }
                super.sendMessage(message);
            }
        };

        ServerCall.Listener<ReqT> listener = next.startCall(interceptedCall, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onMessage(ReqT message) {
                // Serialize request to bytes for logging
                try (InputStream is = call.getMethodDescriptor().getRequestMarshaller().stream(message)) {
                    byte[] binary = is.readAllBytes();
                    log.info("\uD83D\uDCE9 gRPC INCOMING REQUEST BINARY (hex): " + bytesToHex(binary));
                } catch (IOException e) {
                    log.warning("Failed to log incoming request bytes: " + e.getMessage());
                }
                super.onMessage(message);
            }
        };
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}