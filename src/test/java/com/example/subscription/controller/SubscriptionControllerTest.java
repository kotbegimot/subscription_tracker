package com.example.subscription.controller;

import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.service.SubscriptionsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubscriptionControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SubscriptionsService service;
    private List<SubscriptionModel> subscriptions;

    @BeforeEach
    void setUp() {
        subscriptions = new ArrayList<>();
        subscriptions.add(new SubscriptionModel("king_james@nba.com", "2018-08-11"));
        subscriptions.add(new SubscriptionModel("durantula_35@nba.com", "2019-02-01"));
        subscriptions.add(new SubscriptionModel("nikola_jokic@nba.com", "2020-09-22"));
    }

    @AfterEach
    void tearDown() {
        subscriptions.clear();
    }

    @Test
    @DisplayName("Should fetch all subscriptions")
    @WithMockUser
    void getAllSubscriptionsTest() throws Exception {
        DateSortCriteria dateSortCriteria = DateSortCriteria.builder()
                .afterDate(null)
                .beforeDate(null)
                .build();
        when(service.getSubscriptions(dateSortCriteria)).thenReturn(subscriptions);
        mvc.perform(get("/api/v1/subscriptions")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()", is(subscriptions.size())))
                .andExpect(jsonPath("$[0].email", is("king_james@nba.com")))
                .andExpect(jsonPath("$[1].email", is("durantula_35@nba.com")))
                .andExpect(jsonPath("$[2].email", is("nikola_jokic@nba.com")));
    }

    @Test
    @DisplayName("Should fetch the requested subscription by email")
    @WithMockUser
    void getSubscriptionByEmailTest() throws Exception {
        String email = "durantula_35@nba.com";
        when(service.getSubscription(email)).thenReturn(subscriptions.get(1));
        mvc.perform(get("/api/v1/subscriptions/" + email)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.email", is("durantula_35@nba.com")))
                .andExpect(jsonPath("$.date", is("2019-02-01")));
    }

    @Test
    @DisplayName("Should call subscription deleting")
    @WithMockUser
    void deleteSubscriptionTest() throws Exception {
        String email = "durantula_35@nba.com";
        doNothing().when(service).deleteSubscription(email);
        mvc.perform(delete("/api/v1/subscriptions/" + email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(service, times(1)).deleteSubscription(email);
    }
}