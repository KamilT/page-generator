# page-generator
HTML page generator

Used to generate HTML pages from template files.

Template extension: .pg
Include extension: .inc


Commands:

##Include:includes\\example.inc,arg1,arg2## - Includes example.inc into processed file, args are optional

##Arg:1## - Includes content of arg number 1 (could be :2, :3..) 

##If:1:Blue:IsBlue## - If arg 1 content is 'Blue', incdlue 'IsBlue'
