package com.algorian.springcloud.msvc.usuarios.implement;

import com.algorian.springcloud.msvc.usuarios.models.entity.Usuario;
import com.algorian.springcloud.msvc.usuarios.repositories.IUsuarioRepository;
import com.algorian.springcloud.msvc.usuarios.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    private final IUsuarioRepository _usuarioRepository;

    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository) {
        _usuarioRepository = usuarioRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) _usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Usuario> porId(Long id) {
        return _usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return _usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        _usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return _usuarioRepository.porEmail(email);
    }

    @Override
    public boolean existeEmail(String email) {
        return _usuarioRepository.existsByEmail(email);
    }
}
