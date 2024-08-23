package com.algorian.springcloud.msvc.cursos.implement;

import com.algorian.springcloud.msvc.cursos.clients.IUsuarioClientRest;
import com.algorian.springcloud.msvc.cursos.models.Usuario;
import com.algorian.springcloud.msvc.cursos.entity.Curso;
import com.algorian.springcloud.msvc.cursos.entity.CursoUsuario;
import com.algorian.springcloud.msvc.cursos.repositories.ICursoRepository;
import com.algorian.springcloud.msvc.cursos.services.ICursoService;
import feign.FeignException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class CursoServiceImpl implements ICursoService {

    private final ICursoRepository _cursoRepository;

    private final IUsuarioClientRest _clientRest;

    /**
     * Listar todos los cursos
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<Curso>> listar() {
        List<Curso> cursos = new ArrayList<>();
        //  Agregar los datos iterados a una lista
        _cursoRepository.findAll().forEach(cursos::add);
        if (cursos.isEmpty()) {
            // Retornar una respuesta adecuada si no hay cursos
            return ResponseEntity.noContent().build();
        }
        // Retornar la lista de cursos con un estado HTTP 200 (OK)
        return ResponseEntity.ok(cursos);
    }

    /**
     * Listar datos de un curso por su ID
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return _cursoRepository.findById(id);
    }

    /**
     * Listar los usurios que pertenecen a un curso
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> porIdConUsuarios(Long id) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(id);
        if (cursoOptional.isEmpty()) return ResponseEntity.notFound().build();

        Curso curso = cursoOptional.get();
        if (!curso.getCursoUsuarios().isEmpty()) {
            //  Declaro una variable de tipo lista para que tome los usuarioId que tiene CursoUsuario
            List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
            try {
                //  Variable de tipo lista de Usuarios tendrá los valores que nos retorne el servico
                List<Usuario> usuariosCurso = _clientRest.obtenerUsuPorId(ids);
                //  La variable que contiene la lista de Usuarios nos mostrará los que pertenecen a este curso
                curso.setUsuarios(usuariosCurso);
            } catch (FeignException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(Collections.singletonMap("mensaje",
                                "No se pudo obtener la información de los usuarios: " + e.getMessage()));
            }
        }
        return ResponseEntity.ok(curso);
    }

    /**
     * Guardar un nuevo curso
     *
     * @param curso
     * @param result
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> guardar(Curso curso, BindingResult result) {
        if (result.hasErrors()) return validarCampos(result);
        if (_cursoRepository.existsByNombreIgnoreCase(curso.getNombre()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre del curso ya existe.");

        Curso cursoDb = _cursoRepository.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    /**
     * Editar un nuevo curso
     *
     * @param curso
     * @param result
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> editar(Curso curso, Long id, BindingResult result) {
        if (result.hasErrors()) return validarCampos(result);
        String nuevoNombre = curso.getNombre();

        Optional<Curso> cursoOptional = _cursoRepository.findById(id);
        if (cursoOptional.isPresent()) {
            Curso cursoDb = cursoOptional.get();

            if (!cursoDb.getNombre().equalsIgnoreCase(nuevoNombre)) {
                if (_cursoRepository.existsByNombreIgnoreCase(nuevoNombre)) return
                        ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre del curso ya existe.");
            }
            cursoDb.setNombre(nuevoNombre);
            return ResponseEntity.status(HttpStatus.CREATED).body(_cursoRepository.save(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Eliminar un curso basado en su ID
     *
     * @param id
     */
    @Override
    @Transactional
    public ResponseEntity<Void> eliminar(Long id) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(id);
        if (cursoOptional.isPresent()) {
            _cursoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Eliminar CursoUsuario por el usuario ID
     *
     * @param usuarioId
     */
    @Override
    @Transactional
    public ResponseEntity<Void> eliminarCurUsuPorId(Long usuarioId) {
        int rowsAffected = _cursoRepository.eliminarCursoUsuarioPorUsuarioId(usuarioId);
        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generar un archivo Excel de todos los cursos
     *
     * @param response
     */
    @Override
    public void generateExcel(HttpServletResponse response) {

        List<Curso> cursos = (List<Curso>) _cursoRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Info cursos");
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("NOMBRE");

        int dataRowIndex = 1;

        for (Curso curso : cursos) {
            XSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(curso.getId());
            dataRow.createCell(1).setCellValue(curso.getNombre());
            dataRowIndex++;
        }

        try (ServletOutputStream ops = response.getOutputStream()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=cursos.xlsx");

            workbook.write(ops);
        } catch (IOException e) {
            log.error("Error al generar el archivo Excel", e);

            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el archivo Excel");
            } catch (IOException ex) {
                log.error("Error al enviar la respuesta de error", ex);
            }
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error("Error al cerrar el workbook", e);
            }
        }
    }



    //  MÉTODOS PARA CONSUMIR SERVICIOS DE USUARIOS POR MEDIO DEL FEING CLIENTS

    /**
     * Asignar un usuario a un curso
     *
     * @param usuario
     * @param cursoId
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> asignarUsuario(Usuario usuario, Long cursoId) {
        log.info("VALIDAR SI EL CURSO EXISTE POR SU ID");
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isEmpty()) return ResponseEntity.notFound().build();

        try {
            log.info("USAR EL MÉTODO DE FEINGCLIENTS PARA BUSCAR EL USUARIO");
            Usuario usuarioMsvc = _clientRest.detalle(usuario.getId());

            log.info("ASIGNAR LOS VALORES DE LA INSTANCIA ENCONTRADA DE CURSO");
            Curso Curso = cursoOptional.get();
            log.info("SE CREARÁ UN CURSOUSUARIO NUEVO CON EL CURSO ID Y EL USUARIO ID");
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            Curso.addCursoUsuario(cursoUsuario);

            _cursoRepository.save(Curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMsvc);

        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje",
                            "No existe el usuario por el id: " + usuario.getId() + " o error en la comunicación: " + e.getMessage()));
        }
    }

    /**
     * Crear un usuario en la BBDD de usuario y luego asignar a un curso
     *
     * @param usuario
     * @param cursoId
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isEmpty()) return ResponseEntity.notFound().build();

        try {
            Usuario usuarioNuevoMsvc = _clientRest.crear(usuario);

            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);

            _cursoRepository.save(curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevoMsvc);

        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje",
                            "Error al crear el usuario: " + e.getMessage()));
        }
    }

    /**
     * Desasignar un usuario a un curso
     *
     * @param usuario
     * @param cursoId
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<?> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = _cursoRepository.findById(cursoId);
        if (cursoOptional.isEmpty()) return ResponseEntity.notFound().build();

        try {
            Usuario usuarioMsvc = _clientRest.detalle(usuario.getId());

            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            _cursoRepository.save(curso);

            return ResponseEntity.ok(usuarioMsvc);

        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje",
                            "Error al eliminar el usuario: " + e.getMessage()));
        }
    }


    //  MÉTODO ÚTIL
    private ResponseEntity<Map<String, String>> validarCampos(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}