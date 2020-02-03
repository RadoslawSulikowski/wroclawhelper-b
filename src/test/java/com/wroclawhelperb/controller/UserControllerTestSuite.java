package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoIdNoPassword;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void shouldFetchEmptyArray() throws Exception {
        //Given
        when(service.getAllUsers()).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldFetchUserList() throws Exception {
        //Given
        UserDtoNoIdNoPassword user1 = new UserDtoNoIdNoPassword(
                "fName1",
                "lName1",
                "uName1",
                "mail1",
                new GPSLocation(1.0, 1.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        UserDtoNoIdNoPassword user2 = new UserDtoNoIdNoPassword(
                "fName2",
                "lName2",
                "uName2",
                "mail2",
                new GPSLocation(2.0, 2.0, GPSLocation.USER_FAVORITE_LOCATION),
                false);
        List<UserDtoNoIdNoPassword> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(service.getAllUsers()).thenReturn(users);

        //When & Then
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("fName1")))
                .andExpect(jsonPath("$[1].lastName", is("lName2")))
                .andExpect(jsonPath("$[1].userName", is("uName2")))
                .andExpect(jsonPath("$[0].email", is("mail1")))
                .andExpect(jsonPath("$[0].location.latitude", is(1.0)))
                .andExpect(jsonPath("$[1].location.longitude", is(2.0)))
                .andExpect(jsonPath("$[1].location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)))
                .andExpect(jsonPath("$[0].schedulerOn", is(true)));
    }

    @Test
    void shouldHandleUserNotFoundExceptionGetUserById() throws Exception {
        //Given
        when(service.getUserById(anyLong())).thenThrow(UserNotFoundException.class);

        //When & Then
        mockMvc.perform(get("/users/id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldFetchUserById() throws Exception {
        //Given
        UserDtoNoIdNoPassword user =
                new UserDtoNoIdNoPassword(
                        "fName",
                        "lName",
                        "uName",
                        "mail",
                        new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION),
                        false);
        when(service.getUserById(anyLong())).thenReturn(user);

        //When & Then
        mockMvc.perform(get("/users/id/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)))
                .andExpect(jsonPath("$.schedulerOn", is(false)));
    }

    @Test
    void shouldHandleUserNotFoundExceptionGetUserByUsername() throws Exception {
        //Given
        when(service.getUserByUsername(anyString())).thenThrow(UserNotFoundException.class);

        //When & Then
        mockMvc.perform(get("/users/username/username").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldFetchUserByUsername() throws Exception {
        //Given
        UserDtoNoIdNoPassword user =
                new UserDtoNoIdNoPassword(
                        "fName",
                        "lName",
                        "uName",
                        "mail",
                        new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION),
                        false);
        when(service.getUserByUsername(anyString())).thenReturn(user);

        //When & Then
        mockMvc.perform(get("/users/username/username").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)))
                .andExpect(jsonPath("$.schedulerOn", is(false)));
    }

    @Test
    void shouldAddUser() throws Exception {
        //Given
        UserDtoNoId userDto = new UserDtoNoId(
                "fName",
                "lName",
                "uName",
                "pass",
                "mail",
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        when(service.addUser(userDto)).thenReturn(1L);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(201))
                .andExpect(status().reason("User successful added"));
    }

    @Test
    void shouldHandleUserNotFoundExceptionUpdateUser() throws Exception {
        //Given
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword();
        when(service.updateUser(any(UserDtoNoIdNoPassword.class))).thenThrow(UserNotFoundException.class);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //Given
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword(
                "fName",
                "lName",
                "uName",
                "mail",
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        when(service.updateUser(any(UserDtoNoIdNoPassword.class))).thenReturn(userDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)))
                .andExpect(jsonPath("$.schedulerOn", is(true)));
    }


    @Test
    void shouldHandleUserNotFoundExceptionDeleteUser() throws Exception {
        //Given
        doThrow(UserNotFoundException.class).when(service).deleteUser(anyLong());

        //When & Then
        mockMvc.perform(delete("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //Given
        doNothing().when(service).deleteUser(anyLong());

        //When & Then
        mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204))
                .andExpect(status().reason("User successful deleted"));
    }

    @Test
    void shouldReturnVerifyFalse() throws Exception {
        //Given
        when(service.verifyUser(any(UserDtoUsernamePassword.class))).thenReturn(false);
        UserDtoUsernamePassword user = new UserDtoUsernamePassword("username", "password");
        Gson gson = new Gson();
        String jsonContent = gson.toJson(user);
        //When & Then
        mockMvc.perform(get("/users/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("false"));
    }

    @Test
    void shouldVerifyReturnTrue() throws Exception {
        //Given
        when(service.verifyUser(any(UserDtoUsernamePassword.class))).thenReturn(true);
        UserDtoUsernamePassword user = new UserDtoUsernamePassword("username", "password");
        Gson gson = new Gson();
        String jsonContent = gson.toJson(user);

        //When & Then
        mockMvc.perform(get("/users/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("true"));
    }

    @Test
    void shouldUpdateUserPropertyThrowNoUsernameInMapException() throws Exception {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("someKey", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateUserProperty(anyMap())).thenThrow(NoUsernameInMapException.class);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(422))
                .andExpect(status().reason("Username not found in received data"));
    }

    @Test
    void shouldUpdateUserPropertyHandleUserNotFoundException() throws Exception {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("username", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateUserProperty(anyMap())).thenThrow(UserNotFoundException.class);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldUpdateUserPropertyReturnUpdatedUser() throws Exception {
        //Given
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword(
                "fName",
                "lName",
                "uName",
                "mail",
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateUserProperty(anyMap())).thenReturn(userDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)))
                .andExpect(jsonPath("$.schedulerOn", is(true)));
    }
}
