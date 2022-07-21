# TextChatSystem_Database

Some comments about the table ChatLine: The chat line needs to know which chat room it belongs to, so the room attribute was born to store the room id. The chat line needs to know who created the chat line, so the user attribute is born to store the user id. The chat line needs to know who hasn't read it, so the isReadChatLines table is created to show that relationship.

To avoid Login issue #1: I recommend you to use the command "SQLQueryCreateUser.sql". Note: the program only works correctly when there is at least one chat room and when the data related to the user and the chat room is fully added
