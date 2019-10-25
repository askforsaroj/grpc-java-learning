package com.github.saroj.grpc.calculator.server;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SquareRootRequest;
import com.proto.calculator.SquareRootResponse;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        int firstNumber = request.getFirstNumber();
        int secondNumber = request.getSecondNumber();
        int sumResult = firstNumber + secondNumber;
        SumResponse sumResponse = SumResponse.newBuilder().setSumResult(sumResult).build();
        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        double number = request.getNumber();
        if(number >= 0) {
            responseObserver.onNext(SquareRootResponse.newBuilder().setNumberRoot(Math.sqrt(number)).build());
            responseObserver.onCompleted();
        } else {
            //Construct the Exception
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Negative numbers not supported").asRuntimeException());
        }
    }
}
