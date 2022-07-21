USE [J2.L.P0007TCS]
GO

DECLARE @apper INT = 1;
set @apper = (Select COUNT([login]) FROM [dbo].[User]
WHERE [login] = 'ducnm1');

IF @apper =0 
BEGIN
INSERT [dbo].[User] ([login], [fullName], [password]) VALUES (N'ducnm1', N'Nguyen Minh Duc', N'1234')


DECLARE @n INT = 0;
set @n = (Select COUNT([login]) from [dbo].[User])
DECLARE @i INT = 1;
DECLARE @alogin varchar(150) =''
DECLARE @ylogin varchar(150) =''
set @ylogin = (Select [login] from [dbo].[User] WHERE [id]=@n)
DECLARE @roomid int =0;
WHILE @i < @n
BEGIN
       set @alogin = (Select [login] from [dbo].[User] WHERE [id]=@i)
    
     INSERT INTO [dbo].[ChatRoom] DEFAULT
     VALUES
	 set @roomid = (Select COUNT([id]) from [dbo].ChatRoom);
	 INSERT INTO [dbo].[Members]
           ([room]
           ,[user])
     VALUES
           (@roomid
           ,@ylogin)

		   INSERT INTO [dbo].[Members]
           ([room]
           ,[user])
     VALUES
           (@roomid
           ,@alogin)

    SET @i = @i + 1

END
SELECT * FROM dbo.[User] WHERE [login] = 'ducnm1'
END 

