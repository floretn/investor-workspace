CREATE TABLE investor_workspace.t_briefcases
( 
	brf_pk		serial,
	brf_usr_fk	integer,
	usr_name	character varying (255),
	PRIMARY KEY (brf_pk),
	FOREIGN KEY (brf_usr_fk) REFERENCES investor_workspace.t_users (usr_pk)
);