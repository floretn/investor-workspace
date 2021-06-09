CREATE TABLE investor_workspace.t_roles_of_users
( 
	rou_pk		serial,
	rou_usr_fk	integer,
	rou_rol_fk	integer,
	rou_statuw	boolean,
	PRIMARY KEY (rou_pk),
	FOREIGN KEY (rou_usr_fk) REFERENCES investor_workspace.t_users (usr_pk),
	FOREIGN KEY (rou_rol_fk) REFERENCES investor_workspace.t_roles (rol_pk)
);