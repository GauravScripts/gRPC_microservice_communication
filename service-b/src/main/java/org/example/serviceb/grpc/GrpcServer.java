package org.example.serviceb.grpc;


import com.example.grpc.HelloProto;
import com.example.grpc.HelloServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.grpc.stub.StreamObserver;
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
                .addService(ServerInterceptors.intercept(
                        new HelloServiceImpl(),
                        new LoggingServerInterceptor()))
                .build()
                .start();

        System.out.println("✅ Employee gRPC server running on port 9090");
        server.awaitTermination();
    }
    static class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
        @Override
        public void sayHello(HelloProto.HelloRequest request,
                             StreamObserver<HelloProto.HelloResponse> responseObserver) {
            String reply = "Hello, " + request.getName() + "! (via gRPC)";
            HelloProto.HelloResponse response = HelloProto.HelloResponse.newBuilder()
                    .setMessage(reply)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}