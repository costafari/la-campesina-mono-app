package com.mkp.lacampesina.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mkp.lacampesina.web.rest.TestUtil;

public class LotesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lotes.class);
        Lotes lotes1 = new Lotes();
        lotes1.setId(1L);
        Lotes lotes2 = new Lotes();
        lotes2.setId(lotes1.getId());
        assertThat(lotes1).isEqualTo(lotes2);
        lotes2.setId(2L);
        assertThat(lotes1).isNotEqualTo(lotes2);
        lotes1.setId(null);
        assertThat(lotes1).isNotEqualTo(lotes2);
    }
}
