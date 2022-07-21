USE [J2.L.P0007TCS]
GO

INSERT INTO [dbo].[ChatLine]
           ([user]
           ,[room]
           ,[content]
           ,[time])
     VALUES
           ('ducnm'
           ,1
           ,'My name is duc'
           ,GETDATE())
DECLARE @id int =0
SET @id= (Select COUNT([id]) from [dbo].ChatLine)
INSERT INTO [dbo].[isReadChatLines]
           ([chatline]
           ,[user]
           ,[isRead])
     VALUES
           (@id
           ,'ducnm'
           ,1)
INSERT INTO [dbo].[isReadChatLines]
           ([chatline]
           ,[user]
           ,[isRead])
     VALUES
           (@id
           ,'boma'
           ,0)
GO


