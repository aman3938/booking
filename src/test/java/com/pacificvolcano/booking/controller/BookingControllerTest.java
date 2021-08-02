package com.pacificvolcano.booking.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacificvolcano.booking.dto.BookingDto;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
        
    @Autowired
    private ObjectMapper objectMapper;
    
    private String email = "spongebob@yahoo.com";
    private String fName = "SpongeBob";
    private String lName = "SquarePants";
    private Long id = null;
    
    @Test
    public void testCreateBooking() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                .content(objectMapper.writeValueAsString(new BookingDto(email, lName, fName, LocalDate.now().plusDays(3), LocalDate.now().plusDays(4), null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(Long.valueOf(result.getResponse().getContentAsString()));
        
        //test you can't create a booking longer than 3 days.
        result = mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                .content(objectMapper.writeValueAsString(new BookingDto(email, lName, fName, LocalDate.now().plusDays(5), LocalDate.now().plusDays(9), null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals("You can only book for max of three days", result.getResponse().getContentAsString());
    }
    
    @Test
    public void testUpdateBooking() throws Exception{
        //Create a booking and get it
        setupOneBooking(LocalDate.now(), LocalDate.now().plusDays(1));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        BookingDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), BookingDto.class);
                
        //change email to patrick@gmail.com from spongebob@yahoo.com
        dto.setEmail("patrick@gmail.com");
        
        mockMvc.perform(MockMvcRequestBuilders.put("/booking/{id}", id)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("patrick@gmail.com"));
    }
    
    /**
     * Patrick is not happy that his email has been put in the booking so asks SpongeBob and Squidward to 
     * add their email. Both SpongeBob and Squidward decide to add their email 
     * @throws Exception
     */
    @Test
    public void testNoStaleObjectUpdate() throws Exception{
        setupOneBooking(LocalDate.now().plusDays(20), LocalDate.now().plusDays(21));
        //SpongeBob fetches the booking
        MvcResult spongeBobResult = mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        BookingDto spongeDto = objectMapper.readValue(spongeBobResult.getResponse().getContentAsString(), BookingDto.class);
        
        //Squidward fetches the booking and updates the email
        MvcResult squidwardResult = mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        BookingDto squidwardDto = objectMapper.readValue(squidwardResult.getResponse().getContentAsString(), BookingDto.class);
        squidwardDto.setEmail("squidward@yahoo.com");
        
        mockMvc.perform(MockMvcRequestBuilders.put("/booking/{id}", id)
                .content(objectMapper.writeValueAsString(squidwardDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("squidward@yahoo.com"));
        
        //Now SpongeBob tries to update and it should fail
        spongeDto.setEmail("spongebob@yahoo.com");
        mockMvc.perform(MockMvcRequestBuilders.put("/booking/{id}", id)
                .content(objectMapper.writeValueAsString(spongeDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        
        // if we fetch the booking it should still have squidward's info
        mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("squidward@yahoo.com"));
    }
    
    @Test
    public void testGetAndDeleteBooking() throws Exception{
        //Create a booking
        setupOneBooking(LocalDate.now().plusDays(10), LocalDate.now().plusDays(11));
        //Test get
        mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("SpongeBob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("SquarePants"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("spongebob@yahoo.com"));
        
        //Now delete this booking
        mockMvc.perform(MockMvcRequestBuilders.delete("/booking/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        
        //Now get it again and it should return not found
        mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    private void setupOneBooking(LocalDate fromDate, LocalDate toDate) throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                .content(objectMapper.writeValueAsString(new BookingDto(email, lName, fName, fromDate, toDate, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        id = Long.valueOf(result.getResponse().getContentAsString());
    }
}
