package tech.outspace.papershare.config.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import tech.outspace.papershare.filter.JwtVerifyFilter;
import tech.outspace.papershare.filter.OptionsRequestFilter;

public class OptionsConfig<T extends OptionsConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    private final OptionsRequestFilter optionsFilter;

    public OptionsConfig() {
        this.optionsFilter = new OptionsRequestFilter();
    }

    @Override
    public void configure(B builder) throws Exception {
        OptionsRequestFilter filter = postProcess(optionsFilter);
        builder.addFilterBefore(filter, JwtVerifyFilter.class);
    }
}
