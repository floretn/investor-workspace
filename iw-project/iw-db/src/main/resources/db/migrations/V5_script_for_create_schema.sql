begin;

CREATE TABLE investor_workspace.t_users
( 
	usr_pk		serial,
	usr_lnm		character varying (255) not null,
	usr_fnm		character varying (255) not null,
	usr_ptrnm	character varying (255) not null,
	usr_phn		character varying (255) not null,
	usr_email	character varying (255) not null,
	usr_un		character varying (255) not null,
	PRIMARY KEY (usr_pk)
);

CREATE TABLE investor_workspace.t_roles
( 
	rol_pk		serial,
	rol_name	character varying (255) not null,
	PRIMARY KEY (rol_pk)
);

CREATE TABLE investor_workspace.t_pages
( 
	pg_pk		serial,
	pg_path		character varying (255) not null,
	PRIMARY KEY (pg_pk)
);

CREATE TABLE investor_workspace.t_auth_info
( 
	ai_pk		serial,
	ai_usr_fk	integer,
	ai_login	character varying (255) not null,
	ai_pwd		character varying (255) not null,
	PRIMARY KEY (ai_pk),
	FOREIGN KEY (ai_usr_fk) REFERENCES investor_workspace.t_users (usr_pk)
);

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

CREATE TABLE investor_workspace.t_pages_for_roles
( 
	pfr_pk		serial,
	pfr_pg_fk	integer,
	pfr_rol_fk	integer,
	PRIMARY KEY (pfr_pk),
	FOREIGN KEY (pfr_pg_fk) REFERENCES investor_workspace.t_pages (pg_pk),
	FOREIGN KEY (pfr_rol_fk) REFERENCES investor_workspace.t_roles (rol_pk)
);

end;