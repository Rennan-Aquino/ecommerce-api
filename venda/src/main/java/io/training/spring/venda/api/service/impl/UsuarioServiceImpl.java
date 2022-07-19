package io.training.spring.venda.api.service.impl;

import io.training.spring.venda.domain.entity.Usuario;
import io.training.spring.venda.domain.repository.UsuarioRepository;
import io.training.spring.venda.exception.SenhaInvalidaException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public Usuario salvar(Usuario usuario){
        return repository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario) {
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasConferem = encoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasConferem) {
            return user;
        }

        throw new SenhaInvalidaException();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Usuario usuario = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

              String [] roles = usuario.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[] {"USER"};
              return User
                      .builder()
                      .username(usuario.getLogin())
                      .password(usuario.getSenha())
                      .roles(roles)
                      .build();
    }

}
