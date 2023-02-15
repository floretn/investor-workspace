CREATE TABLE investor_workspace.t_pages_for_roles
( 
	pfr_pk		serial,
	pfr_pg_fk	integer,
	pfr_rol_fk	integer,
	PRIMARY KEY (pfr_pk),
	FOREIGN KEY (pfr_pg_fk) REFERENCES investor_workspace.t_pages (pg_pk),
	FOREIGN KEY (pfr_rol_fk) REFERENCES investor_workspace.t_roles (rol_pk)
);