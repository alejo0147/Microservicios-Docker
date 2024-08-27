# Kubernetes

*  Proyecto con Spring boot y Docker
*  2 microservicios cada uno en una base de datos diferente pero con conexión HttpClient
*  Uno de los microservicios está en MySQL y otro en PostgreSQL
*  Se ha dockerizado el servicio de usuarios y curso. Se ha cargado en Docker Hub las imágenes.
*  En la conexión de los dos microservicios se ha hecho que un curso me traiga los usuarios que están inscritos en este
*  Se puede crear un usuario e inscribirlo en un curso desde el servicio de cursos
*  En el servicio de usuario se puede hacer que al eliminar un usuario, este sea desasignado al curso que pertenecia
*  Consumiendo desde un explorardor el servicio de excel, se genera un archivo xlsx con los cursos existentes. 
