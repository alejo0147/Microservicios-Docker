package com.algorian.springcloud.msvc.cursos.implement;

import com.algorian.springcloud.msvc.cursos.clients.IUsuarioClientRest;
import com.algorian.springcloud.msvc.cursos.models.Usuario;
import com.algorian.springcloud.msvc.cursos.models.entity.Curso;
import com.algorian.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.algorian.springcloud.msvc.cursos.repositories.ICursoRepository;
import com.algorian.springcloud.msvc.cursos.services.ICursoService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements ICursoService {

    @Autowired
    private ICursoRepository _cursoRepository;

    @Autowired
    private IUsuarioClientRest _clientRest;


    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) _cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return _cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso Curso) {
        return _cursoRepository.save(Curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        _cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCurUsuPorId(Long id) {
        _cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    public void generateExcel(HttpServletResponse response) {
        List<Curso> cursos = (List<Curso>) _cursoRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Info cursos");
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("NOMBRE");

        int dataRowIndex = 1;

        for (Curso curso : cursos){
            XSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(curso.getId());
            dataRow.createCell(1).setCellValue(curso.getNombre());
            dataRowIndex++;
        }
        try {
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioMsvc = _clientRest.detalle(usuario.getId());

            Curso Curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            Curso.addCursoUsuario(cursoUsuario);
            _cursoRepository.save(Curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioNuevoMsvc = _clientRest.crear(usuario);

            Curso Curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            Curso.addCursoUsuario(cursoUsuario);
            _cursoRepository.save(Curso);
            return Optional.of(usuarioNuevoMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Usuario usuarioMsvc = _clientRest.detalle(usuario.getId());

            Curso Curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            Curso.removeCursoUsuario(cursoUsuario);
            _cursoRepository.save(Curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(id);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            //  SI cursoUsuario NO ES VACÍO ES PORQUE HAY ALUMNOS
            if (!curso.getCursoUsuarios().isEmpty()) {
                //  CREAR LISTA DE IDs USANDO LAMBDA CON Map Y ToList
                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
                List<Usuario> usuariosCurso = _clientRest.obtenerUsuPorId(ids);
                curso.setUsuarios(usuariosCurso);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }
}
