package com.algorian.springcloud.msvc.usuarios.implement;

import com.algorian.springcloud.msvc.usuarios.client.ICursoClientRest;
import com.algorian.springcloud.msvc.usuarios.models.entity.Usuario;
import com.algorian.springcloud.msvc.usuarios.repositories.IUsuarioRepository;
import com.algorian.springcloud.msvc.usuarios.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository _usuarioRepository;
    @Autowired
    private  ICursoClientRest _clientRest;

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
        // Primero, intenta eliminar la relación curso-usuario
        ResponseEntity<Void> response = _clientRest.eliminarCursoUsuarioPorId(id);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            // Si se eliminó correctamente la relación curso-usuario, procede a eliminar el usuario
            _usuarioRepository.deleteById(id);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si no se encontró la relación curso-usuario, decide si aún quieres eliminar el usuario
            // Aquí podrías decidir eliminarlo o lanzar una excepción, dependiendo de la lógica de tu negocio
            _usuarioRepository.deleteById(id); // O eliminar esta línea si no quieres eliminar el usuario
        } else {
            // Si hubo otro tipo de error, puedes manejarlo aquí, por ejemplo lanzando una excepción
            throw new RuntimeException("Error al eliminar la relación curso-usuario");
        }
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
