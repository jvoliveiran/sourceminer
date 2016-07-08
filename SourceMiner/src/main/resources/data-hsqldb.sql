/*
 * Arquivo usado para execução de scripts SQL ao subir a aplicação.
 *  
 */


INSERT INTO user_miner(id_user,username,password,enable) VALUES (2,'joao','joao',true);
INSERT INTO user_miner(id_user,username,password,enable) VALUES (3,'victor','victor',true);

INSERT INTO metric(id_metric,name,description,bean_name,metric_type) VALUES(1,'LOC','Linhas de Código','LOCMetric','COMPLEXITY');

/*
INSERT INTO role(id_role,name) VALUES (1,'USER');

INSERT INTO user_role(id_user,id_role) VALUES (2,1);
INSERT INTO user_role(id_user,id_role) VALUES (3,1);

INSERT INTO permission(id_permission,name,ativo) VALUES (1,'permission_read',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (2,'permission_create',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (3,'permission_update',true);
INSERT INTO permission(id_permission,name,ativo) VALUES (4,'permission_delete',false);

/*
INSERT INTO repository_connector(id_repository_connector,name,description, username, password, id_user, url, location, version_manager)
VALUES(1,'SourceMiner SVN', 'Repositório SVN para teste do SourceMiner',null,null,2,'file:///Users/MacBook/Documents/SVNRepo',
'LOCAL','SVN');

INSERT INTO project(id_project,name,path,id_repository_connector)
VALUES(1,'JGITComponent','/jgitcomponent',1);
*/

-- 	https://svn.riouxsvn.com/sourceminercomp
-- /smcomponents