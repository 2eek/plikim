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



public  int markRead(String userId, String roomNum, String uri) {
        System.out.println("업데이터1"+userId+"##"+roomNum);

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("chatdb"); // Replace with your actual database name
        MongoCollection<Document> collection = database.getCollection("chat"); // Replace with your actual collection name

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

//public void markReadOne(Chat chat, String uri) {
//    // MongoDB 연결 설정
//    ConnectionString connectionString = new ConnectionString(uri);
//    MongoClientSettings settings = MongoClientSettings.builder()
//            .applyConnectionString(connectionString)
//            .build();
//
//    // MongoDB 클라이언트 생성
//    try (MongoClient mongoClient = MongoClients.create(settings)) {
//        // MongoDB 데이터베이스 및 컬렉션 설정
//        MongoDatabase database = mongoClient.getDatabase("chatdb"); // 실제 데이터베이스 이름으로 대체
//        MongoCollection<Document> collection = database.getCollection("chat"); // 실제 컬렉션 이름으로 대체
//
//        // 업데이트할 문서를 찾기 위한 쿼리 생성
//        Document query = new Document("_id", new ObjectId(chat.getId())); // chat 객체의 ID 필드를 사용하여 업데이트할 문서를 찾음
//
//        // 업데이트할 내용을 설정한 문서 생성
//        Document update = new Document("$set", new Document("read", 0));
//
//        // 단일 문서 업데이트 수행
//        UpdateResult updateResult = collection.updateOne(query, update);
//
//        // 업데이트 결과 확인
//        System.out.println("업데이트된 문서 수: " + updateResult.getModifiedCount());
//    } catch (MongoException e) {
//        // MongoDB 연결이나 업데이트 중에 발생하는 예외 처리
//        e.printStackTrace();
//    }
//}
}