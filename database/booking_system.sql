/*
Author: Kawai
Date: 2001-01-24
Desc: Create tables and index for Booking system
*/

CREATE TABLE [dbo].[fmEquipment] (
	[eqm_fac_id] [int] NOT NULL ,
	[eqm_ftp_id] [int] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmFacility] (
	[fac_id] [int] IDENTITY (1, 1) NOT NULL ,
	[fac_ftp_id] [int] NOT NULL ,
	[fac_title] [nvarchar] (255) NOT NULL ,
	[fac_desc] [nvarchar] (1000) NULL ,
	[fac_remarks] [nvarchar] (1000) NULL ,
	[fac_url] [nvarchar] (255) NULL ,
	[fac_url_type] [varchar] (50) NULL ,
	[fac_add_xml] [ntext] NULL ,
	[fac_status] [varchar] (50) NOT NULL ,
	[fac_order] [int] NULL ,
	[fac_loc_id] [int] NULL ,
	[fac_owner_ent_id] [int] NOT NULL ,
	[fac_create_timestamp] [datetime] NOT NULL ,
	[fac_create_usr_id] [varchar] (20) NOT NULL ,
	[fac_upd_timestamp] [datetime] NOT NULL ,
	[fac_upd_usr_id] [varchar] (20) NOT NULL 
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmFacilitySchedule] (
	[fsh_rsv_id] [int] NULL ,
	[fsh_fac_id] [int] NOT NULL ,
	[fsh_date] [datetime] NOT NULL ,
	[fsh_start_time] [datetime] NOT NULL ,
	[fsh_end_time] [datetime] NOT NULL ,
	[fsh_status] [varchar] (50) NOT NULL ,
	[fsh_cancel_timestamp] [datetime] NULL ,
	[fsh_cancel_usr_id] [varchar] (20) NULL ,
	[fsh_cancel_type] [nvarchar] (255) NULL ,
	[fsh_cancel_reason] [nvarchar] (1000) NULL ,
	[fsh_owner_ent_id] [int] NOT NULL ,
	[fsh_create_timestamp] [datetime] NOT NULL ,
	[fsh_create_usr_id] [varchar] (20) NOT NULL ,
	[fsh_upd_timestamp] [datetime] NOT NULL ,
	[fsh_upd_usr_id] [varchar] (20) NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmFacilityType] (
	[ftp_id] [int] NOT NULL ,
	[ftp_title_xml] [nvarchar] (255) NOT NULL ,
	[ftp_main_indc] [int] NULL ,
	[ftp_class_name] [varchar] (50) NULL ,
	[ftp_xsl_prefix] [varchar] (50) NULL ,
	[ftp_parent_ftp_id] [int] NULL ,
	[ftp_owner_ent_id] [int] NOT NULL 
)
GO

