/*
 * Arquivo usado para execução de scripts SQL ao subir a aplicação.
 *  
 */


INSERT INTO user(id_user,username,password,enable) VALUES (2,'joao','joao',true);
INSERT INTO user(id_user,username,password,enable) VALUES (3,'victor','victor',true);

INSERT INTO role(id_role,name) VALUES (1,'USER');

INSERT INTO user_role(id_user,id_role) VALUES (2,1);
INSERT INTO user_role(id_user,id_role) VALUES (3,1);

INSERT INTO permission(id_permission,name,ativo) VALUES (1,'permission_read',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (2,'permission_create',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (3,'permission_update',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (4,'permission_delete',false);