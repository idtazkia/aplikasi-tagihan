package id.ac.tazkia.payment.virtualaccount.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    private static final String SQL_LOGIN
            = "select u.username as username,p.password as password, active "
            + "from s_user u "
            + "inner join s_user_password p on p.id_user = u.id "
            + "where username = ?";

    private static final String SQL_ROLE
            = "select u.username, p.permission_value as authority "
            + "from s_user u "
            + "inner join s_role r on u.id_role = r.id "
            + "inner join s_role_permission rp on rp.id_role = r.id "
            + "inner join s_permission p on rp.id_permission = p.id "
            + "where u.username = ?";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(SQL_LOGIN)
                .authoritiesByUsernameQuery(SQL_ROLE)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().permitAll()
                .and().formLogin().defaultSuccessUrl("/home", true)
                .loginPage("/login")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/api/callback/bni/payment")
                .antMatchers("/info")
                .antMatchers("/js/*")
                .antMatchers("/img/*")
                .antMatchers("/css/*");
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/client/**")
                    .authorizeRequests()
                    .anyRequest().authenticated().and().httpBasic()
                    .and().csrf().disable();
        }
    }

}

