package se.sundsvall.incident.integration.lifebuoy;

import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;

import java.util.concurrent.TimeUnit;

import static se.sundsvall.incident.integration.lifebuoy.LifeBuoyIntegration.INTEGRATION_NAME;

@Import(FeignConfiguration.class)
@EnableConfigurationProperties(LifeBuoyProperties.class)
class LifeBuoyClientConfiguration {
    final private LifeBuoyProperties properties;

    LifeBuoyClientConfiguration(final LifeBuoyProperties properties) {
        this.properties = properties;
    }

    @Bean
    FeignBuilderCustomizer customizer() {
        return FeignMultiCustomizer.create()
                .withErrorDecoder(errorDecoder())
                .withRequestOptions(requestOptions())
                .composeCustomizersToOne();
    }

    Request.Options requestOptions() {
        return new Request.Options(
                properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS,
                properties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS,
                true);

    }

    ErrorDecoder errorDecoder() {
        return new ProblemErrorDecoder(INTEGRATION_NAME);
    }
}
