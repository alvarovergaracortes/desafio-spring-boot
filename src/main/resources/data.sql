-- Usuarios
-- Roles: ADMIN, USER

-- pepito; pwd=clavedepepito
-- admin ; pwd=admin123     
-- user  ; pwd=user123      
INSERT INTO users (username, password, roles) VALUES
('pepito', '$2b$12$OkkQv/7GIiDgPjT299FuwOzQSKVU6sSTIo5eETXg3s7bZrviMAcsy', 'Admin');

INSERT INTO users (username, password, roles) VALUES
('admin', '$2a$10$N2g5wYOvFNsY8LRO6axtb.wsOZHmgUhkgVn40fPGZLbZ4yN907O0m', 'ADMIN');

INSERT INTO users (username, password, roles) VALUES
('user', '$2a$10$go1YU7TF8MVSpbjSycbgQebluJtEebfm22FeabbvZ.uUTAhxtCMSS', 'USER');


-- Estados de tarea
INSERT INTO task_states (name) VALUES ('Pendiente');
INSERT INTO task_states (name) VALUES ('En Proceso');
INSERT INTO task_states (name) VALUES ('Completada');

-- Tareas
INSERT INTO task (title, description, user_id, state_id) VALUES
('Configurar seguridad', 'Aplicar Spring Security y JWT', 1, 1);

INSERT INTO task (title, description, user_id, state_id) VALUES
('Crear endpoints de tareas', 'Desarrollar CRUD de tareas', 2, 2);

INSERT INTO task (title, description, user_id, state_id) VALUES
('Testear login', 'Probar acceso con tokens', 3, 3);