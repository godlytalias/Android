<?php 
$server="localhost";
$sqlid="ENTER YOUR SQL ID HERE";
$sqlpass="ENTER YOUR SQL PASSWORD HERE";
$dbase="ENTER YOUR DATABASE NAME HERE";
$connection=mysql_connect($server,$sqlid,$sqlpass);

if($connection && file_exists($_POST['FILE']))
echo "true";
else
echo "false";
?>