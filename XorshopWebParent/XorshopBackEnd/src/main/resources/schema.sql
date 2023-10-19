INSERT INTO `roles` VALUES (1,'gérer l\'ensemble du site','Admin'),
                           (2,'gérer les prix des produits, les clients, les expéditions, les commandes et les rapports de vente','Vendeur'),
                           (3,'gérer les catégories, les marques, les produits, les articles et les menus','Editeur'),
                           (4,'visualiser les produits, les commandes et mettre à jour l\'état des commandes','Livreur'),
                           (5,'gérer les questions et les revues','Assistant');					
INSERT INTO `users` VALUES (1,_binary '\0','mgnfco@live.com',_binary '','Hamza','BOULKAMH',NULL,'$2a$10$e1TQTaSZ.NxDBpw4Ip1JIOuxALXt.M65EzxLzZNCXlz6y90rxDydO',NULL,'mgnfco');
INSERT INTO `users_role` VALUES (1,1);
