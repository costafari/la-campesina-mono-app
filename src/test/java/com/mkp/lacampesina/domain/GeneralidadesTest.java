package com.mkp.lacampesina.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mkp.lacampesina.web.rest.TestUtil;

public class GeneralidadesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Generalidades.class);
        Generalidades generalidades1 = new Generalidades();
        generalidades1.setId(1L);
        Generalidades generalidades2 = new Generalidades();
        generalidades2.setId(generalidades1.getId());
        assertThat(generalidades1).isEqualTo(generalidades2);
        generalidades2.setId(2L);
        assertThat(generalidades1).isNotEqualTo(generalidades2);
        generalidades1.setId(null);
        assertThat(generalidades1).isNotEqualTo(generalidades2);
    }
}
