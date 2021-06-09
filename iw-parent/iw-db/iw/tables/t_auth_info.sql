CREATE TABLE investor_workspace.t_auth_info
( 
	ai_pk		serial,
	ai_usr_fk	integer,
	ai_login	character varying (255) not null,
	ai_pwd		character varying (255) not null,
	PRIMARY KEY (ai_pk),
	FOREIGN KEY (ai_usr_fk) REFERENCES investor_workspace.t_users (usr_pk)
);