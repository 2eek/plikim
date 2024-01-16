package com.eek.kimpli.chat.service;


import com.eek.kimpli.chat.model.Chat;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;


import com.mongodb.MongoClientURI;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatUpdater {

//    private String uri;

    @Value("${spring.data.mongodb.uri}")
    private String uri;


    public int markRead(String userId, String roomNum, String uri) {
//        System.out.println("업데이터1" + userId + "##" + roomNum);

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("chatdb");
        MongoCollection<Document> collection = database.getCollection("chat");

        // Create a query to find the document to update
        Document query = new Document("receiver", userId).append("roomNum", roomNum);

        // Create an update with the desired changes
        Document update = new Document("$set", new Document("read", 0));

        // Perform the update operation
        collection.updateMany(query, update);

        // Close the MongoDB connection
        mongoClient.close();
        return 1;
    }

}