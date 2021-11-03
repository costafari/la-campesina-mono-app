package sv.com.mono.cam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sv.com.mono.cam.web.rest.TestUtil;

class AbonosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Abonos.class);
        Abonos abonos1 = new Abonos();
        abonos1.setId(1L);
        Abonos abonos2 = new Abonos();
        abonos2.setId(abonos1.getId());
        assertThat(abonos1).isEqualTo(abonos2);
        abonos2.setId(2L);
        assertThat(abonos1).isNotEqualTo(abonos2);
        abonos1.setId(null);
        assertThat(abonos1).isNotEqualTo(abonos2);
    }
}
