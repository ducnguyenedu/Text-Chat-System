USE [J2.L.P0007TCS]
GO
/****** Object:  Table [dbo].[ChatLine]    Script Date: 5/23/2021 10:19:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChatLine](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user] [varchar](150) NOT NULL,
	[room] [int] NOT NULL,
	[content] [nvarchar](150) NOT NULL,
	[time] [date] NOT NULL,
 CONSTRAINT [PK_ChatLine] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChatRoom]    Script Date: 5/23/2021 10:19:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChatRoom](
	[id] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_ChatRoom] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[isReadChatLines]    Script Date: 5/23/2021 10:19:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[isReadChatLines](
	[chatline] [int] NOT NULL,
	[user] [varchar](150) NOT NULL,
	[isRead] [bit] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Members]    Script Date: 5/23/2021 10:19:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Members](
	[room] [int] NOT NULL,
	[user] [varchar](150) NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 5/23/2021 10:19:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[login] [varchar](150) NOT NULL,
	[fullName] [varchar](150) NOT NULL,
	[password] [varchar](150) NOT NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[login] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ChatLine]  WITH CHECK ADD  CONSTRAINT [FK_ChatLine_ChatRoom] FOREIGN KEY([room])
REFERENCES [dbo].[ChatRoom] ([id])
GO
ALTER TABLE [dbo].[ChatLine] CHECK CONSTRAINT [FK_ChatLine_ChatRoom]
GO
ALTER TABLE [dbo].[ChatLine]  WITH CHECK ADD  CONSTRAINT [FK_ChatLine_User] FOREIGN KEY([user])
REFERENCES [dbo].[User] ([login])
GO
ALTER TABLE [dbo].[ChatLine] CHECK CONSTRAINT [FK_ChatLine_User]
GO
ALTER TABLE [dbo].[isReadChatLines]  WITH CHECK ADD  CONSTRAINT [FK_isReadChatLines_ChatLine] FOREIGN KEY([chatline])
REFERENCES [dbo].[ChatLine] ([id])
GO
ALTER TABLE [dbo].[isReadChatLines] CHECK CONSTRAINT [FK_isReadChatLines_ChatLine]
GO
ALTER TABLE [dbo].[isReadChatLines]  WITH CHECK ADD  CONSTRAINT [FK_isReadChatLines_User] FOREIGN KEY([user])
REFERENCES [dbo].[User] ([login])
GO
ALTER TABLE [dbo].[isReadChatLines] CHECK CONSTRAINT [FK_isReadChatLines_User]
GO
ALTER TABLE [dbo].[Members]  WITH CHECK ADD  CONSTRAINT [FK_Members_ChatRoom] FOREIGN KEY([room])
REFERENCES [dbo].[ChatRoom] ([id])
GO
ALTER TABLE [dbo].[Members] CHECK CONSTRAINT [FK_Members_ChatRoom]
GO
ALTER TABLE [dbo].[Members]  WITH CHECK ADD  CONSTRAINT [FK_Members_User] FOREIGN KEY([user])
REFERENCES [dbo].[User] ([login])
GO
ALTER TABLE [dbo].[Members] CHECK CONSTRAINT [FK_Members_User]
GO
