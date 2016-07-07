INSERT INTO user_miner(id_user,username,password,enable) VALUES (2,'joao','joao',true);
INSERT INTO user_miner(id_user,username,password,enable) VALUES (3,'victor','victor',true);

INSERT INTO metric(id_metric,name,description,bean_name,metric_type) VALUES(1,'LOC','Linhas de código','LOCMetric','COMPLEXITY');
INSERT INTO metric(id_metric,name,description,bean_name,metric_type) VALUES(2,'WMC','Métodos Ponderados por classe','WMCMetric','COMPLEXITY');
