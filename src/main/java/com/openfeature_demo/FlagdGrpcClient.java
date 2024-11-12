package com.openfeature_demo;

import com.google.protobuf.Struct;
import dev.openfeature.flagd.grpc.evaluation.Evaluation.ResolveBooleanRequest;
import dev.openfeature.flagd.grpc.evaluation.Evaluation.ResolveBooleanResponse;
import dev.openfeature.flagd.grpc.evaluation.ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.protobuf.Value;


public class FlagdGrpcClient {
    public static void main(String[] args) {
        // Create a channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8013)
                .usePlaintext()
                .build();

        try {
            // Create a stub
            ServiceGrpc.ServiceBlockingStub stub = ServiceGrpc.newBlockingStub(channel);

            Struct context = Struct.newBuilder()
                    .putFields("role", Value.newBuilder().setStringValue("client").build())
                    .build();

            // Create the request
            ResolveBooleanRequest request = ResolveBooleanRequest.newBuilder()
                    .setFlagKey("newFeature")
                    .setContext(context)
                    .build();

            // Make the call
            ResolveBooleanResponse response = stub.resolveBoolean(request);

            // Process the response
            System.out.println("Value: " + response.getValue());
            System.out.println("Reason: " + response.getReason());
            System.out.println("Variant: " + response.getVariant());
        } finally {
            // Shutdown the channel
            channel.shutdown();
        }
    }
}