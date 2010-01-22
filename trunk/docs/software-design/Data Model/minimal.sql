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
(3, 'logo.png', 'image/png', 1, 0, 0, 9509, '�PNG\r\n\Z\n\0\0\0\rIHDR\0\0:\0\0\0�\0\0\0���Z\0\0 \0IDATx��}y�\\U��o�����Nw���̃����"�	�{�^�^�����\n�����z�����	h���2\Z#I@�@BH:�Nw��]=�<�a��Gu��\Z���:�_�Uw�S{�>����k��6a����c!@�]������������������������������������������������������������[�ID���\\С��&T�H$�$I\\�rp�*�!�!��V���1��Q��5��>=���>�7�Lr�rp�$҇��>�!�,��t����B�������t���=���#�Ha\r������1H\r\0��$���\0�\0�{��Z����F��!�a_`hh��~\\NW�Ł��"�,D7�0`̺�^��"��i�f_�~mw6��A��ܗ ���ڕ�c.�H��O�\0S�s��4l��=<��`�F�|��E�y�ҋ�_�<�8�e$��׮sc\0M#�,&�''��i��5��}H����O���Kħ(X=��NX�C��ȷ~4}�zNW�9�u�����?L�F�4X\0�a�.D�a�L�2�D�����X,P�wBtµ��a�_��Mo�H@��c�X\\te''_d�b�n8Ά�"��:�����\\;H�\n�{D:��k+"�"��0�̩�s���1G�26�Xe`*�1�#B#p�\r�S.`�n�1�o�J������\\��1̵+Ǣ������EhD��)W�:�#߄��HYT+׮���\\=��-����6�*w5qp�g�j�U����-&\\�t���+]���Jy�9]9����^�a���UT���=�����������Q�����p,Xh.�~��H�I?=VE�2��)���Tmߌ��''"����kW�3\Z�\0�V�{�$^��\n=���; 8��1���׮�A���D�-�r���S��~�XSS�+�bP�u�	86/0���n���IZ__O�t�X 8�@p�WY�[$?<W���A�z��WNW��NW��x.�%��<�4�]2z\\Qu��_�w�t�8�e9�%@�2�XW����j��l���LA�u,�&W��U"����c��bU+�+��`[@,���i���})��1��#�"�+�b���*����A��F��\0444��9]9��^�8�Q�����7(ɑdZ/<����q�B s�- �\Z������\0ׅ�TL�e��U���s�9��IAb/\\�&z�p�q5�c[��;\0z��ӕc�!�2,]�\Z~��j��W�����)���X�ӕc�B�M���݈�,]�\r��''��''�G�xl�fy�& �r%_Ab/��\0?-X�;Jp��zx��#�e|��qFC� ���=�	b��՜��]�ҙ�-�X�\Z�?4��wMq�c=�����g���L\rp��qF�"��-���P���n���^��� Zo��F�e��C9��ws��pl�2���������R�<������b�ڕ�LA�5(�p���s|�D�bF� \0h��sv��i7�/�v�}m�\\e\0C�%ݎЯ@l,�ϵ+���~D�M�)��!����/�ᣰm�����!�;����P0|''��y !�$�+a]-��+���kM��h�\\h�&I��F�js�PL�7����?tݺJ6�Qi��|"��ԁ��B̂}��:��i�;�+N2��=ZWx���V58q�WQ��+WV��:��S�2Ug\Ze�1�(�$�.���B�ىXFH��Z��({s<�/�Ȳt"�V�eIѤL<����m=�D�\\M����@���#$?�灥\0@9��o�ٜb+�o{��F����j_��o��Po<�K��t�R��_Z�}�ÊN3:Kk4���kE�:�=u	�49����ξ�ٹ����rʕ��J�yi�C�i��٤�nj�q��:��+���E����m6��f��b�Ά�#�"O�Ɗ��N!R�����e�\0B�<\0� �Ĥ��ޫ|��JMh�������c���~��,}�D��:+Ν�X ��ʾ����\0,�����۾���m�,�ۢћ;th<y�?�v������7��U4�拫��v�s�BA��/�oC�H�Ԃs!���&t���F����\0`��ҟNk����T��/�l%�8\\������*X���''�#���N�^oSSS�*0�������F�yh�m8z$��<,�����Sx�H����!uu�=��(\\���V�U����� }ȸ��T�0��J\0`h�ɷ���7}���U\0�w`�o������Y���������T�g�m��Rޮ�fM�綹,"''��f���})U����f��׬�����5�7z�/�8{Vɤ�D<�V�''�%��y�14�eZ��?:�MPBȒ%K�v{�ߴ&⮌����o�>���4���b�U\0G���p8<��p8�d�h�,{-rUjD�=�h��ѣ�mhh���.����4��X��G=u$�ii�Կ..n���X$$�����W�:��n�Ev8���uuu%|���j�(����o�qf�����.���ɹ�{''��������}=�c��] Zᅅk��ٯ��?�7�əc�|�-���>�֖��5z��~xR�� W���a�g�M��d��oY���oo�^�`��$�$��h4��	�/�>0��==I�]������mu��Stn��s<��љK�O��vn�{��K�u��"�r�r5{A�a�����GwM�uU���s:�&	6Iht���^Y������>y$h�Vu��g{�6�oB��`ﺴkK���o���R"!��7n�oi�,�N��k+���j��`����i��C�P	��y�^����%�nx�����L��������rn��l�wu�{W7�~��B�C�H�s5{Kb7�~��z�8m4o�ku��߷����3�����''b\n�fH��,�~�0������~��X���:���U$߹��\\c+N���}u�Hch�,E|''Ա�UƐ�C�:�8m4��D��.]���vu<u$x˹m>[�r&Tz$�\Z�f�i-�QI YhvZ�ܖ�u�J�*S\Z��G��DJMkTљS�6�g�V58\Z�5���X8���JR����z���-�٤JwVo��hc	\0p�hq�����@����%��B�j�����������]��\0<V��:��lo�w���''x���b?6�P��x�/|h<I\r|ZvY��w^��w��zw��)�_N���~y(�N�x��<ַ���Z���%�N��x�7TP��#�0��W��/�<��;��5��:�յ��~M����=�W��p�f�O�\Z����a��0��g���n�ށs��C�����@S��`�#|���?b����^0tpYw�}�����}���:S��/''~4h����S�=��=����zu�͛[�Ƴ��W�v�d�E3C���O,��nx���5���Kz�H��W��E�R��k �k b�˗םP�Ww�{^bsD,��7�{V�tM��\\��C�M/:k_a��\Z�j�����ߦ$G���\n�ڵ��}�xη�����%��o#��:�ъ��R�_�}�''x�y�W�*�����{^\Z*m���p������/o�\\Y_�hs �~��c/\rE���4?��y��R�o���''�U�!�gm�7\njn����X�����������D�˺뿷�xN��1�4q~G��b$���bߴuPF��c���yk�X���+�W���|�\\3��&��k�߮^]���H��O��R�|=q�����H��۷b�#a�\r\r\rM�s�\0Ahoo��^���s������Zo�V�;z&r''g�k �~�w����s���C㡴��m����&�x��,�a�g��W���F?����˼��j �o��]����\r<�F��K%��r��79LlE�Ci퓏*W��S�k�/��`J��c�ʵs��+64W<M�hb޹Z�,vG�Z�Q-�V��h\rV�نc��T�ny��`4�g���;<V�U��4��zCi�t�>�3���|�:���T�ny�''o�\0|6��e�����e<��%�ٮ�ϜS���z�\n�j�]�;�z�̀�����fZ��]>�Qvh"�����\r���5y%J�][\\����<8f���.���4^��iF%7�����?��?lt��w���]b\\J����7�Za���o~��u������P���GOin����ȱ9��˻3����������o��٤�7��vzg̞�I����?\Z�he��ד;/)N��$��k�;/�;�������׮9�7��ӻ4���Fw�lp|�]˗�*�(�6�9m�����±���۩��{����9?|_ �+�\\����K����T�۲}M��5�O�����|6�(XUZ(5''���2]1�}�߾$g	�F�|�٭׮k��c/��K$"r�U俿u��r���p���^>�\n.�hA\\��#��jm�]M6���{���誳���UK�G�x�ﻗuy���\rDrۺFa�,�^���˺��:ۖ���}k�rQg�s��㗇L�\\�ni��������?��QN�''W���)�-���\\]h���An��U�\nt=�ƛ!8LU�NW�h(L�|�W���m��uiW!�Z\0o_���F��7�7s��xi(fb�������ٷ�myk���������7���?j��R����''6W���\\%<۰�^Uڐ�����<\\�zgx�W�_�忆{�`p˙NW�xinͥxMl�k�5�(&���g�M��k���(3��"�yQge��AG����\Z��i�X��Q�H��eUlpW����h���p����D�K''g��W][!z''�l<�����6t?����Vtf�xg���F''�a''��)��\0|x}cQM�,��+�_8&\0!ɾ ����)��2D3�>{�2�b\0{kK�����۽6��,��''_0�ĎU)�HX%!{���\r��_�\\����\0�Ԍ���uUh�X8�\0455��n�H˗��AP%W-o;����͖�''�D�Q=3V��$_F�n�#S3%�p�p4���e3��&��88���J�@���g��ڽE��]��ި:�`43յ�I[D!''5�K��*�Kn��#�\Z�&�G2F.+�$\\���"O�C���Iש�i$���i�*#�����_Y/����\\��駔������ɝ�~���F��~���J0�������h��1��������z�G��d�C-iW�:�%K9�@�H�(�`v��XB���>�����Zo5#�ĕ�t=d�Y�䴈�X=�����o�;�rr�,bW���a�4�t�N�`6M:��c�j\0���N!\0��,�n\rx�w��(�n�Z�~�(N�������ML���uC�}� �|�).:��ؤi���`S��b���\n��j����!#�ߺ\n�{Ptjz��19���~�Z��}4>=P�2tجkr�&a"s�f���"1t;e��?���3Y�Puh\Z������XGG�)*Bkk�͖k�''6� �7c�i*������ut�p�K���ڞJ�b�G�[�\nm����I�e�)+4�	%��\\%>~����t-p�ZY���6nzE}��n4��uՒY$E���U]-GB#�(c�544���F�Ka]\n�J��<�ql�''z�v{n�\0�v������}��5D�%nC�Y�-h�!4��V�.�q�k�o=n�ɴ�%��#��r�Zv�13�dɝ�` "��=�봵�5�>��1�����j��\0��vH���P�N''�I�v�Fˬ��7\0�AiXA�����B��\0����LE��盹�5^#�������ߵ����Y5�\n�h�UM���i�&M���&2K�\\��]���=h����~�XSS���;5�9&��+���r��C &�ߌ�js�P2�pv�URSG�x��N�����8Ф&�T�KC�Ǳon͑�WͰ~��I��d�\n��&	��\\V,,��m'')Y��~V�9����f��[s�T�]�f��/�.�����[�rؠ�xR�(��2�g����)�d��"װ�tZ�����Zh�%������w�9S���g�������ƓgU>Y�49d#�\ZOnlvrj��f+{�l3�{���,�.��L_^g��+_��\r�5{�{��FB~b��<���W�M�7V���+�!:&		&^��`��1��L ���c6�bzC鉔ƙ��?�ۈyq���i4\n3,����Z 1�� g:}6��s��7�k�1�g\Z>zh�3���I�޵k�>35Xg��\\iX�[ �=�Y�����\0&�p�\Z��1����쵊&�u���S���3��裇&>�DO�1��s��c�&d�	�~\\<c��&\n̾�b�Yz�����DOp��Y�ۖ���{�`EM�\Z�롉d �F3zB�)�&T\ZLi=��ӽ�o���͏��Ƙ��W��_ݐ''�����i�A�=�����RP T����������\rNU��v��D��J��lߞ����Hiw�\Zxp��c}җt�L�%����{_��~����|�����w��6���v���7����1�wO�>�D�K��u�wx��+e8ͼx<���Pvy���+��Lݴ���O����_^>��7�޷�iC��3�j���3�|�7t*?���`J����vI����I㘙J����-��6�or̈]gt�w0���1�:��:Gllv~�Bk�u�lۖ�=�kx8\re����!@��Zg�\\Q�,�葴64�hv����І��E��r����4s��O�r��*6;-^�h��HZ���hf�������6�V�o?��i�@����)?zi��&5;e�E���H\\�tx�c������O�]�x��[�����P:�7ɘ�N�#��W7�hq͚ƞ`�@��e4�G3�}9;�#��wV,�Cȭ[ڿ����D�i-��tq���`�g�i+��\0�/]�Y���:�y�喝��k���*Z�`�q���{N��q�R���b�m��K��/_�g�����ڹ��S����\r)�ֳmy]�K\ns,xcX��N�U�\Z���?�^���GC���VX�,ܴ��p��\0�~��ͭ���u$��iwK�S�ysk��]�]���`�`���8V���>AN���*��Z�紹�jq�wo���u.��xc�c�H뱊W�l������k��m���CO	��''��)`}�9m�ۥ����+W�����G������j��3��9g\Z]-"�֤m�KMNK�S^�uzm��Udu��6��������G�%�U��l��oosw�lsiZȵk��i|-�=}-��������V��K���y�v''䆍�׭kz�xdg�h0e�S�|�s��u�<V1����H%\n@��},�SjZ���DY���I�v��stJCR��Iu"�&=�"�E�E�E��K�v��jS���:�P�]ѩE��g��<V�ׇ�t�����k���ΘN''����	��)�+�\0��n:k{OS �\ZTY�J�g;J�@j�^�k�8˟�~�ħ�	\Z�@d�ȢP�a��hJ���d�XE�.�\Z��%U=�Ѽ�.Q v���Mz��m!%�))�&U�$�$��.	Y0l,ô����x���Nf��Ѯ���1��2�$�%�����IU/��Wu��z\\�E��,�\\��b��P��}�:eqE�+�$�U��1pь�(���]��Y(�ᚹ�:�����F�\Z%�]]�\\�6��XFc�JH�f��B��3''���IU^B�:c��&	�k����\Z���Bi�X�S�W����5A�r����Ru�,���Li�N�.c,����"�pZ��Z�شNY(���r�{>��hF+��\ng+�k�h�r>��F3\Z��58��4��%���Ul0��em��=�TiL�Iu�Tn����πHF7*��0e<��s�S8����tiP)+p�~پ��ǂ3�g�Yʘ�V�TqE�(+DI �09�S�(�}rL�*''U��5��	�Ӧ�h�9�|$��	Se�SK��ڵ6��\nN���^`��FY*���&	vI0J�(��,ur0�@ϰΐPu�EB��"�J�,j:=S�bE{>�R��$�8��2�5�T�^T^\rgk�kW�U��i+#�@�(St�p���ӄJ\n�M\\�M$�E!�K�$�,$T�Ru�\\�-j�4\0�U2o\\�l)c����*	���K�H�ydN#�1\0\0�IDAT����m�`�������k��ЮĘ$3�#!�@l��$��*���|gL6�H�H�Z:e�"�}L:��g�\0^�T�I-x�bJ#&�N��Ք֨ɽ��\nډ䴈El��l]Ю&�%��.�%F^���/��M��`s��I�KO��䓹"����p,W<V1�R�1��\Z5	���J�W�\Z5m5^''�k�+��咙Sm��eh;�:5Y͚�5k��C*��o�tVOV�<)�i���X��E�����]�2�2f�e��YN��I��Za4-Vx�(���+3Wċe��p5q��^����M��ٽf&�����]k�&�O�d�.�qFr�gd�93��R��ZZϛtW�2���Ӝ�g�1��M]71�f���r''��82�2C�R��B�}c	2cs�քv-��H��~sҵ�#a�z7j�\n"��De.���)Vt-;,��Q�Ȑ�9�Vc�26U�4�gee����3���;�g���T�(��o�4�<cK�|���c%�\n^�r�Մ1\\�� \n�ȁIf��d�f�yl��/���մ��:c�.{Ec\r@���<U\\5i����Ԁ��մ�׮SiV�b�9k��BX�V�3��I�3V���,��sr5�ֵeϥ�\Z+tX��\r��XJ��ʌ��Y�]g�rkU�9�L˦�s�9���kU̼S�W��1@���ڊ�8�̚)�bN''�iל1\\F�J��͘}9]��]+b��s�C��R�q�"	�,/��̞jk購b�\0bj�j��#��kM��"8C��ծ�~�$��]����L��f��r*uU����db�kI��\r�%NyR�u\n&r��|�]�`IQt5Zw�b1�ʤ4DNU)B�?]��ry�%��$�\ZE)�b9�W�:*�3��5Ϥ9�qJ��X0Ε�\n����Eb�4cE���/K�ys�)+Ԍ''e�@Wx���v-�c%cJ�\Z��n����yӬ`}�NnF-��s�ys��>���1��^��\Z����_�f���dٱ%�\n�U���\nI�M��>[��s4^L���p��Hf�&�I1��6``������d>գ��lR\n�dӆ��\0ER�09z�_X(�@&������١�&\n���ʜG��K�f[;�;�O��uAi�|շ���"<)��4��\\Ȣv2�2�)�(sElw2�M�s��]Y�?�Ue�t�sIڛ/��d(O�����&]98�:+p�:c���Ӭ��F�+�+�+�+�+�+G��?G4�Cd�\0\0\0\0IEND�B`�', '2010-01-22 11:04:11', 0);

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
(1, 'user0', '0f5a6c77e10f8c761467e4fa799a0eb9873c2a360d940fef055cda03bca8dbca3adbeaabd9ab6c4dfdbef821a59c6a0498d15a18b39631acc3bb4d0675ddf79a', '55a42832f6252ccd8ce6b6620601d9194ca131806963697817a36e21d945c8a38bc8112ff33270a32c8bdc79979b205e5ce28821e2042412c0c0dc21e3eaec0e', 'Gordon', 'Иванов', 'Silly Lane 859', '66123', 'Київ');

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
