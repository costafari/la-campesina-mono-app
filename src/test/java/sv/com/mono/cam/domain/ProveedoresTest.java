package sv.com.mono.cam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sv.com.mono.cam.web.rest.TestUtil;

class ProveedoresTest {

    @Test
    void equalsVerifier() throws Exception {
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
