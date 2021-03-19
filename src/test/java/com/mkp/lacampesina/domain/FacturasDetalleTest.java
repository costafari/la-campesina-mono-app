package com.mkp.lacampesina.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mkp.lacampesina.web.rest.TestUtil;

public class FacturasDetalleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturasDetalle.class);
        FacturasDetalle facturasDetalle1 = new FacturasDetalle();
        facturasDetalle1.setId(1L);
        FacturasDetalle facturasDetalle2 = new FacturasDetalle();
        facturasDetalle2.setId(facturasDetalle1.getId());
        assertThat(facturasDetalle1).isEqualTo(facturasDetalle2);
        facturasDetalle2.setId(2L);
        assertThat(facturasDetalle1).isNotEqualTo(facturasDetalle2);
        facturasDetalle1.setId(null);
        assertThat(facturasDetalle1).isNotEqualTo(facturasDetalle2);
    }
}
