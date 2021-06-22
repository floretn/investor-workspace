begin;

CREATE TABLE investor_workspace.t_briefcases
( 
	brf_pk		serial,
	brf_usr_fk	integer,
	brf_name	character varying (255),
	brf_desc	character varying (255),
	PRIMARY KEY (brf_pk),
	FOREIGN KEY (brf_usr_fk) REFERENCES investor_workspace.t_users (usr_pk)
);

CREATE TABLE investor_workspace.t_files
( 
	fl_pk		serial,
	fl_name		character varying (255),
	fl_date_dwn	date,
	fl_content	bytea,
	PRIMARY KEY (fl_pk)
);

CREATE TABLE investor_workspace.t_briefcases_states
( 
	bs_pk		serial,
	bs_brf_fk	integer,
	bs_fl_fk	integer,
	bs_date		date,
	PRIMARY KEY (bs_pk),
	FOREIGN KEY (bs_brf_fk) REFERENCES investor_workspace.t_briefcases (brf_pk),
	FOREIGN KEY (bs_fl_fk) REFERENCES investor_workspace.t_files (fl_pk)
);

CREATE TABLE investor_workspace.t_stocks_in_briefcases
( 
	sib_pk			serial,
	sib_bs_fk		integer,
	sib_stck_fk		integer,
	sib_stck_num		bigint,
	PRIMARY KEY (sib_pk),
	FOREIGN KEY (sib_bs_fk) REFERENCES investor_workspace.t_briefcases_states (bs_pk),
	FOREIGN KEY (sib_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk)
);

CREATE TABLE investor_workspace.t_account
( 
	acc_pk		serial,
	acc_bs_fk	integer,
	acc_cur_fk	integer,
	acc_am		numeric,
	PRIMARY KEY (acc_pk),
	FOREIGN KEY (acc_bs_fk) REFERENCES investor_workspace.t_briefcases_states (bs_pk),
	FOREIGN KEY (acc_cur_fk) REFERENCES investor_workspace.t_currency (cur_pk)
);

end;