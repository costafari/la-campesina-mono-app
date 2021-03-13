package com.mkp.lacampesina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mkp.lacampesina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class FacturasMasterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturasMaster.class);
        FacturasMaster facturasMaster1 = new FacturasMaster();
        facturasMaster1.setId(1L);
        FacturasMaster facturasMaster2 = new FacturasMaster();
        facturasMaster2.setId(facturasMaster1.getId());
        assertThat(facturasMaster1).isEqualTo(facturasMaster2);
        facturasMaster2.setId(2L);
        assertThat(facturasMaster1).isNotEqualTo(facturasMaster2);
        facturasMaster1.setId(null);
        assertThat(facturasMaster1).isNotEqualTo(facturasMaster2);
    }
}
