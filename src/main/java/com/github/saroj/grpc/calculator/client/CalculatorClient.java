package com.github.saroj.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(managedChannel);

        SumRequest sumRequest = SumRequest.newBuilder().setFirstNumber(10).setSecondNumber(20).build();

        SumResponse sumResponse = calculatorClient.sum(sumRequest);

        System.out.println(sumResponse);

    }
}
