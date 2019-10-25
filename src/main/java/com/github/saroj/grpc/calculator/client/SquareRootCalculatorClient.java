package com.github.saroj.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SquareRootRequest;
import com.proto.calculator.SquareRootResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SquareRootCalculatorClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub squareRootClient = CalculatorServiceGrpc.newBlockingStub(channel);

        try {
            SquareRootResponse squareRootResponse = squareRootClient.squareRoot(SquareRootRequest.newBuilder().setNumber(5).build());
            System.out.println("Square root = " + squareRootResponse.getNumberRoot());

            squareRootResponse = squareRootClient.squareRoot(SquareRootRequest.newBuilder().setNumber(-100).build());
        } catch (Exception e) {
            System.out.println("Received exception for square root");
            e.printStackTrace();
        }


    }
}
