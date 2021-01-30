package com.mkp.lacampesina.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mkp.lacampesina.web.rest.TestUtil;

public class ProveedoresTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proveedores.class);
        Proveedores proveedores1 = new Proveedores();
        proveedores1.setId(1L);
        Proveedores proveedores2 = new Proveedores();
        proveedores2.setId(proveedores1.getId());
        assertThat(proveedores1).isEqualTo(proveedores2);
        proveedores2.setId(2L);
        assertThat(proveedores1).isNotEqualTo(proveedores2);
        proveedores1.setId(null);
        assertThat(proveedores1).isNotEqualTo(proveedores2);
    }
}
