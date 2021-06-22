CREATE TABLE investor_workspace.t_stocks_in_briefcases
( 
	sib_pk		serial,
	sib_bs_fk	integer,
	sib_stck_fk	integer,
	sib_stck_num	bigint,
	PRIMARY KEY (sib_pk),
	FOREIGN KEY (sib_bs_fk) REFERENCES investor_workspace.t_briefcases_states (bs_pk),
	FOREIGN KEY (sib_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk)
);