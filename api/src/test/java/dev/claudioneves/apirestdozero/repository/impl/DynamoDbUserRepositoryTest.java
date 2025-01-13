package dev.claudioneves.apirestdozero.repository.impl;


import dev.claudioneves.apirestdozero.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamoDbUserRepositoryTest {

    public DynamoDbUserRepository dynamoDbUserRepository;

    @Mock
    DynamoDbClient dynamoDbClient;

    @BeforeAll
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.dynamoDbUserRepository = new DynamoDbUserRepository(dynamoDbClient);
        ReflectionTestUtils.setField(this.dynamoDbUserRepository, "tableName","dev-users");
    }

    @BeforeEach
    void resetMocks(){
        reset(this.dynamoDbClient);
    }


    @Test
    void itShouldCreateAnUser(){

        var user = User.builder()
                .name("claudioneves")
                .email("cfneguacu@hotmail.com")
                .password("123456")
                .build();

        this.dynamoDbUserRepository.createUser(user);

        var capture = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(this.dynamoDbClient, times(1)).putItem(capture.capture());

        var request = capture.getValue();
        assertEquals("dev-users", request.tableName());
        assertEquals("claudioneves", request.item().get("name").s());
        assertEquals("cfneguacu@hotmail.com",request.item().get("email").s());
        assertEquals("123456", request.item().get("password").s());
        assertNotNull(request.item().get("created_at").s());
        assertNotNull(request.item().get("updated_at").s());

    }

    @Test
    void testDeleteUser(){

        var userId= UUID.randomUUID();

        this.dynamoDbUserRepository.deleteUserById(userId);

        var capture = ArgumentCaptor.forClass(DeleteItemRequest.class);

        verify(this.dynamoDbClient, times(1)).deleteItem(capture.capture());
        var request = capture.getValue();
        assertEquals("dev-users", request.tableName());
        assertEquals(userId.toString(), request.key().get("id").s());

    }

    @Test
    void itShouldReturnAnUserByEmailIfUserExistsOnDynamoDb() {
        // given
        var email = "john.doe@gmail.com";

        var map = Map.of(
                "id", AttributeValue.builder().s(UUID.randomUUID().toString()).build(),
                "name", AttributeValue.builder().s("Petrus de Melo").build(),
                "email", AttributeValue.builder().s("petrusdemelo@gmail.com").build(),
                "password", AttributeValue.builder().s("123456").build(),
                "created_at", AttributeValue.builder().s(String.valueOf(LocalDateTime.now())).build(),
                "updated_at", AttributeValue.builder().s(String.valueOf(LocalDateTime.now())).build()
        );

        var response = QueryResponse.builder()
                .count(1)
                .items(List.of(map))
                .build();

        when(this.dynamoDbClient.query(any(QueryRequest.class))).thenReturn(response);

        // when
        var result = this.dynamoDbUserRepository.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        verify(this.dynamoDbClient, times(1)).query(any(QueryRequest.class));
        var user = result.get();
        assertEquals("Petrus de Melo", user.getName());
        assertEquals("petrusdemelo@gmail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void itShouldReturnEmptyIfUserDoesntExistByEmail() {
        // given
        var email = "john.doe@gmail.com";

        var response = QueryResponse.builder()
                .count(0)
                .items(Collections.emptyList())
                .build();

        when(this.dynamoDbClient.query(any(QueryRequest.class))).thenReturn(response);

        // when
        var result = this.dynamoDbUserRepository.findByEmail("john.doe@gmail.com");

        // then
        assertFalse(result.isPresent());
        verify(this.dynamoDbClient, times(1)).query(any(QueryRequest.class));
    }

    @Test
    void itShouldReturnAnUserIfUserExistByID() {
        // given
        var userID = UUID.randomUUID();
        var map = Map.of(
                "id", AttributeValue.builder().s(UUID.randomUUID().toString()).build(),
                "name", AttributeValue.builder().s("Petrus de Melo").build(),
                "email", AttributeValue.builder().s("petrusdemelo@gmail.com").build(),
                "password", AttributeValue.builder().s("123456").build(),
                "created_at", AttributeValue.builder().s(String.valueOf(LocalDateTime.now())).build(),
                "updated_at", AttributeValue.builder().s(String.valueOf(LocalDateTime.now())).build()
        );

        var response = GetItemResponse.builder().item(map).build();
        when(this.dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(response);

        // when
        var optionalUser = this.dynamoDbUserRepository.findById(userID);

        // then
        assertTrue(optionalUser.isPresent());
        var user = optionalUser.get();
        assertEquals("Petrus de Melo", user.getName());
        assertEquals("petrusdemelo@gmail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());

        verify(this.dynamoDbClient, times(1))
                .getItem(any(GetItemRequest.class));
    }

    @Test
    void itShouldReturnEmptyIfUserDoesntExistByID() {
        // given
        var userID = UUID.randomUUID();

        var response = GetItemResponse.builder().item(null).build();

        when(this.dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(response);

        // when
        var result = this.dynamoDbUserRepository.findById(userID);

        // then
        assertFalse(result.isPresent());
        verify(this.dynamoDbClient, times(1))
                .getItem(any(GetItemRequest.class));
    }

}
