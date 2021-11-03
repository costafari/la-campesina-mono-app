package sv.com.mono.cam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sv.com.mono.cam.web.rest.TestUtil;

class PreciosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Precios.class);
        Precios precios1 = new Precios();
        precios1.setId(1L);
        Precios precios2 = new Precios();
        precios2.setId(precios1.getId());
        assertThat(precios1).isEqualTo(precios2);
        precios2.setId(2L);
        assertThat(precios1).isNotEqualTo(precios2);
        precios1.setId(null);
        assertThat(precios1).isNotEqualTo(precios2);
    }
}
