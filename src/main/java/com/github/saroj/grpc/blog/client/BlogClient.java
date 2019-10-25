package com.github.saroj.grpc.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {

        System.out.println("Hello!!! This is blog gRPC Client. ");

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(managedChannel);

        CreateBlogResponse createBlogResponse = blogClient.createBlog(CreateBlogRequest.newBuilder().setBlog(Blog.newBuilder().setAuthorId("Saroj").setTitle("My Blog").setContent("This is my new blog!!!").build()).build());

        System.out.println("created blog " + createBlogResponse.toString());

        System.out.println("Reading Blog with id: " + createBlogResponse.getBlog().getId());
        ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder().setId(createBlogResponse.getBlog().getId()).build());

        System.out.println("Received blog " + readBlogResponse.toString());

        System.out.println("Reading blog with non existing id");

        blogClient.readBlog(ReadBlogRequest.newBuilder().setId("5db18d3a01378e0cc5488e0z").build());

        managedChannel.shutdown();

    }
}
