package com.bridgelabz.loginregistration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todoapplication.ToDoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ToDoApplication.class)
public class UserTests {

	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

        @Test
        public void loginTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\": \"sameersaurabh9314@gmail.com\", \"password\" : \"9314\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Welcome sameersaurabh9314@gmail.com"))
                    .andExpect(jsonPath("$.status").value(1));
                   
        }
        
        @Test
        public void loginEmailEmptyStringTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\": \"\", \"password\" : \"9314\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Email and password cannot be null"))
                    .andExpect(jsonPath("$.status").value(-1));
                   
        }
        @Test
        public void loginPasswordEmptyStringTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\": \"sameersaurabh9314@gmail.com\", \"password\" : \"\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Email and password cannot be null"))
                    .andExpect(jsonPath("$.status").value(-1));
        }
        @Test
        public void loginEmailNullTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"password\" : \"9314\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Email cannot be Null"))
                    .andExpect(jsonPath("$.status").value(-1));
                   
        }
        @Test
        public void loginPasswordNullTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\": \"sameersaurabh9314@gmail.com\"\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Password Cannot be null"))
                    .andExpect(jsonPath("$.status").value(-1));
                   
        }
        @Test
        public void registerationTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\": \"sameersaurabh9314@gmail.com\", \"password\" : \"9314\",\"id\":\"1\",\"userName\":\"saurav\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Mail sent to the user with email \" + user.getEmail()"))
                    .andExpect(jsonPath("$.status").value(200));
        }
}

