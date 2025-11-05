package org.example.serviceb.grpc;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(ServerInterceptors.intercept(
                        new EmployeeServiceImpl(),
                        new LoggingServerInterceptor()))
                .build()
                .start();

        System.out.println("✅ Employee gRPC server running on port 9090");
        server.awaitTermination();
    }
}