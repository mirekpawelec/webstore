package pl.pawelec.webshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawelec.webshop.service.UserInfoService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserInfoService userInfoService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/orders*", "/admin/delivery*", "/admin/deliveries*", "/admin/repository").hasAnyRole("USER", "ADMIN", "DBA")
                    .antMatchers("/admin/parameters/*", "/admin/parameters*").hasRole("DBA")
                    .antMatchers("/admin/users/*/update").hasAnyRole("CLIENT", "USER", "ADMIN", "DBA")
                    .antMatchers(HttpMethod.POST, "/admin/users/update").hasAnyRole("CLIENT", "USER", "ADMIN", "DBA")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN", "DBA")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").failureUrl("/login?error").usernameParameter("username").passwordParameter("password").permitAll()
                    .and()
                    .logout().logoutSuccessUrl("/login?logout").permitAll()
                    .and()
                    .exceptionHandling().accessDeniedPage("/login?accessDenied")
                    .and()
                    .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userInfoService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**");
    }
}
