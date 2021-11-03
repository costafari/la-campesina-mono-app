package sv.com.mono.cam.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.mono.cam.service.MailService;

@Configuration
public class NoOpMailConfiguration {

    private final MailService mockMailService;

    public NoOpMailConfiguration() {
        mockMailService = mock(MailService.class);
        doNothing().when(mockMailService).sendActivationEmail(any());
    }

    @Bean
    public MailService mailService() {
        return mockMailService;
    }
}
