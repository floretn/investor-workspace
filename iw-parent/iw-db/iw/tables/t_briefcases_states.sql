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