package com.github.saroj.grpc.greeting.server;

import com.proto.greet.GreetEveryoneRequest;
import com.proto.greet.GreetEveryoneResponse;
import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetManyTimesResponse;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.GreetWithDeadlineRequest;
import com.proto.greet.GreetWithDeadlineResponse;
import com.proto.greet.LongGreetRequest;
import com.proto.greet.LongGreetResponse;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        String greetResult = "Hello " + firstName + "!!!!";

        GreetResponse greetResponse = GreetResponse.newBuilder().setResult(greetResult).build();
        responseObserver.onNext(greetResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();

        try {
            for (int i = 0; i < 10; i++) {
                String greetResult = "Hello " + firstName + "!!!!" + "response number: " + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(greetResult).build();
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest longGreetRequest) {
                result = result + "Hello " + longGreetRequest.getGreeting().getFirstName() + "!!!";
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        StreamObserver<GreetEveryoneRequest> requestObserver = new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest greetEveryoneRequest) {
                String result = "Hello " + greetEveryoneRequest.getGreeting().getFirstName() + "!!!";
                responseObserver.onNext(GreetEveryoneResponse.newBuilder().setResult(result).build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }

    @Override
    public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {
        Context context = Context.current();
        try {
            for (int i = 0; i < 3; i++) {
                if (!context.isCancelled()) {
                    System.out.println("Sleeping for "+ i + " 100 second");
                    Thread.sleep(100);
                } else {
                    return;
                }
            }
            responseObserver.onNext(GreetWithDeadlineResponse.newBuilder().setResult("Hello " + request.getGreeting().getFirstName() + "!!!!").build());
            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
