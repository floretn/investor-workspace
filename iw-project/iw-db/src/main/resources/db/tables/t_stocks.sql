CREATE TABLE investor_workspace.t_stocks
( 
	stck_pk			serial,
	stck_cmpn_fk		integer,
	stck_cur_fk		integer not null,
	stck_name		varchar(255),
	stck_ticker		varchar(255) not null,
	PRIMARY KEY (stck_pk),
	FOREIGN KEY (stck_cmpn_fk) REFERENCES investor_workspace.t_companies (cmpn_pk),
	FOREIGN KEY (stck_cur_fk) REFERENCES investor_workspace.t_currency (cur_pk)
);