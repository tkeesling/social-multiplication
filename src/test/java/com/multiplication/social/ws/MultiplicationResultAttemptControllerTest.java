package com.multiplication.social.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplication.social.domain.Multiplication;
import com.multiplication.social.domain.MultiplicationResultAttempt;
import com.multiplication.social.domain.User;
import com.multiplication.social.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;


    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnsCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnsNotCorrect() throws Exception {
        genericParameterizedTest(false);
    }

    private void genericParameterizedTest(final boolean correct) throws Exception {
        // given
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);
        User user = new User("Tyler");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500, correct);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResultAttempt.write(attempt).getJson()))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResultAttempt.write(
                        new MultiplicationResultAttempt(
                                attempt.getUser(),
                                attempt.getMultiplication(),
                                attempt.getResultAttempt(),
                                correct))
                        .getJson());
    }

    @Test
    public void getUserStats() throws Exception {
        // given
        User user = new User("tyler");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500, true);
        List<MultiplicationResultAttempt> recentAttemps = Lists.newArrayList(attempt, attempt);
        given(multiplicationService.getStatsForUser("tyler")).willReturn(recentAttemps);

        // when
        MockHttpServletResponse response = mvc.perform(get("/results").param("alias", "tyler")).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(recentAttemps).getJson());
    }
}
