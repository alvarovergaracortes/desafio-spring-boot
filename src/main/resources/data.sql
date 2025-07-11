-- Usuarios
-- Roles: ADMIN, USER

-- pepito; pwd=clavedepepito
-- admin ; pwd=admin123     
-- user  ; pwd=user123      
INSERT INTO users (username, password, roles) VALUES
('pepito', '$2a$10$V1Oa9S0S5SUjVvGjKUCyaurCDgHf4AHtukJWQCpsqQYvQ.gnUIsl.', 'Admin');

INSERT INTO users (username, password, roles) VALUES
('admin', '$2a$10$zFC/8N.FuD/Q3mXEwcOyIOQf6lopvdAoq.eyBHEII8RW8H66g1B3W', 'ADMIN');

INSERT INTO users (username, password, roles) VALUES
('user', '$2a$10$s2u5T1tbMsgnUZZKmpl5.e4ztz0HWncrVns3eK/e3QvA4yhKL0JE.', 'USER');


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