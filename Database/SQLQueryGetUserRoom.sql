SELECT u.[login] as ylogin,u.[password] as ypass, u.[fullName] as yfullname , cr.id as crid , au.[login] as alogin ,  au.[fullName] as afullname ,
isNULL(cl.id,-1) as clid, isNULL(cl.[user],'') as sender, isNULL(cl.content,'') as content, isNULL(cl.[user],'') as uchat, isNULL(ircl.isRead,0) as isRead, isNULL(ircl.[user],'') as uread
FROM     dbo.[User] u INNER JOIN
                  dbo.Members m ON u.[login] = m.[user] INNER JOIN dbo.[ChatRoom] cr on cr.id = m.[room]
				  INNER JOIN dbo.Members am ON cr.id = am.[room]
				  INNER JOIN dbo.[User] au ON au.[login] = am.[user]
				  LEFT JOIN dbo.[ChatLine] cl ON cl.[room] = cr.[id]
				  LEFT JOIN dbo.[isReadChatLines] ircl ON ircl.[chatline] = cl.[id]
				  WHERE u.[login] ='boma' AND u.[login] != au.[login] 
				  Order by crid