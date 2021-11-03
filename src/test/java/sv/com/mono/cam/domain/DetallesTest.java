package sv.com.mono.cam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sv.com.mono.cam.web.rest.TestUtil;

class DetallesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Detalles.class);
        Detalles detalles1 = new Detalles();
        detalles1.setId(1L);
        Detalles detalles2 = new Detalles();
        detalles2.setId(detalles1.getId());
        assertThat(detalles1).isEqualTo(detalles2);
        detalles2.setId(2L);
        assertThat(detalles1).isNotEqualTo(detalles2);
        detalles1.setId(null);
        assertThat(detalles1).isNotEqualTo(detalles2);
    }
}
