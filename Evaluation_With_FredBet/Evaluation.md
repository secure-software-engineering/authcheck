# Evaluation with FredBet!
This guide details analysing the **[FredBet](https://github.com/fred4jupiter/fredbet)** project version 2.2.1 by AuthCheck. 

In the folder *XYZ*, there is a main configuration and an input model file, developed for each of the 22 controller's analysis, present in the **FredBet** project. Although, there is no need for having separate files for each of the controller, but they help in analyzing, evaluating and experimenting with each controller separately. However, the configurations and input model files could be merged easily, if it is desired. 

The input model files list all the method which mapped onto some path, with or without authorization restrictions in the controllers. 

## Controllers and their permissions 

Following is the list of the 22 controllers and their permissions in the project, in lexicographical order.

|    Controller  |	Groups           	 |    Permissions              |
|----------------|-------------------------------|-----------------------------|
|AdminController|ROLE_ADMIN|PERM_ADMINISTRATION, PERM_SHOW_ACTIVE_USERS, PERM_SHOW_LAST_LOGINS |
|BetController|||
|ConfigurationController|ROLE_ADMIN|PERM_ADMINISTRATION|
|CreateEditMatchController|ROLE_ADMIN |PERM_CREATE_MATCH,<br> PERM_EDIT_MATCH,<br> PERM_DELETE_MATCH|
|DatabaseBackupController|ROLE_ADMIN|PERM_ADMINISTRATION|
|ExcelExportController|ROLE_ADMIN|PERM_ADMINISTRATION|
|ExcelImportController|ROLE_ADMIN|PERM_ADMINISTRATION|
|ExtraBetController|||
|HomeController|||
|ImageCroppingController|||
|ImageGalleryController|||
|ImageGroupController|ROLE_ADMIN|PERM_EDIT_IMAGE_GROUP|
|ImageUploadController|||
|InfoController|*Ambigious*|PERM_EDIT_INFOS_RULES|
|MatchController|||
|MatchResultController|*Ambigious*|PERM_EDIT_MATCH_RESULT|
|PointsFrequencyController|||
|RankingController|||
|RuntimeConfigurationController|ROLE_ADMIN |PERM_ADMINISTRATION|
|SystemInfoController|ROLE_ADMIN |PERM_ADMINISTRATION|
|UserController|*Ambigious*|PERM_EDIT_USER,<br> PERM_DELETE_USER,<br> PERM_CREATE_USER, <br>PERM_PASSWORD_RESET|
|UserProfileController|||

, and after them the schema of groups and permission is listed.

## Group/Permission schema

Following is the Group/Permission schema in the **FredBet** project.

|    Group       |    Permissions              |
|----------------|----------------------------|
|ROLE_USER| |
|ROLE_USER_ENTER_RESULTS|PERM_EDIT_MATCH_RESULT|
|ROLE_SUB_ADMIN|PERM_EDIT_MATCH,<br> PERM_EDIT_MATCH_RESULT,<br> PERM_CREATE_USER,<br> PERM_USER_ADMINISTRATION,<br> PERM_PASSWORD_RESET,<br> PERM_EDIT_USER,<br> PERM_EDIT_INFOS_RULES,<br> PERM_EDIT_INFOS_PRICES |
|ROLE_ADMIN|PERM_CREATE_MATCH,<br> PERM_EDIT_MATCH,<br> PERM_EDIT_MATCH_RESULT,<br> PERM_DELETE_MATCH,<br> PERM_CREATE_USER,<br> PERM_EDIT_USER,<br> PERM_DELETE_USER,<br> PERM_PASSWORD_RESET,<br> PERM_USER_ADMINISTRATION,<br> PERM_SYSTEM_INFO,<br> PERM_ADMINISTRATION,<br> PERM_CHANGE_USER_ROLE,<br> PERM_EDIT_INFOS_RULES,<br> PERM_EDIT_INFOS_PRICES,<br> PERM_SHOW_ACTIVE_USERS,<br> PERM_SHOW_LAST_LOGINS,<br> PERM_EDIT_IMAGE_GROUP,<br> PERM_DOWNLOAD_IMAGES,<br> PERM_DELETE_ALL_IMAGES
 |

## Input models introducing CWEs

There is also *ZYX* folder which contains the configuration files and inaccurate input models for introducing CWE 862 and CWE 863, each in two controllers. They as aforementioned, do not need to be separate files and can be merged easily into two files, if it is desired. The CWEs and their controllers are listed below.

|    CWE Type       |    Controller              |
|-------------------|----------------------------|
|CWE 862|PointsFrequencyController,<br> UserProfileContoller|
|CWE 863|AdminController,<br> ImageGroupController|

The changes made in the input models of the controllers (peresent in XYZ) for each CWE are:
	
- CWE 862: 
  + PointsFrequencyController: Added an "authorizationExpression" : "hasAuthority('PERM_ADMINISTRATION')" on its show(...) method in its input model.
  + UserProfileContoller: Added an "authorizationExpression" : "hasAuthority('PERM_ADMINISTRATION')" on its changePassword(...) and changeUsername(...) methods in its input model.

- CWE 863:
  + AdminController: Changed the "authorizationExpression" :  "hasAuthority('PERM_SHOW_LAST_LOGINS')" to "hasAuthority('PERM_ADMINISTRATION')" of showLastLogins(...) method in its input model.
  + ImageGroupController: Changed the "authorizationExpression" : "hasAuthority('PERM_EDIT_IMAGE_GROUP')" to "hasAuthority('PERM_ADMINISTRATION')" of show() and deleteImage() methods in its input model.






