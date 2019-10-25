package com.github.saroj.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {


    private MongoClient mongoClient =  MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("blogdb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {



        Blog blog = request.getBlog();

        System.out.println("Received Blog " + blog.toString());

        Document document = new Document("author_id", blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());

        System.out.println("Inserting blog to mongodb....");

        collection.insertOne(document);

        System.out.println("Inserted blog to mongodb....");

        String id = document.getObjectId("_id").toString();

        System.out.println("Received id for created blog in mongodb....");

        CreateBlogResponse createBlogResponse = CreateBlogResponse.newBuilder().setBlog(blog.toBuilder().setId(id).build()).build();

        responseObserver.onNext(createBlogResponse);

        responseObserver.onCompleted();

    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {

        String blogId = request.getId();
        System.out.println("Received request to read blog:" + blogId);

        Document blogDocument = collection.find(Filters.eq("_id", new ObjectId(blogId))).first();

        if (blogDocument == null) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Provided id not found").asRuntimeException());
        } else {
            responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(Blog.newBuilder().setId(blogDocument.getObjectId("_id").toString()).setTitle(blogDocument.getString("title")).setContent(blogDocument.getString("content")).setAuthorId(blogDocument.getString("author_id")).build()).build());
            responseObserver.onCompleted();
        }
    }
}
