package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.UserDtoFull;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoPassword;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        UserDtoNoPassword user1 = new UserDtoNoPassword(
                1L,
                "fName1",
                "lName1",
                "uName1",
                "mail1",
                new GPSLocation(1.0, 1.0, GPSLocation.USER_FAVORITE_LOCATION));
        UserDtoNoPassword user2 = new UserDtoNoPassword(
                2L,
                "fName2",
                "lName2",
                "uName2",
                "mail2",
                new GPSLocation(2.0, 2.0, GPSLocation.USER_FAVORITE_LOCATION));
        List<UserDtoNoPassword> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(service.getAllUsers()).thenReturn(users);

        //When & Then
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("fName1")))
                .andExpect(jsonPath("$[1].lastName", is("lName2")))
                .andExpect(jsonPath("$[1].userName", is("uName2")))
                .andExpect(jsonPath("$[0].email", is("mail1")))
                .andExpect(jsonPath("$[0].location.latitude", is(1.0)))
                .andExpect(jsonPath("$[1].location.longitude", is(2.0)))
                .andExpect(jsonPath("$[1].location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)));
    }

    @Test
    void shouldHandleUserNotFoundExceptionGetUser() throws Exception {
        //Given
        when(service.getUser(anyLong())).thenThrow(UserNotFoundException.class);

        //When & Then
        mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldFetchUser() throws Exception {
        //Given
        UserDtoNoPassword user =
                new UserDtoNoPassword(
                        1L,
                        "fName",
                        "lName",
                        "uName",
                        "mail",
                        new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION));
        when(service.getUser(anyLong())).thenReturn(user);

        //When & Then
        mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)));
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
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION));
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
        UserDtoFull userDto = new UserDtoFull();
        when(service.updateUser(any(UserDtoFull.class))).thenThrow(UserNotFoundException.class);
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
        UserDtoFull userDto = new UserDtoFull(
                1L,
                "fName",
                "lName",
                "uName",
                "pass",
                "mail",
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION));
        UserDtoNoPassword userDtoNoPassword = new UserDtoNoPassword(
                1L,
                "fName",
                "lName",
                "uName",
                "mail",
                new GPSLocation(2.0, 3.0, GPSLocation.USER_FAVORITE_LOCATION));
        when(service.updateUser(any(UserDtoFull.class))).thenReturn(userDtoNoPassword);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When & Then
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("fName")))
                .andExpect(jsonPath("$.lastName", is("lName")))
                .andExpect(jsonPath("$.userName", is("uName")))
                .andExpect(jsonPath("$.email", is("mail")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.USER_FAVORITE_LOCATION)));
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

}