CREATE TABLE [dbo].[fmLocation] (
	[loc_id] [int] IDENTITY (1, 1) NOT NULL ,
	[loc_title] [nvarchar] (255) NOT NULL ,
	[loc_desc] [nvarchar] (1000) NULL ,
	[loc_owner_ent_id] [int] NOT NULL ,
	[loc_create_timestamp] [datetime] NOT NULL ,
	[loc_create_usr_id] [varchar] (20) NOT NULL ,
	[loc_upd_timestamp] [datetime] NOT NULL ,
	[loc_upd_usr_id] [varchar] (20) NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmReservation] (
	[rsv_id] [int] IDENTITY (1, 1) NOT NULL ,
	[rsv_purpose] [nvarchar] (255) NOT NULL ,
	[rsv_desc] [nvarchar] (1000) NULL ,
	[rsv_ent_id] [int] NULL ,
	[rsv_participant_no] [int] NULL ,
	[rsv_main_fac_id] [int] NULL ,
	[rsv_status] [varchar] (50) NOT NULL ,
	[rsv_cancel_timestamp] [datetime] NULL ,
	[rsv_cancel_usr_id] [varchar] (20) NULL ,
	[rsv_cancel_type] [nvarchar] (255) NULL ,
	[rsv_cancel_reason] [nvarchar] (1000) NULL ,
	[rsv_owner_ent_id] [int] NOT NULL ,
	[rsv_create_timestamp] [datetime] NOT NULL ,
	[rsv_create_usr_id] [varchar] (20) NOT NULL ,
	[rsv_upd_timestamp] [datetime] NOT NULL ,
	[rsv_upd_usr_id] [varchar] (20) NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmRoom] (
	[rom_fac_id] [int] NOT NULL ,
	[rom_ftp_id] [int] NOT NULL ,
	[rom_capacity] [int] NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[fmTimeSlot] (
	[tsl_id] [varchar] (10) NOT NULL ,
	[tsl_owner_ent_id] [int] NOT NULL ,
	[tsl_start_time] [varchar] (50) NOT NULL ,
	[tsl_end_time] [varchar] (50) NOT NULL 
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[fmFacilityType] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmFacilityType] PRIMARY KEY  CLUSTERED 
	(
		[ftp_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmEquipment] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmEquipment] PRIMARY KEY  NONCLUSTERED 
	(
		[eqm_fac_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmFacility] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmFacility] PRIMARY KEY  NONCLUSTERED 
	(
		[fac_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmLocation] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmLocation] PRIMARY KEY  NONCLUSTERED 
	(
		[loc_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmReservation] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmReservation] PRIMARY KEY  NONCLUSTERED 
	(
		[rsv_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmRoom] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmRoom] PRIMARY KEY  NONCLUSTERED 
	(
		[rom_fac_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmTimeSlot] WITH NOCHECK ADD 
	CONSTRAINT [PK_fmTimeSlot] PRIMARY KEY  NONCLUSTERED 
	(
		[tsl_id],
		[tsl_owner_ent_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[fmEquipment] ADD 
	CONSTRAINT [FK_fmEquipment_fmFacility] FOREIGN KEY 
	(
		[eqm_fac_id]
	) REFERENCES [dbo].[fmFacility] (
		[fac_id]
	),
	CONSTRAINT [FK_fmEquipment_fmFacilityType] FOREIGN KEY 
	(
		[eqm_ftp_id]
	) REFERENCES [dbo].[fmFacilityType] (
		[ftp_id]
	)
GO

ALTER TABLE [dbo].[fmFacility] ADD 
	CONSTRAINT [FK_fmFacility_fmFacilityType] FOREIGN KEY 
	(
		[fac_ftp_id]
	) REFERENCES [dbo].[fmFacilityType] (
		[ftp_id]
	)
GO

ALTER TABLE [dbo].[fmFacilitySchedule] ADD 
	CONSTRAINT [FK_fmFacilitySchedule_fmFacility] FOREIGN KEY 
	(
		[fsh_fac_id]
	) REFERENCES [dbo].[fmFacility] (
		[fac_id]
	),
	CONSTRAINT [FK_fmFacilitySchedule_fmReservation] FOREIGN KEY 
	(
		[fsh_rsv_id]
	) REFERENCES [dbo].[fmReservation] (
		[rsv_id]
	)
GO

ALTER TABLE [dbo].[fmFacilityType] ADD 
	CONSTRAINT [FK_fmFacilityType_fmFacilityType] FOREIGN KEY 
	(
		[ftp_parent_ftp_id]
	) REFERENCES [dbo].[fmFacilityType] (
		[ftp_id]
	)
GO

ALTER TABLE [dbo].[fmRoom] ADD 
	CONSTRAINT [FK_fmRoom_fmFacility] FOREIGN KEY 
	(
		[rom_fac_id]
	) REFERENCES [dbo].[fmFacility] (
		[fac_id]
	),
	CONSTRAINT [FK_fmRoom_fmFacilityType] FOREIGN KEY 
	(
		[rom_ftp_id]
	) REFERENCES [dbo].[fmFacilityType] (
		[ftp_id]
	)
GO

ALTER TABLE dbo.acSite ADD
	ste_rsv_min_gap int NULL,
	ste_rsv_min_len int NULL
GO

