CREATE TABLE investor_workspace.t_files
( 
	fl_pk		serial,
	fl_name		character varying (255),
	fl_date_dwn	date,
	fl_content	bytea,
	PRIMARY KEY (fl_pk)
);