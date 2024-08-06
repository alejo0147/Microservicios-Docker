package com.algorian.springcloud.msvc.usuarios.implement;

import com.algorian.springcloud.msvc.usuarios.client.ICursoClientRest;
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

    //  DEPENDENCIAS A INYECTAR
    private final IUsuarioRepository _usuarioRepository;
    @Autowired
    private  ICursoClientRest _clientRest;


    //  CONSTRUCTOR PARA INYECTAR DEPENDENCIA
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository) {
        this._usuarioRepository = usuarioRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) _usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
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
        _clientRest.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) _usuarioRepository.findAllById(ids);
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
