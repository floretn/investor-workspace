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