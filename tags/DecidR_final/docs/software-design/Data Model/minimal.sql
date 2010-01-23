-- phpMyAdmin SQL Dump
-- version 3.1.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 22, 2010 at 03:03 PM
-- Server version: 5.1.30
-- PHP Version: 5.2.8

SET FOREIGN_KEY_CHECKS=0;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

SET AUTOCOMMIT=0;
START TRANSACTION;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `decidrdb`
--

--
-- Dumping data for table `activity`
--

REPLACE INTO `activity` (`id`, `mapping`, `name`, `known_web_service_id`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n<mapping xmlns="http://decidr.de/schema/wsmapping" xmlns:ns2="http://docs.oasis-open.org/wsbpel/2.0/varprop">\n    <activity>Decidr-Email</activity>\n    <portType>EmailPT</portType>\n    <operation>sendEmail</operation>\n    <binding>EmailSOAP11Binding</binding>\n    <partnerLinkType>\n        <name>EmailPLT</name>\n        <partnerRole>EmailProvider</partnerRole>\n    </partnerLinkType>\n    <service>Email</service>\n    <servicePort>EmailSOAP11</servicePort>\n    <properties>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tAbstractUserList" name="to"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tAbstractUserList" name="cc"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tAbstractUserList" name="bcc"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="fromName"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="fromAddress"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="subject"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tStringMap" name="headers"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="message"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="bodyHTML"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tIDList" name="attachments"/>\n    </properties>\n    <propertyAliases>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tAbstractUserList" messageType="ns4:sendEmailRequest" propertyName="to">\n            <ns2:query>/tns:sendEmail/tns:to /tns:sendEmail/tns:cc /tns:sendEmail/tns:bcc /tns:sendEmail/tns:fromName /tns:sendEmail/tns:fromAddress /tns:sendEmail/tns:subject /tns:sendEmail/tns:headers /tns:sendEmail/tns:bodyTXT /tns:sendEmail/tns:bodyHTML /tns:sendEmail/tns:attachments</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tAbstractUserList" messageType="ns4:sendEmailRequest" propertyName="cc">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tAbstractUserList" messageType="ns4:sendEmailRequest" propertyName="bcc">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:sendEmailRequest" propertyName="fromName">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:sendEmailRequest" propertyName="fromAddress">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:sendEmailRequest" propertyName="subject">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tStringMap" messageType="ns4:sendEmailRequest" propertyName="headers">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:sendEmailRequest" propertyName="message">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:sendEmailRequest" propertyName="bodyHTML">\n            <ns2:query/>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/Email" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tIDList" messageType="ns4:sendEmailRequest" propertyName="attachments">\n            <ns2:query/>\n        </ns2:propertyAlias>\n    </propertyAliases>\n</mapping>', 'DecidR-Email', 1),
(2, '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n<mapping xmlns="http://decidr.de/schema/wsmapping" xmlns:ns2="http://docs.oasis-open.org/wsbpel/2.0/varprop">\n    <activity>Decidr-HumanTask</activity>\n    <portType>HumanTaskPT</portType>\n    <operation>createTask</operation>\n    <binding>HumanTaskSOAP11</binding>\n    <partnerLinkType>\n        <name>HumanTaskPLT</name>\n        <myRole>HumanTaskClient</myRole>\n        <partnerRole>HumanTaskProvider</partnerRole>\n    </partnerLinkType>\n    <service>HumanTask</service>\n    <servicePort>HumanTaskSOAP11</servicePort>\n    <properties>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tID" name="wfmID"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tID" name="processID"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/DecidrTypes" type="ns3:tID" name="userID"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="taskName"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:boolean" name="userNotification"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="description"/>\n        <ns2:property xmlns:ns3="http://decidr.de/schema/humanTask" type="ns3:tHumanTaskData" name="taskData"/>\n        <ns2:property xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:long" name="taskID"/>\n    </properties>\n    <propertyAliases>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tID" messageType="ns4:createTaskRequest" propertyName="wfmID">\n            <ns2:query>/tns:createTask/tns:wfmID</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tID" messageType="ns4:createTaskRequest" propertyName="processID">\n            <ns2:query>/tns:createTask/tns:processID</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/DecidrTypes" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tID" messageType="ns4:createTaskRequest" propertyName="userID">\n            <ns2:query>/tns:createTask/tns:userID</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:createTaskRequest" propertyName="taskName">\n            <ns2:query>/tns:createTask/tns:taskName</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:boolean" messageType="ns4:createTaskRequest" propertyName="userNotification">\n            <ns2:query>/tns:createTask/tns:userNotification</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:string" messageType="ns4:createTaskRequest" propertyName="description">\n            <ns2:query>/tns:createTask/tns:description</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:ns3="http://decidr.de/schema/humanTask" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="ns3:tHumanTaskData" messageType="ns4:createTaskRequest" propertyName="taskData">\n            <ns2:query>/tns:createTask/tns:taskData</ns2:query>\n        </ns2:propertyAlias>\n        <ns2:propertyAlias xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns4="http://decidr.de/webservices/HumanTask" xmlns:ns5="http://decidr.de/schema/wsmapping" xmlns="" type="xs:long" messageType="ns4:createTaskResponse" propertyName="taskID">\n            <ns2:query>/tns:createTask/tns:taskID</ns2:query>\n        </ns2:propertyAlias>\n    </propertyAliases>\n</mapping>', 'DecidR-HumanTask', 2);

--
-- Dumping data for table `change_email_request`
--


--
-- Dumping data for table `deployed_workflow_model`
--


--
-- Dumping data for table `file`
--

REPLACE INTO `file` (`id`, `fileName`, `mimeType`, `mayPublicRead`, `mayPublicReplace`, `mayPublicDelete`, `fileSizeBytes`, `data`, `creationDate`, `temporary`) VALUES
(1, 'styles1.css', 'text/css', 1, 0, 0, 70, '@import url(../reindeer/styles.css);\r\n\r\n.v-body{\r\nbackground : red;\r\n}', '2010-01-22 11:04:11', 0),
(2, 'styles1.css', 'text/css', 1, 0, 0, 70, '@import url(../reindeer/styles.css);\r\n\r\n.v-body{\r\nbackground : red;\r\n}', '2010-01-22 11:04:11', 0),
(3, 'logo.png', 'image/png', 1, 0, 0, 9509, '‰PNG\r\n\Z\n\0\0\0\rIHDR\0\0:\0\0\0˜\0\0\0ûƒ²Z\0\0 \0IDATxÚí}y˜\\UµïoŸ¡æ©ÇêNw§ÓéÌƒ‘ ˆ"Á	ß{‚^¯^¸Êóúô\nâğ¨¯záİçãú	h¹€€2\Z#I@Ğ@BH:éNwºÓ]=Ô<aïûGu’êœ\Zºªº:½_ıUwS{Õ>û·×Úk­½6aŒƒƒc!@à]ÀÁÁéÊÁÁÁéÊÁÁéÊÁÁÁéÊÁÁÁéÊÁÁéÊÁÁÁéÊÁÁÁéÊÁÁéÊÁÁÁéÊÁÁÁéÊÁÁéÊÁ±‘ú[­IDø\\Ğ¡€&TÖH$$I\\»rpÔ*â»!·!úÙV‹†¸1ÌÁQ³š5×ˆ>=†Øó>Ÿ7™LrºrpÔ$Ò‡ŠÔ>€!ù,šãtåà¨´ÑB¯ÌôÀ¹ÁÀt€©=äñù#‘Ha\rØÃéÊÁ1H\r\0  $™¥Ê\0À\0¤{ Z¤¥ÔôF±ç!ùa_`hh¨ì~\\NWÅ±Ÿ"ö,D7´0`ÌºÄ^ØÖ"òÄi®f_„~mw6‡ÃA³õÜ— øîæÚ•ƒc. HÀøO¡\0S†s©Ç4l«ß=<«`ĞF‘|Ùë’E™y—Ò‹Ô_á<éƒ8ñe$ÿÊ×®sc\0M#ö,&î''‚€iãÓ5äË}HìÉÕìOôÑâKÄ§(X=‚øNX–C¬ÃÈ·~4}òzNW9ğuŠªÇÄı?L¤F°4X\0Ôa¸.Døa°L®2šDø‘ºú–X,PÄwBtÂµã÷aì_ Mo•H@’øcäX\\te''_dbôn8Î†ë"¦ˆ:ÁôÁÜ\\Í¾H¾\nß{D:‚k+""¾Ğ0ÉÌ©×síÊÁ1Gº26ÅXe`*â»1ö#B#p¼\r¡S.`¹nÑ1şoïJĞ†¿ø®\\­¤1Ìµ+Ç¢¡ë®úEhD´ˆ)W³:¹#ß„š®HYT+×®‹‡­\\=½ -€«“ô6ç*w5qp”gíjÀU“õªá-&\\åtåà˜+]«ÆÕJy†9]9™‚­‚^åa…ÁUTĞÕÄ=Ã‹†®ÕáêäĞÑÑQöïÀ‹¿p,Xh.„~™‹H˜I?=VE®2ˆ)†±±TmßŒ†Ç''"´««‹kW3\Z’\0äV¤{Š$^…¹\n=œÿç; 8ƒÑ1§ÓÉ×®‹AÁ†à¹Dª-®r±¡éS£~ÆXSS§+ÇbP°u 	86/0®‚¡n»®„IZ__OátåX 8·@pÕWY[$?<WŒÆAğz½…WNW…NW™©x.­%½Š<·4ı]2z\\Qu¿ß_Ôwåtå8ë‡e9ä%@¯2ûXW…¨ÕjµÙlœ®‹LAİu,ª&WóéU"¢å‹Áñc”ÒbU+§+Ç£`[@,°­­i®‚Á})˜‰1Ç#Š"§+Çb…÷*–ÚåªèAÃÇF†‡\0444”ğ9]9Îˆ^è8ßQ‹¾¥ìŸõ7(É‘dZ/<ĞÊéÊq†B sÎ- ¶\ZŠ¯úÔ\0×…©TL–e—ËU¢ÍÏs†9¾£IAb/\\‚&zÊpÍq5ûc[ƒÖ;\0z¢‡Ó•cñ!ù2,]\Z~éùj£ÍW³®‹Ñô)¦‹¿Xó–Ó•cÁBéMÁ¶‰İˆÿ,]¨\rÜü''ƒ´''áGÑxlëfy°& ær%_Ab/šş\0?-X„;Jp¢şzxşË#Öe|íÊqFC ±–å=û	bÏÁÕœÛÇ]ÃÒ™û-ªXà\Zê?4ùÚwMq®c=†±Ÿâøg²ÉÂL\rpíÊqF‚"±Î-€ˆàP•ân¾ò¤^ÇÄı Zo‡àFòeØÖC9†Àws¯ñ“pl†2€á¯‚÷œ–ëØôRÂ<·¡õĞˆbãÚ•ãLAê5(ƒpØs|»D®bFè \0h¡Úsvµ´i7º/v¹}mñ\\e\0Câ%İĞ¯@l,ÓÏµ+Ç„ô~DŸMÌ)¢ù!·Íüä¡/ á£°m˜ùÿ‰Ÿ!±;›—ÇÙP0|''ÀĞy !ú$¬+a]-€ãŸ+’«ÓkMˆõhú\\hš&I¹ëFğjsÅPLÙ7ßˆï‰?tİºJ6¥Qi™Ğ|"ÿÔ¹åBÌ‚}£¡:ó©i’;+N2ÉÑ=ZWx¶¡ùV58q¬WQõ•+WVƒ®:ƒªS•2Ug\Ze”1‹(Ø$Á.„ë²BÑÙ‰XFH»ÇZå¾Õ({s<¹/È²t"©V«eIÑ¤L<àõ½îm=åDé\\MíÃÄÏ@ìğÿ#$?œç¥\0@9†‘oåŠÙœb+ƒo{é¼ïFğ¢¹j_¶oâğPo<©K’´téRÃï_Z·}â±ÃŠN3:Kk4£ÑìkE§:Í=u	„49å—¥»Î¾¡Ù¹©ÅÕéµrÊ•†¤Jïyiè±CãiğÙ¤nj¹q“¿:¤‰+ïûåE§óòİm6›Íf‹Åb’Î†#ı"O‚ÆŠŒ¯N!R“–¾¦eÛ\0Bà<\0‚ úÄ¤ìºŞ«| ®JMh¾ÍÁ±£ÁcŒùı~Ç,}¢Dº¾:+ÎÇX ®âÊ¾‘øÃÇ\0,õÚŞÙåÛ¾¶©Ímá,ÂÛ¢Ñ›;th<yê?á´vÏŞÁ‰ä7ŞÙU4Êæ‹«§àv»s…BA—İ/ûoCôHìÔ‚s!²Öï&tüğô‡FŸû\0`éÂÒŸNk¯ÿãÓTÖ“/l%ò8\\€µéÃù¹*Xá»õ×''£#½½ºN½^oSSSŞ*0óæˆ¤ÿÿßF®yhÿm8z$˜â<,÷ìšÊÕSxêHğ±Ã‹§!uuõ=¡à(\\£õØVÆU¦Ïü¸È }È¸¢÷TÏ0…÷J\0`háÉ·”É7}ïËÃU\0®w`ùo¨ûİÇú†Y–»ººš››©ØT¢gøm÷¾RŞ®¿fMã­ç¶¹,"''¤¹f»äş})UÏùîúfçı×¬©´ƒÑÌ5í7z÷/Ÿ8{VÉ¤¦D<¾V¨''ú%ÔÑyË14çªeZ¿É?:ÜMPBÈ’%Kìv{áß´&â®Œ±ßûğo¾>šàœ4Á‰˜bÄU\0G«‘âp8<¾Öp8¬dÒhş,{-rUjDÇ=ÑhòèÑ£Ñmhhèîî.Š«¨©4‰áXææG=u$ÈiiÑÔ¿..nï»Ïç¶X$$öœÊÖWƒ:ŠÄn‹Ev8İİİuuu%|ÇÚÊjÒ(»ıù¾oŒqfæÄ·¥Î.½»¾É¹¨{''¾–¥ˆş¾¹š}=öc›«] Zá……kš®Ù¯öí?¦7ÄÉ™c‘|ä-†õ¸>öÖ–ÅÛ5z®‹~xRµÖ WÁ aâgÍMõÉd²´oYş¬¦ooë^Õ`÷Ú$»$ˆÉh4®è£	µ/œ>0šØ==Iç]ÊŞşü±¯muƒStnÜäs<ùôÑ™K†OŸÓvn›{ñöKêu¸¶"ñrír5{AøaÒğß±£GwMĞuUƒ½İs:Â&	6IhtÈëšï^YàÍñä¯>y$hâ”Vuú¥g{ñ¾µ6‰oB˜©`ïº´kK»ç×oŒõ†R"!š7nòoi÷,ŞNÉ…k+ÆÿĞjš«` œ¸½iÉ×C¡P	Ë×yÈ^ÓèøÚ%Ënx‹ÿçûLœ™ıáô½¯ßrn§èl¼wuÃ{W7ğ~˜BéCæH­s5{Kb7”~‘é”z¡8m4oºkuƒıß·¯İÚé3¹æÁı£''b\nfHì…ı,„~µ0¸šışª§~ùØX Øï:Ÿ¦¦U$ß¹¬û\\c+NÕéÿ}u˜Hch°,E|''Ô±ÃUÆéCä±:¥8m4ÏèD‚».]şşŒvu<u$xË¹m>[ér&Tz$˜\ZŒfÂi-­QI YhvZÚÜ–åuöJ‡*S\Zí¥GâÊDJMkTÑ™S¼6Ég“V58\Zì5·ñX8İÉâJRÕàÅz»Ôå³-óÙ¤JwVoÆØhc	\0pÈhq‘õØØÙ@¯Äş÷%ûñBâjöÅø½–î÷†ûü­]†®\0<Vñ¶ó:¾ôloÎwş®''xÃÆæb?6PŸè™x®/|h<I\r|ZvYÜäw^¼ÌwùŠzwùò)Ã_NÄşØ~y(ÖN›xÔÚ<Ö·¶¸®ZÕğö%†Nİìx£7TPºÒ#Ú0ÕÉW”À/<ÑÜ;¤5£¾:»Õµ­»~M££ĞÏ=ÒWşÈp¦f·O¾\ZŒ±ïïa÷0ƒœgâ²àònáŸŞs¦×CÓÆá¾¡‡@SŒ«`Ğ#|×ßú?b±˜Ûí^0tpYwİ}¯Úûåó}¡¢è:Sşõ/''~4h´›ï´êSõ=ƒÑ=ƒÑîºzuÃÍ›[ç¢Æ³ºôWÆv‰däE3CÑÌïO,óÙnx‹ÿš5ÕÏKzêHğŞW†òE×Rª¾k ²k b—Ë—×PÙWw²{^bsD,®à7Ù{V‘tM„ó\\¤öC°M/:k_a É\Zâjö­Èãğß¦$G°°è\nàÚµß}ñxÎ·ö‘Œîµæ%¸ÿo#÷½:œÑŠÛá•Rõ_î}¢''xÛyíW­*Ñãúøá‰{^\Z*m¯ö±púíìäÍñ/oí\\Y_¥hs ¡~õ…c/\rE‹í«ò4?§ïyˆ½Ro‚¬¬''™Uí!»gmÉ7\njnà“ÕãªX‡®ì†ÿÊÑÑÑæææ…D×Ëºë¿·ûxN³‘1¶4q~G¸b$£ñ™Şbß´uPF»ó…c¯Çÿyk§XŒš‹+úWÿØÿ|ß\\3±Œ&¾¹kàß®^]…ÿÛHüóO÷†Rê|=qºåÿ±H“Û·bÖ#aŒ\r\r\rMısö\0Ahoo¯®^€sÁ¦ş¹ÀèZo—VÔ;z&r''gå¥k ¡~úw‡…Ós—äÑCã¡´ömËô¬Œ&Ôx²§,»aÁg·´W¡·÷F?÷û£ó»Ë¼®®j ×oÈÕ]¤½½àîªª\r<ÉF£’K%£†r†Ö79LlE“Cií“*W³øSøkì/äÊ`JûÄc‡Êµsíİ+64W<MÿhbŞ¹ZÜ,vGÕZÚQ-®Vªºh\rV¯Ù†c£·TÊnyòÈ`4“g…à;<VUÊè4œÖzCiÕt¼>Ñ3±Ñï|ÿ:³ƒıTÊny²''oÓ\0|6©Åe©·ËÙÉe<©%”Ù®×ÏœSñ®ñ¤zÛ\nâj]ö;åz»Ì€‘¸ÒªøfZ²¦]>èQvh"«Éêòá\r‡«§5y%J×][\\ƒÓè­î<8f¸©İ.‹Û×4^»®iF%7•²¿ÇÚ?º³?ltï÷wßá]b\\Jêû»ß7ÛZa“„¬o~×òºu³‡¡˜òÒPôÁ×GOinÚÜÚèÈ±9îîË»3úéÿÁæÒÉßØÙoîóÙ¤ë7ú·vzgÌãIõ·ÇØ?\ZËheôÂ×“;/Âš)N¾Ñ$ûùkì;/’;¶–É¬®øŒ×®9ë¤7ÒÀÓ»4ñË£Fw­lp|ë]Ë—æ*¹(äœ6÷9mî…¿òÂ±„’ÃÛ©èô‡{¿õ®å9?|_ ñ+ã¦\\ØéûâK›¹¿T›Û²}Mãö5O÷†ş÷Ÿ|6É(XUZ(5''ëï2]1¾}ó§ß¾$g	F‡|óÙ­×®kºó…c/”K$"rßUä¿¿uæÍrÛò‰Íp–«ò^>®\n.ĞhA\\ÜĞ#Õçjm­]M6ß¾·{ĞÈâèª³ÿôªUKó•G½x™ï»—uy•ë\rDrÛºFa§,®^İø½Ëº¸:Û–×ıâ}k¿rQg¥s†ğã—‡L¼\\·niÿÂùæå²êíÒ?ßQNø''Wæàê)¸-å¡Æ\\]h¼÷AnËÏUë\nt=€Æ›!8LUñ™NW‹h(LÎ|‡W‡ã¯â¹mÜuiW!¡Z\0o_âşğF¿‘7ñ7s”¶xi(fbŸÛî¹ã¢ÎÂÙ·Ämyk‹«Òİû§şˆ‰7îıëš?j¼ñ½RƒïëÈ''6W©±œ\\%<Û°ô^UÚŒô ùÖ<\\Ğzgx¢W³_€å¿†{ˆ`pË™NWİxinÍ¥xMlÑk×5­(&ÙàãgµMÏæªk±ãÀ(3€Ë"ÜyQge‡AG™‘‡\Z–ÔióX«¿Q‘HùŞeUlpWíë°ìçhøØÈpÿñáøDÔK''g™­W][!z''Âl<¤ƒÿ6t?ûºÅèVtf¬xgªª´F''—a''Çî)¯œ\0|x}cQM»,âå+ê_8&\0!É¾ €ÎÍè)Šš2D3>{ö2åb\0{kK“³‚…ÎÛ½6–å,À£''_0ÀÄU)ÛHX%!{ã¿å\rë«_€\\·íîªÓ\0ƒÔŒ¦¿ƒuUhüX8Æ\0455¹İn¦HË—Ğ÷AP%W-o;üŸœèÍ–Û''„D£Q=3V×ş$_Fàn¨#S3%ÎpÏp4­e3œê&¸å™88”â¶J@ÈÔßgµ¸Ú½Eçñ]¹²Ş¨:ù`43ÕµÛI[D!''5‚K»ê*ÚKn«á#³\Z¯&G2F.+›$\\·¡ˆ"OÙC²ÏI×©ªi$»ö€i³*#¦ãõŠÕ_Y/‘¾íğ\\‘Œé§”¹İîÆÆÉÉÈ~Ğ¼ïFèá~àúêJ0–¤ÙÚh‡1÷øø˜ÃÒìèzÁGğĞdå¾C-iWŠ:‡%K9‘@ˆHˆ(‘`vòÀXBíğæ>»ö‚¥ŞZo5#Ä•©t=d¼Yßä´ˆµX=ôğ„¡Ìoñ;‹rr¹,bWı¤aÎ4ÊtN`6M:­ÄcÍj\0ª¢èšN!\0‘¶,©n\rx®w ñ“š(ªnµZı~¿(N—‚ˆ¨¿±ç¡MLãªÜßuCı}‚ ø|¾).:ÒØØ¤iÚÈğ`SãÕbış\n’ûj•®§õ!#Ùßº\n”{Ptjzã19§ãç¯~şZ Œ}4>=PÙ2tØ¬kr &a"sâf´„È"1t;e¬¬?õ—¬3Y£Puh\Zºê«Ú÷‚XGGú)*Bkk«Í–kº''6– 7cä®i*ÿç‘şŒ¢utäpŒK’ÔÒÚJ¥bãG›[¿\nm¼¶èêµIÙeÛ)+4«	%ø\\%>~“ô Ùt-p‡ZYõ6nzE}–n4é®åuÕ’Y$EœòÈU]-GB#¡(cŒ544˜ŸòF¬Ka]\nëJ¤Ÿ<Óqlë''zív{n’\0ìv»İŞ‡¡…}5D×%nCıY²-h’!4Ûî¨âV’.ë qÓko=nœÉ´Î%¢”#ÔårZvæ13ıdÉè½` "Úîš=ªë´µµ5ï½>Ÿ1ïğğğ™ìjÒŒ\0ÌÎvH©ÕËP÷N''¡IÓv¹FË¬¦Œ7\0ÛAiXAººŠ¨±B¬\0à¾±çà¹LE™Ïç›¹Ğ5^#´¶¶Âíâ×ßµı¦‡Y5Ï\nh”UM¶ÖéiÃ&M×ìñ&2Kü\\ûÜ]‚ÿó=húû¡¡~ÆXSSÓüÏ;5Ò9&©ö+êí³½­r°ÓC &¾ßŒÎjsì™P2³pvÒURSGÑx“’N¦ô––š8Ğ¤&èšTéKC†Ç±onÍ‘ WÍ°~ëô…ºI±¢d¹\n£”&	Õ\\V,,¹îm'')Y–¯~VÙ9¤„øí›fï³[sôTƒ]fòş/ë.£û„™[£rØ ØxRí(ßš2Âg“ŒœÃ)Õd“à"×°étZÕôÎÎöZhŞ%¦´ßŸéêwÊ9S‹ËÒgœ°î¨¤ËÇï”¢¾‡Æ“gU>Y¿49d#º\ZOnlvrjæ„Íf+{—l3à{»›,ø.ëÎL_^gşê+_ƒ¦\r•5{ú{‡ñFB~bıÂ<ÓõŞW†Mâ7V‘¼³+÷!:&		&^«²`•ñ1–‘L ¡Öàc6™bzCé‰”Æ™Àéš?ûÛˆyqƒ÷®i4\n3,¯³½õZ 1–¬ g:}6«±sø‘7Çkğ1›g\Z>zhœ3ÓÕI•Şµkà¹>35Xg“®\\iX¡[ ¹=ÆYüäå•\0&Çpí\Zˆ¼1–¬µÇìµŠ&µuë÷Sœœ®3‘Öè£‡&>óDOŞ1ıésÚÌc«&dî	¦~\\<c÷&\nÌ¾¸b…Yzú÷™ÒüDOp¸ÂYĞÛ–›íì»{÷`EM\Z¥ë¡‰d ¡F3zB¥)&T\ZLi=ÁÔÓ½¡oÿùøÍŞñÆ˜šW¬¨_İ''õ¼»ÎÖiìAÙ=ıúÎş˜RP T£ìÁı£ßúóñÇ\rNUšvÕD¼¤J¿ôlßÁ‚Î˜Hiwí\Zxpÿèc}Ò—tùL’%âŠş…§{_›~ıÆ§Í|¡üœ»wÎñ6ù×v†ÕÇ7·Şñü1£wO¤>óDÏK½—u×wx¬³+e8Í¼x<òôÑPvyôĞÄ+¬¤Lİ´¹õóO÷šğÿ_^>ñë7ÆŞ·¶iC³Ó3«j”ÎĞ3‘|º7t*?äå±`J«¯Ø’vI¸¼»şIã˜™JÙö¶¹-Û×6­orÌˆ]gt¶w0úğÁ1î—:£è:Gllv~î¼BkíuùlÛ–×=İkx8\reØÙÙÙ!@‡×Zg“\\Q¥,®è‘´64ëhv•²ßûĞ†ü“E‹ËríÚÆß4sÒêOşr€Ç*6;-^«h—ÅHZ¥µ¡hf¶ñğÁ±›6·V®o?°¾i×@ÄÜâŠ)?zi€Ï&5;e·E´ˆÂH\\©txŒcáÑõ‚¥ŞO]ÜxıÈ[ü½¡ÔÑP:¯7É˜Ná©#Á«W7’hqÍšÆ`êµ@ş¸e4£G3ù}9;û#ŞèwV,ÇCÈ­[Ú¿±³ óDÂi-œæŠtq»šŒ`ÉgÎi+–«\0‚/]ØYÆÚÙ:ƒy½ï©¸í¼¼kìÂÁ*Zİ`¿q“Ÿ{N×ÒqáRïÿ¹bå¹mî’©şõK–­/_á•gûÂ‰Âß‚ŞÚ¹¥İS®¦Ÿé\r)ŞÖ³my]õK\ns,xcXÈÖNïU«\ZšòÜ?ê^°ô÷GCíãVX‡,Ü´¹µp‹”\0Ÿ~û’Í­®Ÿıu$¥ÍiwK³S¾yskŠ³]Ö]×áµş`Ï`²øí8V‘Ôì>AN×òÃ*’³Zİç´¹Ïjq•woôåİu.õîxcì…cáHë±ŠW®l¸¼»®©Îk÷¼m‰ûÑCO	¦‹''­ß)`}ó9mÕÛ¥µ¶Ññ£+WşêÀØÓGƒ…³ï‚¥Ş÷¬jøÂ3½œ9g\Z]-"ÉÖ¤m°KMNK³S^æ³uzm•‹UduãÇ6ù¯ßØü—±G%ÌU–ùlšooswùlsiZÈµk·¯i|-ß=}-ˆç‹úúòæV÷ùK½ŞyØv''ä†Í×­kzñxdgäh0eÔSË|¶sÚÜuú<V1É÷ÇÎH%\n@Õ¢},©SjZ£ŠÎDYœ²àµIõv¹ÀstJCR¥ãIu"¥&=»"•EâE—E¬·Kõv¹¦jS†±¤:šP¢]Ñ©EÜÑg“Ú<V‘×‡átåàà¨õµkõ•†Î˜N''“Éê	áä)­+Æ\0n:k{OS \ZTY§Jòg;Jˆ@j·^§k¡8ËŸ~êÄ§ì	\Z’@dÈ¢PašÒhJ¥æµd‘XEÁ.å\Z—%U=£Ñ¼Ş.Q v©¸¦Mz¾Õm!%Í))•&Uİ$€$‹Ä.	Y0l,Ã´Ò”¡ßxƒ¤ÁNfùÑ®“‡1¦˜2«$Ø%¡åÑÍèIU/ÜÆWu¦êz\\ÑE¸,â\\’ŠbŠPôÂ}Ò:eqE+º$U¬ş1pÑŒ( ¯²]ÍèY(®áš¹è:—§À€´FÓ\Z%€]]±\\ú6¡ÒXFc¥JH‹f´¸B¼Å3''­ÑÈIU^BÓ:c¡´&	Äk•Ìã®å\ZÿŠÎBi­XSÙWèõœ­5A×r™¬ªRu»,ÎİûLiŠNç.c,œÖü®"ËpZËÆZçØ´NY(­ùr¥{>©ÒhF+ùÓ\ng+§kh×r>‡´F3\Z‹58‘Ò4ÊÊ%•£˜Ul0¥©emºÒ=ŸTiLÑIuÌTn—µ’âÏ€HF7*œ—0e<ƒƒsÊS8­©óÔtiP)+pË~Ù¾’ÑÇ‚3†g«YÊ˜ÏV„TqE×(+DI ¢09ÛSÆ(Ë}rLá*''Uªè5	„Ó¦íhõ9ö|$­ø	SeÖSKËæÚµ6Œá\nNÿ‘Œ^`²‘FY*ŸçÃ&	vI0JÖ(Ëè,ur0 @Ï°ÎPuó¦EBœÑ"æ¾J¥,j:=SÈbE{>¡R–ï$8äÜ2§5šT©^T^\rgkkWU’„i+#Æ@Ó(St¦p€’ªÓ„J\n¡M\\ÑM$‘E!¯Kˆ$§,$TšRu‡\\¨-jŞ4\0·U2o\\ˆl)cŠ®êÔ*	…¬˜KÿH©ydN#¨1\0\0àIDATö˜ú¥m’`“„¸¢±‡k×ÚĞ®Ä˜$3ß#!’@lÄì$×*¶ŠÄ|gL6ÇH«HŠZ:eÁ"’}L:ƒ‰g‹\0^›T I-x­bJ#&§N•ÅÕ”Ö¨É½ğÙ\nÚ‰ä´ˆEl ãl]Ğ®&Ø%¡Ş.å%F^ˆÉ/¤ŸMáş`sõâ±I¶KOöÊä“¹"¹†„şp,W<V1¡R“1ÄÒ\Z5	í¨Æ¦JûW\Z5m5^''—k¾+Í¦å’™SmÁÃeh;»:5YÍšĞ5kåC*›¸oÒtVOVÖ<)é«i”™ÜX¬ÌEÈÀµè ]§2–2f’e›ÑYN‡IğÆZaÂ˜4-Vx¦(¹çË+3WÄ‹eíšÓp5qé^†‹ÑMš®Ù½f&©••™¯]kÃ&åšOÉdµ.ÃqFràgd93şúRåéZZÏ›tW±2“¢¼Óœ•g’1œ…M]71äf™±ñr''ÙÍ82ƒ2CÃR¨üBÊ}c	2cs¾Ö„v-³²HŒò~sÒµš#a¶z7jº\n"•ÜDe.ÆÕÄ)Vt-;,¢ QİÈË9ªVcŠ26U™4½geeæÚõÌ3†ˆ¦;™g›¾„T«(Ü­oÒ4«<cKû|ÖĞc%¹\n^ærŠÕ„1\\şç \nÄÈIf˜Ãdşfîylºä/¯ÌÜÕ´Àè:cª.{Ec\r@›è<U\\5iš±Š™Ô€ÌÜÕ´°×®SiVòb´9kö¿BXµV¯3¾IÓ3V¹µ³,¯Ìsr5ñ²ÖµeÏ¥ê\Z+tXˆÑ\rö…XJªÊŒ÷…YŠ]g­rkU9ÇLË¦“sÆ9ãé¡kUÌ¼SÏWŸå1@§¬„ÚŠÌ8Ìš)ŒbN''ëi×œ1\\F™JõñÍ˜}9]«¨]+bæÆs©Cº²Rìq“"	Â,/™¾Ìjkè³¼bµ\0bj‡j”·#§ğkMŸÓ"8CâÌÕ®Å~¬$£ô]½ÈñãLàœfÿÛr*uU‰Òüdb›kI£\rŠ%NyRüu\n&r×ñ|¯]ç¤`IQt5Zwéb1£Ê¤4DNU)B?]¥¬ryó%÷¼$“\ZE)Øb9¦Wª:*¼3™Ó5Ï¤9—qJŠ¼X0Î•Ğ\næŒùÖíœEbÖ4cE›—…/Kîys™)+ÔŒ''eÜ@Wx«µ¶v-ác%cJ\Zƒ™nİŒõˆyÓ¬`}ÅNnF-Şséys™³>§¼Ü1ßé^´¸\Z¸ÏéÌ_»f¯—dÙ±%œ\n™UªŒ™\nIéM›·>[€¥s4^L²³‹pÁHfÊ&ó·I1íà6``¹úú”îåd>Õ£¥ÇlR\n¯dÓ†™Æ\0ERŠ09z_X(à@&½øš»¬ÈÙ¡ğ&\nìùòÊœG†ÂKœf[;İ;ıO‰ÓuAi×|Õ·‹‹õ"<)ìğ4±ø\\È¢v2 2»)Ê(sElw2½MÆsç¦]Yí?“Ueâ¨tÓsIÚ›/™Íd(OÎ©™³œ&]98ª:+pí:c˜ƒƒÓ¬úàF§+§+§+§+§+§+Gâ?G4ÌCdë\0\0\0\0IEND®B`‚', '2010-01-22 11:04:11', 0);

--
-- Dumping data for table `invitation`
--


--
-- Dumping data for table `known_web_service`
--

REPLACE INTO `known_web_service` (`id`, `name`) VALUES
(1, 'Email'),
(2, 'HumanTask');

--
-- Dumping data for table `log`
--


--
-- Dumping data for table `login`
--


--
-- Dumping data for table `password_reset_request`
--


--
-- Dumping data for table `permission`
--


--
-- Dumping data for table `registration_request`
--


--
-- Dumping data for table `role`
--


--
-- Dumping data for table `role_has_permission`
--


--
-- Dumping data for table `server`
--

REPLACE INTO `server` (`id`, `location`, `load`, `locked`, `dynamicallyAdded`, `serverTypeId`, `lastLoadUpdate`) VALUES
(1, 'localhost', 62, 0, 0, 1, '2010-01-15 17:00:18'),
(2, 'localhost:8080', 66, 0, 0, 2, '2010-01-21 04:25:45'),
(3, 'localhost:8080', 34, 0, 0, 3, '2010-01-16 23:11:18'),
(4, 'localhost:8080', 7, 0, 0, 4, '2010-01-21 17:54:34');

--
-- Dumping data for table `server_type`
--

REPLACE INTO `server_type` (`id`, `name`) VALUES
(4, 'Esb'),
(2, 'Ode'),
(1, 'Storage'),
(3, 'WebPortal');

--
-- Dumping data for table `session`
--


--
-- Dumping data for table `start_configuration`
--


--
-- Dumping data for table `system_settings`
--

REPLACE INTO `system_settings` (`id`, `modifiedDate`, `autoAcceptNewTenants`, `systemName`, `baseUrl`, `systemEmailAddress`, `logLevel`, `superAdminId`, `passwordResetRequestLifetimeSeconds`, `registrationRequestLifetimeSeconds`, `changeEmailRequestLifetimeSeconds`, `invitationLifetimeSeconds`, `mtaHostname`, `mtaPort`, `mtaUseTls`, `mtaUsername`, `mtaPassword`, `maxUploadFileSizeBytes`, `maxAttachmentsPerEmail`, `monitorUpdateIntervalSeconds`, `monitorAveragingPeriodSeconds`, `serverPoolInstances`, `minServerLoadForLock`, `maxServerLoadForUnlock`, `maxServerLoadForShutdown`, `minUnlockedServers`, `minWorkflowInstancesForLock`, `maxWorkflowInstancesForUnlock`, `maxWorkflowInstancesForShutdown`) VALUES
(1, '1986-01-28 11:04:11', 0, 'DecidR Test System', 'iaassrv4stud.informatik.uni-stuttgart.de:8080/WebPortal', 'test@decidr.de', 'DEBUG', 1, 259200, 259200, 259200, 259200, 'smtp.googlemail.com', 0, 1, 'decidr.iaas@googlemail.com', 'DecidR0809', 10485760, 10, 60, 300, 5, 80, 0, 20, 1, 10, 8, 1);

--
-- Dumping data for table `tenant`
--

REPLACE INTO `tenant` (`id`, `name`, `description`, `logoId`, `simpleColorSchemeId`, `advancedColorSchemeId`, `currentColorSchemeId`, `approvedSince`, `adminId`) VALUES
(1, 'DefaultTenant', 'This is the default tenant description', 3, 2, 1, 1, '2010-01-22 11:04:11', 1);

--
-- Dumping data for table `user`
--

REPLACE INTO `user` (`id`, `authKey`, `email`, `disabledSince`, `unavailableSince`, `registeredSince`, `creationDate`, `currentTenantId`) VALUES
(1, NULL, 'user_0_e88bd858@test.decidr.de', NULL, NULL, '2010-01-22 11:04:11', '2010-01-22 11:03:37', NULL);

--
-- Dumping data for table `user_administrates_workflow_instance`
--


--
-- Dumping data for table `user_administrates_workflow_model`
--


--
-- Dumping data for table `user_has_file_access`
--


--
-- Dumping data for table `user_is_member_of_tenant`
--


--
-- Dumping data for table `user_participates_in_workflow`
--


--
-- Dumping data for table `user_profile`
--

REPLACE INTO `user_profile` (`id`, `username`, `passwordHash`, `passwordSalt`, `firstName`, `lastName`, `street`, `postalCode`, `city`) VALUES
(1, 'user0', '0f5a6c77e10f8c761467e4fa799a0eb9873c2a360d940fef055cda03bca8dbca3adbeaabd9ab6c4dfdbef821a59c6a0498d15a18b39631acc3bb4d0675ddf79a', '55a42832f6252ccd8ce6b6620601d9194ca131806963697817a36e21d945c8a38bc8112ff33270a32c8bdc79979b205e5ce28821e2042412c0c0dc21e3eaec0e', 'Gordon', 'Ğ˜Ğ²Ğ°Ğ½Ğ¾Ğ²', 'Silly Lane 859', '66123', 'ĞšĞ¸Ñ—Ğ²');

--
-- Dumping data for table `workflow_instance`
--


--
-- Dumping data for table `workflow_model`
--


--
-- Dumping data for table `workflow_model_is_deployed_on_server`
--


--
-- Dumping data for table `work_item`
--


--
-- Dumping data for table `work_item_contains_file`
--


SET FOREIGN_KEY_CHECKS=1;

COMMIT;
