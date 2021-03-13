package com.mkp.lacampesina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mkp.lacampesina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class AbonoFacturasTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbonoFacturas.class);
        AbonoFacturas abonoFacturas1 = new AbonoFacturas();
        abonoFacturas1.setId(1L);
        AbonoFacturas abonoFacturas2 = new AbonoFacturas();
        abonoFacturas2.setId(abonoFacturas1.getId());
        assertThat(abonoFacturas1).isEqualTo(abonoFacturas2);
        abonoFacturas2.setId(2L);
        assertThat(abonoFacturas1).isNotEqualTo(abonoFacturas2);
        abonoFacturas1.setId(null);
        assertThat(abonoFacturas1).isNotEqualTo(abonoFacturas2);
    }
}
