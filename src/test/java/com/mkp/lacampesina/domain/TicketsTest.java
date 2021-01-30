package com.mkp.lacampesina.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mkp.lacampesina.web.rest.TestUtil;

public class TicketsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tickets.class);
        Tickets tickets1 = new Tickets();
        tickets1.setId(1L);
        Tickets tickets2 = new Tickets();
        tickets2.setId(tickets1.getId());
        assertThat(tickets1).isEqualTo(tickets2);
        tickets2.setId(2L);
        assertThat(tickets1).isNotEqualTo(tickets2);
        tickets1.setId(null);
        assertThat(tickets1).isNotEqualTo(tickets2);
    }
}
